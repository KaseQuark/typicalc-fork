package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import edu.kit.typicalc.view.MathjaxAdapter;

/**
 * Renders the constraints and unification from LaTeX using MathJax and allows step-by-
 * step-revealing capabilities. Relies on MathjaxUnificationJS to interact with MathJax.
 */
@Tag("tc-unification")
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-unification.ts")
public class MathjaxUnification extends LitTemplate implements MathjaxAdapter {

    private final String[] latex;

    /**
     * Creates a new HTML element that renders the constraints and unification and
     * calculates the steps.
     * @param latex the LaTeX-String[] to render with MathJax
     */
    public MathjaxUnification(String[] latex) {
        this.latex = latex;
        for (String s : latex) {
            getElement().callJsFunction("setTex", s);
        }
        showStep(0);
    }

    @Override
    public int getStepCount() {
        return this.latex.length;
    }

    @Override
    public void showStep(int n) {
        getElement().callJsFunction("showStep", n);
    }

    @Override
    public void scale(double newScaling) {
        // todo implement
    }
}

