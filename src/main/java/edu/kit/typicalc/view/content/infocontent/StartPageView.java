package edu.kit.typicalc.view.content.infocontent;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxProofTree;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxUnification;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "", layout = MainViewImpl.class)
@PageTitle("Typicalc")
@JsModule("./src/mathjax-setup.js")
public class StartPageView extends VerticalLayout implements ControlPanelView {

    private final Div content;
    private final ControlPanel controlPanel;
    MathjaxProofTree tree;
    MathjaxUnification unification;

    public StartPageView() {
        // todo implement correctly
        setId("start-page");
        setSizeFull();
        content = new Div();
        controlPanel = new ControlPanel(this, this);
        Scroller scroller = new Scroller(content);
        scroller.setSizeFull();
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        setAlignItems(Alignment.CENTER);
        add(scroller, controlPanel);
        disableControlPanel();
        createContent();

    }

    private void createContent() {
        content.add("TODO");
    }

    private void disableControlPanel() {
        // todo disable everything
        controlPanel.setEnabledFirstStep(false);
        controlPanel.setEnabledLastStep(false);
        controlPanel.setEnabledNextStep(false);
        controlPanel.setEnabledPreviousStep(false);
        controlPanel.setEnabledShareButton(false);
    }

    private int currentStep = 0;
    private void refreshElements(int currentStep) {
        unification.showStep(currentStep);
        tree.showStep(currentStep < tree.getStepCount() ? currentStep : tree.getStepCount() - 1);
    }

    @Override
    public void firstStepButton() {
        currentStep = currentStep > tree.getStepCount() ? tree.getStepCount() - 1 : 0;
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
}
