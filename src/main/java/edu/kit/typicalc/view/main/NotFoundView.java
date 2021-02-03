package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ParentLayout;

@ParentLayout(MainViewImpl.class)
public class NotFoundView extends VerticalLayout {


    public NotFoundView(BeforeEnterEvent event) {
        Label error = new Label(event.getLocation().getPath());
        add(error);
        setAlignItems(Alignment.CENTER);
    }
}