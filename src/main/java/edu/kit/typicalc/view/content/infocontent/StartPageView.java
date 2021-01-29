package edu.kit.typicalc.view.content.infocontent;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxProofTree;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxUnification;
import edu.kit.typicalc.view.main.MainViewImpl;
import edu.kit.typicalc.view.main.MathjaxDisplay;

@Route(value = "home", layout = MainViewImpl.class)
@PageTitle("Typicalc")
@RouteAlias(value = "", layout = MainViewImpl.class)
@JsModule("./src/mathjax-setup.js")
public class StartPageView extends VerticalLayout implements ControlPanelView {

    private final Div content;
    private final ControlPanel controlPanel;
    MathjaxProofTree mjxPT;

    public StartPageView() {
        // todo implement correctly
        setId("start-page");
        setSizeFull();
        content = new Div();
        controlPanel = new ControlPanel(this);
        Scroller scroller = new Scroller(content);
        scroller.setSizeFull();
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        setAlignItems(Alignment.CENTER);
        add(scroller, controlPanel);
        disableControlPanel();
        createContent();

    }

    private void createContent() {
        content.add(new MathjaxDisplay(getTranslation("abs-rule")));
        content.add(new MathjaxUnification("\\(conswwwwwwWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"
                + "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"
                + "WWWWWWWWWWWWWWWWWWWWWtraint test \\vdash \\)"));
        mjxPT = new MathjaxProofTree(getTranslation("demo-tree"));
        content.add(mjxPT);
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
    }

    private void disableControlPanel() {
        // todo disable everything
//        controlPanel.setEnabledFirstStep(false);
//        controlPanel.setEnabledLastStep(false);
        controlPanel.setEnabledNextStep(false);
        controlPanel.setEnabledPreviousStep(false);
        controlPanel.setEnabledShareButton(false);
    }

    @Override
    public void shareButton() {
    }

    @Override
    public void firstStepButton() {
        mjxPT.showStep(0);
    }

    @Override
    public void lastStepButton() {
        mjxPT.showStep(5);
    }

    @Override
    public void nextStepButton() {
    }

    @Override
    public void previousStepButton() {
    }
}
