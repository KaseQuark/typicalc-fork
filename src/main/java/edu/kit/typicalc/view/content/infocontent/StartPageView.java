package edu.kit.typicalc.view.content.infocontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.kit.typicalc.view.main.MainViewImpl;
import edu.kit.typicalc.view.main.MathjaxDisplay;

@Route(value = "home", layout = MainViewImpl.class)
@PageTitle("Typicalc")
@RouteAlias(value = "", layout = MainViewImpl.class)
public class StartPageView extends VerticalLayout {

    private TextArea name;
    private Button sayHello;

    public StartPageView() {
        // todo implement correctly
        setId("start-page");
        name = new TextArea("translation test");
        name.setValue(getTranslation("abs-rule"));
        name.setWidthFull();
        add(new MathjaxDisplay());
        sayHello = new Button("Say hello");
        add(name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
