package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "visualize", layout = MainViewImpl.class)
@PageTitle("TypeInferenceView")
@PreserveOnRefresh
public class TypeInferenceView extends VerticalLayout
        implements ControlPanelView, ComponentEventListener<AttachEvent> {

    private int currentStep = 0;

    private MathjaxUnification unification;
    private MathjaxProofTree tree;
    private final transient TypeInfererInterface typeInferer;
    private final Div content;
    private final ControlPanel controlPanel;

    public TypeInferenceView(TypeInfererInterface typeInferer) {
        this.typeInferer = typeInferer;
        setId("type-inference-view");
        setSizeFull();
        addAttachListener(this);
        content = new Div();
        controlPanel = new ControlPanel(this);
        Scroller scroller = new Scroller(content);
        scroller.setSizeFull();
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        setAlignItems(Alignment.CENTER);
        add(scroller, controlPanel);
        setContent();
    }

    private void setContent() {
        // todo implement correctly
        LatexCreator lc = new LatexCreator(typeInferer);
        unification = new MathjaxUnification(lc.getUnification());
        tree = new MathjaxProofTree(lc.getTree());
        content.add(unification, tree);
    }

    @Override
    public void shareButton() {
        // todo implement
    }

    private void refreshElements(int currentStep) {
        unification.showStep(currentStep);
        tree.showStep(currentStep < tree.getStepCount() ? currentStep : tree.getStepCount() - 1);
    }

    @Override
    public void firstStepButton() {
        currentStep = currentStep > tree.getStepCount()  && tree.getStepCount() > 0 ? tree.getStepCount() - 1 : 0;
        refreshElements(currentStep);
    }

    @Override
    public void lastStepButton() {
        currentStep = currentStep < tree.getStepCount() - 1 ? tree.getStepCount() - 1 : unification.getStepCount() - 1;
        refreshElements(currentStep);
    }

    @Override
    public void nextStepButton() {
        currentStep = currentStep < unification.getStepCount() - 1 ? currentStep + 1 : currentStep;
        refreshElements(currentStep);
    }

    @Override
    public void previousStepButton() {
        currentStep = currentStep > 0 ? currentStep - 1 : currentStep;
        refreshElements(currentStep);
    }

    @Override
    public void onComponentEvent(AttachEvent attachEvent) {
        currentStep = 0;
        refreshElements(currentStep);
    }
}
