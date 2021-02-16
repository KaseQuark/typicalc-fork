package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import edu.kit.typicalc.model.parser.ParseError;

/**
 * The notification being displayed on invalid input.
 */
@CssImport("./styles/view/error-notification.css")
@CssImport(value = "./styles/view/main/error-details.css", themeFor = "vaadin-details")
public class ErrorNotification extends Notification {

    private static final long serialVersionUID = 239587L;

    private static final String NOTIFICATION_ID = "errorNotification";
    private static final String NOTIFICATION_CONTENT_ID = "errorNotificationContent";
    private static final String ADDITIONAL_INFO_ID = "additionalInfo";
    private static final String ERROR_SUMMARY_ID = "errorSummary";

    /**
     * Creates a new ErrorNotification with a specific error.
     *
     * @param error the error
     */
    protected ErrorNotification(ParseError error) {
        VerticalLayout container = new VerticalLayout();
        container.setId(NOTIFICATION_CONTENT_ID);
        Button closeButton = new Button(getTranslation("root.close"), event -> this.close());

        container.add(buildErrorMessage(error), closeButton);
        addThemeVariants(NotificationVariant.LUMO_ERROR);
        add(container);
        setPosition(Position.MIDDLE);
        setId(NOTIFICATION_ID);
    }
    
    private Details buildErrorMessage(ParseError error) {
        VerticalLayout additionalInformation = new VerticalLayout();
        additionalInformation.setId(ADDITIONAL_INFO_ID);
        Paragraph summary = new Paragraph(getTranslation("root." + error.toString()));
        summary.setId(ERROR_SUMMARY_ID);
        Details errorMessage = new Details(summary, additionalInformation);
        
        if (error == ParseError.TOO_FEW_TOKENS) {
            additionalInformation.add(new Span(getTranslation("root.tooFewTokensHelp")));
        } else {
            additionalInformation.add(new Span(getTranslation("root.wrongCharacter") + error.getCause().getText()));
            additionalInformation.add(new Span(getTranslation("root.position") + error.getCause().getPos()));
        }
        
        return errorMessage;
    }
}
