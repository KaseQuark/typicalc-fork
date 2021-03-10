package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

/**
 * Renders a tree from LaTeX using MathJax and allows step-by-step revealing of
 * proof trees that use the bussproofs package. Relies on MathjaxProofTreeTS to interact
 * with MathJax.
 */
@Tag("tc-proof-tree")
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-proof-tree.ts")
public class MathjaxProofTree extends LitTemplate implements MathjaxAdapter {

    private int stepCount = -1;

    // initialized by Vaadin
    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the proof tree and cuts it into steps.
     * The latex code must consist of exactly one proof tree environment.
     * In other cases unexpected behaviour may occur.
     *
     * @param latex the LaTeX code to render with MathJax
     */
    public MathjaxProofTree(String latex) {
        content.add(latex);
        getElement().callJsFunction("requestTypeset");
    }

    /**
     * Used by mathjax-proof-tree.ts to set the calculated number of steps in the tree.
     * @param stepCount number of steps in tree
     */
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
}

