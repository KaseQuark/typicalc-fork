package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "control-panel")
public class ControlPanelElement extends HorizontalLayoutElement {

    public void firstStep() {
        $(ButtonElement.class).get(1).click();
    }

    public void previousStep() {
        $(ButtonElement.class).get(2).click();
    }

    public void nextStep() {
        $(ButtonElement.class).get(3).click();
    }

    public void lastStep() {
        $(ButtonElement.class).get(4).click();
    }
    
    public void openShareDialog() {
        $(ButtonElement.class).first().click();
    }
}
