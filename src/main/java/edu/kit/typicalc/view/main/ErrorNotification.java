package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * The notification being displayed on invalid input.
 */
@CssImport("./styles/view/error-notification.css")
public class ErrorNotification extends Notification {

    private static final long serialVersionUID = 239587L;

    private static final String NOTIFICATION_ID = "errorNotification";
    private static final String NOTIFICATION_CONTENT_ID = "errorNotificationContent";

    /**
     * Creates a new ErrorNotification with a specific error message.
     *
     * @param errorMessage the error message
     */
    protected ErrorNotification(String errorMessage) {
        VerticalLayout container = new VerticalLayout();
        container.setId(NOTIFICATION_CONTENT_ID);
        Span errorSpan = new Span(errorMessage);
        Button closeButton = new Button(getTranslation("root.close"), event -> this.close());

        container.add(errorSpan, closeButton);
        addThemeVariants(NotificationVariant.LUMO_ERROR);
        add(container);
        setPosition(Position.MIDDLE);
        setId(NOTIFICATION_ID);
    }
}
