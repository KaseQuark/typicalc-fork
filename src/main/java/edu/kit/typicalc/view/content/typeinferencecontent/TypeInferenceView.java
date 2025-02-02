package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.StepAnnotator;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.ExplanationCreator;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreator;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorMode;
import edu.kit.typicalc.view.main.TypeInferenceRules;
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
@JavaScript("./src/key-shortcuts.ts")
@Route(value = TypeInferenceView.ROUTE + "/:term", layout = MainViewImpl.class)
public class TypeInferenceView extends VerticalLayout
        implements ControlPanelView, ComponentEventListener<AttachEvent>, LocaleChangeObserver, HasDynamicTitle,
        BeforeEnterObserver {
    /**
     * Route of this view.
     */
    public static final String ROUTE = "infer";
    private static final String ID = "type-inference-view";
    private static final String CONTENT_ID = "content";
    private static final String CONTENT_ID2 = "content2";
    private static final String CONTENT_ID3 = "content3";
    private static final String CONTENT_ID4 = "content4";
    private static final String RULES_ID = "rules";
    private static final String HOVER_BUTTON_CLASS = "hovering-button";
    private static final String RULES_BUTTON_ID = "rules-button";
    private static final String EXPLANATION_TEXT_BUTTON_ID = "explanation-text-button";
    private static final String H_LINE_ID = "horizontal-line";
    private static final String FOOTER_ID = "footer";

    private List<Integer> treeNumbers;

    private int currentStep = 0;


    private MathjaxUnification unification;
    private MathjaxProofTree tree = null;
    private MathjaxExplanation explanation = null;
    private transient LatexCreator lc;
    private transient LatexCreator lcUser;
    private final transient TypeInfererInterface typeInferer;
    private final List<String> stepAnnotations;
    private final Div content;
    private final ControlPanel controlPanel;
    private String term = "?";

    // used by Spring
    public TypeInferenceView() {
        this.typeInferer = null;
        this.stepAnnotations = null;
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
        this.typeInferer = typeInferer;
        var annotator = new StepAnnotator();
        typeInferer.getFirstInferenceStep().accept(annotator);
        this.stepAnnotations = annotator.getAnnotations();

        setId(ID);
        addAttachListener(this);
        content = new Div();
        content.setId(CONTENT_ID);
        // actual content is added by the immediately received LocaleChangeEvent
        controlPanel = new ControlPanel(this);
        controlPanel.setEnabledFirstStep(false);
        controlPanel.setEnabledPreviousStep(false);

        Hr line = new Hr();
        line.setId(H_LINE_ID);
        Footer footer = new Footer(line, controlPanel);
        footer.setId(FOOTER_ID);
        add(content, footer);
    }

    private boolean rulesVisible = false;
    private boolean explanationTextsVisible = true;

    private void setContent() {
        Button button = new Button(getTranslation("root.inferenceRules"));
        button.setClassName(HOVER_BUTTON_CLASS);
        button.setId(RULES_BUTTON_ID);
        Button button2 = new Button(getTranslation("root.explanationTexts"));
        button2.setClassName(HOVER_BUTTON_CLASS);
        button2.setId(EXPLANATION_TEXT_BUTTON_ID);

        Div horizontalContainer = new Div();
        horizontalContainer.setId(CONTENT_ID4);
        Div container = new Div();
        container.setId(CONTENT_ID2);
        unification = new MathjaxUnification(lc.getUnification());
        var explainer = new ExplanationCreator(typeInferer, getLocale());
        this.explanation = new MathjaxExplanation(
                this::setCurrentStep, explainer.getExplanationTexts(), this.currentStep);

        if (tree == null) {
            tree = new MathjaxProofTree(lc.getTree(), stepAnnotations);
        }

        Div treeDiv = new Div();
        treeDiv.setId(CONTENT_ID3);
        treeDiv.add(tree, button, button2);
        container.add(unification, treeDiv);
        horizontalContainer.add(container, explanation);

        TypeInferenceRules rules = new TypeInferenceRules();
        rules.setId(RULES_ID);
        rules.getElement().setVisible(rulesVisible);
        button.addClickListener(e -> {
            rulesVisible = !rulesVisible;
            rules.getElement().setVisible(rulesVisible);
        });
        button2.addClickListener(e -> {
            explanationTextsVisible = !explanationTextsVisible;
            explanation.getElement().setVisible(explanationTextsVisible);
        });

        content.add(rules, horizontalContainer);
    }

    @Override
    public void shareButton() {
        UI.getCurrent().getPage().executeJs("return decodeURI(window.location.href)").then(url ->
                new ShareDialog(
                        url.asString().replace(" ", "%20"),
                        lcUser.getTree(),
                        lcUser.getUnification()[currentStep]).open()
        );
    }

    private void refreshElements() {
        unification.showStep(currentStep);
        tree.showStep(treeNumbers.get(currentStep));
        explanation.showStep(currentStep);

        if (currentStep == 0) {
            controlPanel.setEnabledFirstStep(false);
            controlPanel.setEnabledPreviousStep(false);
            controlPanel.setEnabledNextStep(true);
            controlPanel.setEnabledLastStep(true);
        } else if (currentStep == unification.getStepCount() - 1) {
            controlPanel.setEnabledFirstStep(true);
            controlPanel.setEnabledPreviousStep(true);
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

    public void setCurrentStep(int step) {
        currentStep = step;
        refreshElements();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        if (typeInferer != null) {
            content.removeAll();
            lc = new LatexCreator(typeInferer,
                    error -> getTranslation("root." + error.toString().toLowerCase(Locale.ENGLISH)),
                    LatexCreatorMode.MATHJAX);
            lcUser = new LatexCreator(typeInferer,
                    error -> getTranslation("root." + error.toString().toLowerCase(Locale.ENGLISH)),
                    LatexCreatorMode.NORMAL);
            treeNumbers = lc.getTreeNumbers();
            setContent();
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
