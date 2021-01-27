package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.view.main.MainViewImpl;

@Route(value = "visualize", layout = MainViewImpl.class)
@PageTitle("TypeInferenceView")
public class TypeInferenceView extends HorizontalLayout implements HasUrlParameter<TypeInfererInterface> {

    private TextField name;
    private Button sayHello;

    public TypeInferenceView() {
        setId("type-inference-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello, test " + name.getValue());
        });
    }

    @Override
    public void setParameter(BeforeEvent event, TypeInfererInterface parameter) {
        // TODO Auto-generated method stub
    }

}
