package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.ParentLayout;

/**
 * This is the view used when an unknown URL is requested by the user.
 */
@ParentLayout(MainViewImpl.class)
public class NotFoundView extends VerticalLayout {
    public NotFoundView() {
        H1 error404 = new H1("404 - Not found");
        H2 suggestion = new H2("Try \"/infer/<term>\" or type your favourite term into the input field");
        add(error404, suggestion); // todo make beautiful
        setAlignItems(Alignment.CENTER);
    }
}