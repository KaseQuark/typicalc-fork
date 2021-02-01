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
import edu.kit.typicalc.view.main.MathjaxDisplay;

@Route(value = "home", layout = MainViewImpl.class)
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
//        disableControlPanel();
        createContent();

    }

    private void createContent() {
        String[] strings = new String[]{"$\\tau_0$", "$\\tau_1$", "$\\tau_2$", "$\\tau_3$", "$\\tau_4$",
                "$\\tau_5$", "$\\tau_6$", "$\\tau_7$", "$\\tau_8$", "$\\tau_9$", "$\\tau_{10}$", "$\\tau_{11}$",
                "$\\tau_{12}$", "$\\tau_{13}$", "$\\tau_{14}$"};
        content.add(new MathjaxDisplay(getTranslation("abs-rule")));
        unification = new MathjaxUnification(strings);
        tree = new MathjaxProofTree(getTranslation("demo-tree"));
        content.add(unification);
        content.add(tree);
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
    }

    private void disableControlPanel() {
        // todo disable everything
        controlPanel.setEnabledFirstStep(false);
        controlPanel.setEnabledLastStep(false);
        controlPanel.setEnabledNextStep(false);
        controlPanel.setEnabledPreviousStep(false);
        controlPanel.setEnabledShareButton(false);
    }

    @Override
    public void shareButton() {
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
