package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

/**
 * Renders a tree from LaTeX using MathJax and allows for step-by-step-revealing for
 * proof trees that use the bussproofs package. Relies on MathjaxProofTreeJS to interact
 * with MathJax.
 */
@Tag("tc-proof-tree")
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-proof-tree.ts")
public class MathjaxProofTree extends LitTemplate implements MathjaxAdapter {

    private int stepCount = -1;

    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the proof tree and cuts it into steps.
     * The latex String must consist of exactly one proof tree environment in order for
     * this element to work. In other cases the expected behaviour is undefined.
     *
     * @param latex the LaTeX-String to render with MathJax
     */
    public MathjaxProofTree(String latex) {
        content.add(latex);
    }

    @ClientCallable
    private void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public int getStepCount() {
        return this.stepCount;
    }

    @Override
    public void showStep(int n) {
        getElement().callJsFunction("showStep", n);
    }

    @Override
    public void scale(double newScaling) {

    }
}

