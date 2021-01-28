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

    public StartPageView() {
        // todo implement correctly
        setId("start-page");
        setSizeFull();
        content = new Div();
        controlPanel = new ControlPanel(this);
        Scroller scroller = new Scroller(content);
        scroller.setSizeFull();
        scroller.setScrollDirection(Scroller.ScrollDirection.BOTH);
        content.add(new MathjaxDisplay(getTranslation("abs-rule")));
        content.add(new MathjaxUnification("\\(conswwwwwwWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWtraint test \\vdash \\)"));
        MathjaxProofTree mjxPT = new MathjaxProofTree(getTranslation("demo-tree"));
        mjxPT.showStep(10);
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        content.add(new MathjaxProofTree(getTranslation("demo-tree")));
        setAlignItems(Alignment.CENTER);
        add(scroller, controlPanel);
    }

    @Override
    public void shareButton() {

    }

    @Override
    public void firstStepButton() {

    }

    @Override
    public void lastStepButton() {

    }

    @Override
    public void nextStepButton() {

    }

    @Override
    public void previousStepButton() {

    }
}
