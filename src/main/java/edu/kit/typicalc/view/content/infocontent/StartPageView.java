package edu.kit.typicalc.view.content.infocontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxProofTree;
import edu.kit.typicalc.view.content.typeinferencecontent.MathjaxUnification;
import edu.kit.typicalc.view.main.MainViewImpl;
import edu.kit.typicalc.view.main.MathjaxDisplay;

@Route(value = "home", layout = MainViewImpl.class)
@PageTitle("Typicalc")
@RouteAlias(value = "", layout = MainViewImpl.class)
@JsModule("./src/mathjax-setup.js")
public class StartPageView extends VerticalLayout {

    private Button sayHello;

    public StartPageView() {
        // todo implement correctly
        setId("start-page");
        add(new MathjaxDisplay(getTranslation("abs-rule")));
        add(new MathjaxUnification("\\(constraint test \\vdash \\)"));
        add(new MathjaxProofTree(getTranslation("demo-tree")));
        sayHello = new Button("Say hello");
        add(sayHello);
    }

}
