package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Vaadin TestBench element for {@link edu.kit.typicalc.view.main.InputBar}.
 */
@Attribute(name = "id", value = "inputBar")
public class InputBarElement extends HorizontalLayoutElement {

    /**
     * Click the type infer button.
     */
    public void typeInfer() {
        $(ButtonElement.class).id("inferButton").click();
    }

    /**
     * Set the current value of the inputField.
     * 
     * @param value the value
     */
    public void setCurrentValue(String value) {
        $(TextFieldElement.class).id("inputField").setValue(value);
    }

    /**
     * Get the current value of the inputField.
     * 
     * @return the current value
     */
    public String getCurrentValue() {
        return $(TextFieldElement.class).id("inputField").getValue();
    }

    /**
     * Open the example dialog.
     */
    public void openExampleDialog() {
        $(ButtonElement.class).id("exampleButton").click();
    }
    
    /**
     * Opens the type assumptions area.
     */
    public void openTypeAssumptionsArea() {
        $(ButtonElement.class).first().click();
    }
    
}
