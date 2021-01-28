package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

/**
 * Renders static LaTeX content using MathJax. Relies on MathjaxDisplayJS to interact
 * with MathJax.
 */
@Tag("tc-display")
@JsModule("./src/mathjax-display.ts")
public class MathjaxDisplay extends LitTemplate implements MathjaxAdapter {

    @Id("tc-content")
    private Div content;
    /**
     * Creates a new HTML element that renders the LaTeX code.
     *
     * @param latex the LaTeX string to render with MathJax
     */
    public MathjaxDisplay(String latex) {
        content.add(latex);
    }

    @Override
    public int getStepCount() {
        return 0;
    }

    @Override
    public void showStep(int n) {
        // do nothing
    }

    @Override
    public void scale(double newScaling) {
        // todo implement
    }
}

