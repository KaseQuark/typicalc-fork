package edu.kit.typicalc.view.content.infocontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "home", layout = MainViewImpl.class)
@PageTitle("Typicalc")
@CssImport("./styles/views/typeinferenceview/type-inference-view-view.css")
@RouteAlias(value = "", layout = MainViewImpl.class)
public class StartPageView extends VerticalLayout {

    private TextField name;
    private Button sayHello;

    public StartPageView() {
        setId("start-page");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
