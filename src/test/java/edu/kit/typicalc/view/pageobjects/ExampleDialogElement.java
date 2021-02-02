package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

@Attribute(name = "id", value = "exampleDialog")
public class ExampleDialogElement extends DialogElement {

    public void insertExample(String example) {
        $(VerticalLayoutElement.class).first().$(ButtonElement.class).id(example).click();
    }
}
