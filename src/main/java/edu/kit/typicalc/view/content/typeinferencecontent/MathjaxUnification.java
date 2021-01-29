package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import edu.kit.typicalc.view.MathjaxAdapter;

import java.util.Arrays;

/**
 * Renders the constraints and unification from LaTeX using MathJax and allows step-by-
 * step-revealing capabilities. Relies on MathjaxUnificationJS to interact with MathJax.
 */
@Tag("tc-unification")
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-unification.ts")
public class MathjaxUnification extends LitTemplate implements MathjaxAdapter {

    private int stepCount = -1;
//
//    @Id("tc-content")
//    private Div content;

    /**
     * Creates a new HTML element that renders the constraints and unification and
     * calculates the steps.
     * @param latex the LaTeX-String[] to render with MathJax
     */
    public MathjaxUnification(String[] latex) {
        Notification.show(Arrays.toString(latex));
        for (String s : latex) {
            getElement().callJsFunction("setTex", s);
        }
        showStep(1);
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
        // todo implement
    }
}

