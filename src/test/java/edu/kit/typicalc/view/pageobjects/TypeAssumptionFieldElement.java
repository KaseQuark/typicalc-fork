package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "typeAssumptionField")
public class TypeAssumptionFieldElement extends HorizontalLayoutElement {

    public void setVariable(String variable) {
        $(TextFieldElement.class).first().setValue(variable);
    }
    
    public void setType(String type) {
        $(TextFieldElement.class).last().setValue(type);
    }
}
