package edu.kit.typicalc.views.typeinferenceview;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "visualize", layout = MainView.class)
@PageTitle("TypeInferenceView")
@CssImport("./styles/views/typeinferenceview/type-inference-view-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class TypeInferenceViewView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public TypeInferenceViewView() {
        setId("type-inference-view-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
