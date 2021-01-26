package edu.kit.typicalc.view.content.type_inference_content;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "visualize", layout = MainViewImpl.class)
@PageTitle("TypeInferenceView")
@CssImport("./styles/views/typeinferenceview/type-inference-view-view.css")
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
            Notification.show("Hello, test " + name.getValue());
        });
    }

}
