package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "header")
public class UpperBarElement extends HorizontalLayoutElement {

    /**
     * Opens the help dialog.
     */
    public void openHelpDialog() {
        $(ButtonElement.class).last().click();
    }
}
