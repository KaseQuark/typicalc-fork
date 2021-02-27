package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Vaadin TestBench element for {@link edu.kit.typicalc.view.main.TypeAssumptionsArea}.
 */
@Attribute(name = "id", value = "assLayout")
public class TypeAssumptionsAreaElement extends VerticalLayoutElement {

    /**
     * Adds a new TypeAssumption.
     */
    public void addTypeAssumption() {
        $(HorizontalLayoutElement.class).id("assButtons").$(ButtonElement.class).first().click();
    }
    
    public TypeAssumptionFieldElement getLastTypeAssumption() {
        return $(VerticalLayoutElement.class).id("assContainer")
                .$(TypeAssumptionFieldElement.class).last();
    }
    
    public void closeDialog() {
        $(HorizontalLayoutElement.class).first().$(ButtonElement.class).last().click();
    }
}
