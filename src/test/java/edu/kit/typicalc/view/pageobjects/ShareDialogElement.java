package edu.kit.typicalc.view.pageobjects;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Vaadin TestBench element for {@link edu.kit.typicalc.view.content.typeinferencecontent.ShareDialog}.
 */
@Attribute(name = "id", value = "shareDialog")
public class ShareDialogElement extends DialogElement {

    /**
     * Get the permalink in the share dialog.
     *
     * @return the permalink
     */
    public String getPermalink() {
        return $(VerticalLayoutElement.class).first().$(TextFieldElement.class).first().getValue();
    }
}
