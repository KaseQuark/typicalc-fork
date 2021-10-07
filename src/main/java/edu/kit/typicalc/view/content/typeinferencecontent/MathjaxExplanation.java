package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

import java.util.List;

/**
 * Renders the constraints and unification (LaTeX, using MathJax) and allows step-by-
 * step revealing. Relies on MathjaxUnificationTS to interact with MathJax.
 */
@Tag("tc-explanation")
@JsModule("./src/mathjax-explanation.ts")
@CssImport("./styles/view/explanation.css")
public class MathjaxExplanation extends LitTemplate implements MathjaxAdapter {

    private final TypeInferenceView typeInferenceView;
    private final List<String> latex;

    // initialized by Vaadin
    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the provided texts.
     *
     * @param view the type inference view this explanation text is attached to
     * @param latex the latex texts for all steps
     * @param initialStep the step to show initially
     */
    public MathjaxExplanation(TypeInferenceView view, List<String> latex, int initialStep) {
        this.typeInferenceView = view;
        this.latex = latex;
        StringBuilder finalTex = new StringBuilder("<div>");
        for (int i = 0; i < latex.size(); i++) {
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

    @ClientCallable
    private void switchToStep(int unificationStepIdx) {
        typeInferenceView.setCurrentStep(unificationStepIdx);
    }
}

