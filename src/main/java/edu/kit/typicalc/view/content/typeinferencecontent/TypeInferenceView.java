package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreator;

import java.util.List;

@PageTitle("TypeInferenceView")
@CssImport("./styles/view/type-inference.css")
public class TypeInferenceView extends VerticalLayout
        implements ControlPanelView, ComponentEventListener<AttachEvent> {
    /**
     * Route of this view.
     */
    public static final String ROUTE = "infer";
    private static final String SCROLLER_ID = "scroller";
    private static final String CONTENT_ID = "content";
    private static final String ID = "type-inference-view";

    private final List<Integer> treeNumbers;

    private int currentStep = 0;


    private MathjaxUnification unification;
    private MathjaxProofTree tree;
    private final transient LatexCreator lc;
    private final Div content;

    public TypeInferenceView(TypeInfererInterface typeInferer) {
        setId(ID);
        setSizeFull();
        addAttachListener(this);
        lc = new LatexCreator(typeInferer);
        content = new Div();
        content.setId(CONTENT_ID);
        ControlPanel controlPanel = new ControlPanel(this, this);
        Scroller scroller = new Scroller(content);
        scroller.setId(SCROLLER_ID);
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        setAlignItems(Alignment.CENTER);
        add(scroller, controlPanel);
        treeNumbers = lc.getTreeNumbers();
        setContent();
    }

    private void setContent() {
        // todo maybe swap
        unification = new MathjaxUnification(lc.getUnification());
        tree = new MathjaxProofTree(lc.getTree());
        content.add(unification, tree);
    }

    @Override
    public void shareButton() {
        UI.getCurrent().getPage().executeJs("return decodeURI(window.location.href)").then(url ->
            new ShareDialog(url.asString(), lc.getLatexPackages(), lc.getTree()).open()
        );
    }

    private void refreshElements() {
        unification.showStep(currentStep);
        tree.showStep(treeNumbers.get(currentStep));
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
}
