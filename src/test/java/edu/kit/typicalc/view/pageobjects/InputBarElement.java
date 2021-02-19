package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "inputBar") // maybe make id constants public to use the here
public class InputBarElement extends HorizontalLayoutElement {

    public void typeInfer() {
        $(ButtonElement.class).id("inferButton").click();
    }

    public void setCurrentValue(String value) {
        $(TextFieldElement.class).id("inputField").setValue(value);
    }

    public String getCurrentValue() {
        return $(TextFieldElement.class).id("inputField").getValue();
    }

    public void openExampleDialog() {
        $(ButtonElement.class).id("exampleButton").click();
    }
}
