package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreator;
import edu.kit.typicalc.view.main.MainViewImpl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * The main user interface. It provides a way to show the proof tree, the constrains, the
 * unification, the most general unifier and the final type of the lambda term to the user.
 * Also provides a way for the user to interact with it, e.g. looking at everything step-by-step.
 * In order to do this, this class uses {@link LatexCreator} tp create LaTeX-code from the
 * {@link TypeInfererInterface} and MathJax to render the LaTeX to the user.
 */
@CssImport("./styles/view/type-inference.css")
@JavaScript("./src/key-shortcuts.js")
@Route(value = TypeInferenceView.ROUTE + "/:term", layout = MainViewImpl.class)
public class TypeInferenceView extends VerticalLayout
        implements ControlPanelView, ComponentEventListener<AttachEvent>, LocaleChangeObserver, HasDynamicTitle,
        BeforeEnterObserver {
    /**
     * Route of this view.
     */
    public static final String ROUTE = "infer";
    private static final String CONTENT_ID = "content";
    private static final String ID = "type-inference-view";

    private final List<Integer> treeNumbers;

    private int currentStep = 0;


    private MathjaxUnification unification;
    private MathjaxProofTree tree;
    private transient LatexCreator lc;
    private final transient TypeInfererInterface typeInferer;
    private final Div content;
    private final ControlPanel controlPanel;
    private String term = "?";

    // used by Spring
    public TypeInferenceView() {
        this.typeInferer = null;
        this.content = null;
        this.controlPanel = null;
        this.treeNumbers = null;
    }

    /**
     * Initializes the component. When initialization is complete, the first step of the type
     * inference algorithm is shown to the user.
     *
     * @param typeInferer used to create LaTeX code from
     */
    public TypeInferenceView(TypeInfererInterface typeInferer) {
        setId(ID);
        addAttachListener(this);
        this.typeInferer = typeInferer;
        lc = new LatexCreator(typeInferer,
                error -> getTranslation("root." + error.toString().toLowerCase(Locale.ENGLISH)));
        content = new Div();
        content.setId(CONTENT_ID);
        controlPanel = new ControlPanel(this);
        treeNumbers = lc.getTreeNumbers();
        setContent();
        controlPanel.setEnabledFirstStep(false);
        controlPanel.setEnabledPreviousStep(false);

        Footer footer = new Footer(controlPanel);
        add(content, footer);
    }

    private void setContent() {
        unification = new MathjaxUnification(lc.getUnification());
        tree = new MathjaxProofTree(lc.getTree());
        content.add(unification, tree);
    }

    @Override
    public void shareButton() {
        UI.getCurrent().getPage().executeJs("return decodeURI(window.location.href)").then(url ->
                new ShareDialog(url.asString().replace(" ", "%20"), lc.getTree()).open()
        );
    }

    private void refreshElements() {
        unification.showStep(currentStep);
        tree.showStep(treeNumbers.get(currentStep));

        if (currentStep == 0) {
            controlPanel.setEnabledFirstStep(false);
            controlPanel.setEnabledPreviousStep(false);
        } else if (currentStep == unification.getStepCount() - 1) {
            controlPanel.setEnabledNextStep(false);
            controlPanel.setEnabledLastStep(false);
        } else {
            controlPanel.setEnabledNextStep(true);
            controlPanel.setEnabledLastStep(true);
            controlPanel.setEnabledFirstStep(true);
            controlPanel.setEnabledPreviousStep(true);
        }
    }

    @Override
    public void firstStepButton() {
        int treeEnd = treeNumbers.indexOf(tree.getStepCount() - 1);
        currentStep = currentStep > treeEnd && tree.getStepCount() > 0 ? treeEnd : 0;
        refreshElements();
    }

    @Override
    public void lastStepButton() {
        int treeEnd = treeNumbers.indexOf(tree.getStepCount() - 1);
        currentStep = currentStep < treeEnd ? treeEnd : unification.getStepCount() - 1;
        refreshElements();

    }

    @Override
    public void nextStepButton() {
        currentStep = currentStep < unification.getStepCount() - 1 ? currentStep + 1 : currentStep;
        refreshElements();
    }

    @Override
    public void previousStepButton() {
        currentStep = currentStep > 0 ? currentStep - 1 : currentStep;
        refreshElements();
    }

    @Override
    public void onComponentEvent(AttachEvent attachEvent) {
        currentStep = 0;
        refreshElements();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        if (typeInferer != null) {
            lc = new LatexCreator(typeInferer,
                    error -> getTranslation("root." + error.toString().toLowerCase(Locale.ENGLISH)));
            unification = new MathjaxUnification(lc.getUnification());
            content.removeAll();
            content.add(unification, tree);
            refreshElements();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getPath().matches(ROUTE + "/.*")) {
            term = URLDecoder.decode(
                    event.getRouteParameters().get("term")
                            .orElseThrow(IllegalStateException::new),
                    StandardCharsets.UTF_8);
        }
    }

    @Override
    public String getPageTitle() {
        if (typeInferer != null) {
            return typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm().toString();
        }
        return UI.getCurrent().getTranslation("root.typicalc") + " - " + term;
    }
}
