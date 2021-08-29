package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

/**
 * Renders the constraints and unification (LaTeX, using MathJax) and allows step-by-
 * step revealing. Relies on MathjaxUnificationTS to interact with MathJax.
 */
@Tag("tc-unification")
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-unification.ts")
@CssImport("./styles/view/unification.css")
public class MathjaxUnification extends LitTemplate implements MathjaxAdapter {

    private final String[] latex;

    // initialized by Vaadin
    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the constraints and unification and
     * calculates the steps.
     *
     * @param latex the LaTeX-String[] to render with MathJax
     */
    public MathjaxUnification(String[] latex) {
        this.latex = latex;
        showStep(0);
    }

    @Override
    public int getStepCount() {
        return this.latex.length;
    }

    @Override
    public void showStep(int n) {
        getElement().callJsFunction("showLatex", latex[n]);
    }
}

