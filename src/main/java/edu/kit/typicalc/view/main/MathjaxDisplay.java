package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

@Tag("tc-display")
@JsModule("./src/mathjax-display.ts")
public class MathjaxDisplay extends LitTemplate {

    @Id("tc-content")
    private Div content;
    /**
     * Creates the hello world template.
     */
    public MathjaxDisplay() {
        content.add(getTranslation("abs-rule"));
    }
}

