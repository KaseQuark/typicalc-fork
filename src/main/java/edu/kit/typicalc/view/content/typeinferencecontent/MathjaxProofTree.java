package edu.kit.typicalc.view.content.typeinferencecontent;

import com.google.gson.Gson;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import edu.kit.typicalc.view.MathjaxAdapter;

import java.util.List;

/**
 * Renders a tree from LaTeX using MathJax and allows step-by-step revealing of
 * proof trees that use the bussproofs package. Relies on MathjaxProofTreeTS to interact
 * with MathJax.
 */
@Tag("tc-proof-tree")
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
     * @param extraData extra data to pass to the frontend script
     */
    public MathjaxProofTree(String latex, List<String> extraData) {
        // step definitions used for tooltips
        String latexCode = "\\[\\cssId{typicalc-prooftree}{" + latex + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-abs}{"
                + getTranslation("root.absLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-abs-let}{"
                + getTranslation("root.absLetLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-app}{"
                + getTranslation("root.appLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-const}{"
                + getTranslation("root.constLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-var}{"
                + getTranslation("root.varLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-var-let}{"
                + getTranslation("root.varLetLatex")
                + "}"
                + "}"
                + "\\class{typicalc-definition}{"
                + "\\cssId{typicalc-definition-let}{"
                + getTranslation("root.letLatex")
                + "}"
                + "}"
                + "\\]";
        content.add(latexCode);
        getElement().callJsFunction("requestTypeset", new Gson().toJson(extraData));
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

