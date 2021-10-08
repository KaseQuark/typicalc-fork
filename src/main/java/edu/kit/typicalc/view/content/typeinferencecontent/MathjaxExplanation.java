package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

import java.util.List;
import java.util.function.Consumer;

/**
 * Renders the constraints and unification (LaTeX, using MathJax) and allows step-by-
 * step revealing.
 */
@Tag("tc-explanation")
@JsModule("./src/mathjax-explanation.ts")
public class MathjaxExplanation extends LitTemplate implements MathjaxAdapter {

    private final transient Consumer<Integer> stepSwitchCallback;
    private final List<String> latex;

    // initialized by Vaadin
    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the provided texts.
     *
     * @param stepSwitchCallback the callback used when the user clicks on one of the texts
     * @param latex the latex texts for all steps
     * @param initialStep the step to show initially
     */
    public MathjaxExplanation(Consumer<Integer> stepSwitchCallback, List<String> latex, int initialStep) {
        this.stepSwitchCallback = stepSwitchCallback;
        this.latex = latex;
        StringBuilder finalTex = new StringBuilder("<div>");
        for (int i = 0; i < latex.size(); i++) {
            // this class/id is used by the frontend script to locate texts
            finalTex.append(String.format("<p class='tc-text' id='tc-text-%d'>", i));
            finalTex.append(latex.get(i));
            finalTex.append("</p>");
        }
        var finalString = finalTex.append("</div>").toString();
        content.add(new Html(finalString));
        showStep(initialStep);
    }

    @Override
    public int getStepCount() {
        return this.latex.size();
    }

    @Override
    public void showStep(int n) {
        getElement().callJsFunction("showStep", n);
    }

    /**
     * Called when the user clicks on the explanation text of a specific step.
     */
    @ClientCallable
    private void switchToStep(int unificationStepIdx) {
        stepSwitchCallback.accept(unificationStepIdx);
    }
}

