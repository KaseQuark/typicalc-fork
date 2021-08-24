package edu.kit.typicalc.view.content.typeinferencecontent;

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
@JsModule("./src/mathjax-adapter.ts")
@JsModule("./src/mathjax-explanation.ts")
@CssImport("./styles/view/explanation.css")
public class MathjaxExplanation extends LitTemplate implements MathjaxAdapter {

    private final List<String> latex;

    // initialized by Vaadin
    @Id("tc-content")
    private Div content;

    /**
     * Creates a new HTML element that renders the provided texts.
     *
     * @param latex the latex texts for all steps
     */
    public MathjaxExplanation(List<String> latex) {
        this.latex = latex;
        StringBuilder finalTex = new StringBuilder("<div>");
        for (int i = 0; i < latex.size(); i++) {
            finalTex.append("<p class='tc-text' id='tc-text-").append(i).append("'>");
            finalTex.append(latex.get(i));
            finalTex.append("</p>");
        }
        var finalString = finalTex.append("</div>").toString();
        content.add(new Html(finalString));
        showStep(0);
    }

    @Override
    public int getStepCount() {
        return this.latex.size();
    }

    @Override
    public void showStep(int n) {
        getElement().callJsFunction("showStep", n);
    }
}

