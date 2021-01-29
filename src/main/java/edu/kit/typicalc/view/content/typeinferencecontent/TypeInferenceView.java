package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "visualize", layout = MainViewImpl.class)
@PageTitle("TypeInferenceView")
public class TypeInferenceView extends HorizontalLayout
        implements ControlPanelView, HasUrlParameter<TypeInfererInterface> {

    private int currentStep;

    private MathjaxUnification unification;
    private MathjaxProofTree tree;

    public TypeInferenceView() {
        setId("type-inference-view");
        add(new ControlPanel(this));
    }

    @Override
    public void setParameter(BeforeEvent event, TypeInfererInterface typeInferer) {
        buildView(typeInferer);
    }

    private void buildView(TypeInfererInterface typeInferer) {
        // todo implement correctly
        LatexCreator lc = new LatexCreator(typeInferer);
        unification = new MathjaxUnification(lc.getUnification());
        tree = new MathjaxProofTree(lc.getTree());
        add(unification);
        add(new Scroller(tree));
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
        currentStep = currentStep > tree.getStepCount() ? tree.getStepCount() : 0;
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
