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
     * Set the current value of the termInputField.
     * 
     * @param value the value
     */
    public void setTerm(String value) {
        $(TextFieldElement.class).id("term-input-field").setValue(value);
    }

    /**
     * Get the current value of the termInputField.
     * 
     * @return the current value
     */
    public String getTerm() {
        return $(TextFieldElement.class).id("term-input-field").getValue();
    }
    
    /**
     * Set the current value of the assumptionInputField.
     * 
     * @param assumptions the value
     */
    public void setTypeAssumptions(String assumptions) {
        $(TextFieldElement.class).id("ass-input-field").setValue(assumptions);
    }
    
    /**
     * Get the current value of the assumptionInputField.
     * 
     * @return the current value
     */
    public String getTypeAssumptions() {
        return $(TextFieldElement.class).id("ass-input-field").getValue();
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
        $(ButtonElement.class).id("assButton").click();
    }
    
    /**
     * Opens the info dialog.
     */
    public void openInfoDialog() {
        $(ButtonElement.class).first().click();
    }
    
}
