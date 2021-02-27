package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "control-panel")
public class ControlPanelElement extends HorizontalLayoutElement {
    
    public void lastStep() {
        $(ButtonElement.class).get(4).click();
    }
    
    public void openShareDialog() {
        $(ButtonElement.class).first().click();
    }
}
