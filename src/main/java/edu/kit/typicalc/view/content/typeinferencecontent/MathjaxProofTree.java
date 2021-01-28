package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

@Tag("tc-proof-tree")
@JsModule("./src/mathjax-proof-tree.ts")
public class MathjaxProofTree extends LitTemplate {

    @Id("tc-content")
    private Div content;
    /**
     * todo Creates the hello world template.
     */
    public MathjaxProofTree() {
        content.add(getTranslation("demo-tree"));
    }
}

