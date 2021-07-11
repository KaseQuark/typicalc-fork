package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Vaadin TestBench element for {@link edu.kit.typicalc.view.main.ExampleDialog}.
 */
@Attribute(name = "id", value = "exampleDialog")
public class ExampleDialogElement extends DialogElement {

    /**
     * Click on an example of the example dialog.
     * 
     * @param exampleTerm the example
     */
    public void insertExample(String exampleTerm) {
        $(VerticalLayoutElement.class).first().$(ButtonElement.class).id(exampleTerm).click();
    }
    
    public void insertAssumptions(String exampleAssumptions) {
        $(VerticalLayoutElement.class).first().$(ButtonElement.class).id(exampleAssumptions).click();
    }
}
