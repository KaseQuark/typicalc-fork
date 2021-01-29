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
        if (typeInferer == null) {
            // todo throw exception
            unification = new MathjaxUnification("\\vdash test");
            tree = new MathjaxProofTree(getTranslation("demo-tree"));
        } else {
                LatexCreator lc = new LatexCreator(typeInferer);
                unification = new MathjaxUnification(lc.getUnification());
                tree = new MathjaxProofTree(lc.getTree());
        }
        add(unification);
        add(new Scroller(tree));
    }

    @Override
    public void shareButton() {
        // todo implement
    }

    //todo implement correctly
    @Override
    public void firstStepButton() {
        currentStep = 0;
        unification.showStep(currentStep);
        tree.showStep(currentStep);
    }

    @Override
    public void lastStepButton() {
        currentStep = unification.getStepCount() - 1;
        unification.showStep(currentStep);
        tree.showStep(tree.getStepCount() - 1);
    }

    @Override
    public void nextStepButton() {
        currentStep++;
        unification.showStep(currentStep);
        tree.showStep(currentStep);
    }

    @Override
    public void previousStepButton() {
        currentStep--;
        unification.showStep(currentStep);
        tree.showStep(currentStep);
    }
}
