package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "errorNotification")
public class ErrorNotificationElement extends NotificationElement {

    public void close() {
        $(ButtonElement.class).first().click();
    }
    
    public SpanElement getErrorSpan() {
        return $(VerticalLayoutElement.class).first().$(SpanElement.class).first();
    }
}
