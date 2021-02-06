package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorNotification extends Notification {

    private static final long serialVersionUID = 239587L;

    private static final String NOTIFICATION_ID = "errorNotification";

    protected ErrorNotification(String errorMessage) {
        VerticalLayout container = new VerticalLayout();
        Span errorSpan = new Span(errorMessage);
        Button closeButton = new Button(getTranslation("root.close"), event -> this.close());

        container.add(errorSpan, closeButton);
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        addThemeVariants(NotificationVariant.LUMO_ERROR);
        add(container);
        setPosition(Position.MIDDLE);
        setId(NOTIFICATION_ID);
    }
}
