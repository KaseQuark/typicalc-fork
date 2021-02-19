package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Vaadin TestBench element for {@link edu.kit.typicalc.view.main.ErrorNotification}.
 */
@Attribute(name = "id", value = "errorNotification")
public class ErrorNotificationElement extends NotificationElement {

    /**
     * Click to close button of the ErrorNotification
     */
    public void close() {
        $(ButtonElement.class).first().click();
    }
    
    /**
     * Get the errorMessage of the ErrorNotification
     * 
     * @return the errorMessage within a ParagraphElement
     */
    public ParagraphElement getErrorParagraph() {
        return $(VerticalLayoutElement.class).id("errorNotificationContent").$(ParagraphElement.class)
                .id("errorSummary");
    }
}
