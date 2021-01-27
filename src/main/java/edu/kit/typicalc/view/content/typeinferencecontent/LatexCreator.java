package edu.kit.typicalc.view.content.typeinferencecontent;


import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.TermVisitor;
import edu.kit.typicalc.model.type.TypeVisitor;

/**
 * Generates LaTeX-code from a TypeInfererInterface object. Two mostly independent pie-
 * ces of code are generated, one for the constraints/unification and one for the proof tree.
 * The LaTeX-code is created in a form, that it can be rendered by MathJax, so it must
 * only use packages and commands that MathJax supports. The LaTeX code is also usable
 * outside of MathJax, in a normal .tex document.
 */
public class LatexCreator implements StepVisitor, TermVisitor, TypeVisitor {

    private static final String LABEL_ABS = "\\LeftLabel{ABS}";
    private static final String LABEL_APP = "\\LeftLabel{APP}";
    private static final String LABEL_CONST = "\\LeftLabel{CONST}";
    private static final String LABEL_VAR = "\\LeftLabel{VAR}";
    private static final String LABEL_LET = "\\LeftLabel{LET}";

    private static final String UIC = "\\UnaryInfC";
    private static final String BIC = "\\BinaryInfC";
    private static final String AXC = "\\AxiomC";

    private static final String TREE_BEGIN = "\\begin{prooftree}";
    private static final String TREE_END = "\\end{prooftree}";




    private final StringBuilder tree;
    private final boolean stepLabels;

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer theTypeInfererInterface to create the LaTeX-code from
     */
    protected LatexCreator(TypeInfererInterface typeInferer) {
        this(typeInferer, true);
    }

    protected LatexCreator(TypeInfererInterface typeInferer, boolean stepLabels) {
        this.tree = new StringBuilder();
        this.stepLabels = stepLabels;

        typeInferer.getFirstInferenceStep().accept(this);
    }

    /**
     * @return the LaTeX-code for the proof tree
     */
    protected String getTree() {
        return null;
    }

    /**
     * @return the LaTeX-code for constraints nad unification
     */
    protected String getUnification() {
        return null;
    }

    /**
     * @return the packages needed for the LaTeX-code from getTree() and getUnification()to work
     */
    protected String getLatexPackages() {
        return null;
    }

    private String conclusionToLatex(Conclusion conclusion) {
        // todo implement
        return "";
    }

    private void generateConclusion(InferenceStep step, String label, String command) {
        StringBuilder conclusion = new StringBuilder();
        if (stepLabels) {
            conclusion.append(label);
            conclusion.append(System.lineSeparator());
        }
        conclusion.append(command);
        conclusion.append(conclusionToLatex(step.getConclusion()));
        conclusion.append(System.lineSeparator());
        tree.insert(0, conclusion);
    }

    @Override
    public void visit(AbsStepDefault absD) {
        generateConclusion(absD, LABEL_ABS, UIC);


    }

    @Override
    public void visit(AbsStepWithLet absL) {
        generateConclusion(absL, LABEL_ABS, UIC);

    }

    @Override
    public void visit(AppStepDefault appD) {
        generateConclusion(appD, LABEL_APP, UIC);

    }

    @Override
    public void visit(ConstStepDefault constD) {
        generateConclusion(constD, LABEL_CONST, UIC);

    }

    @Override
    public void visit(VarStepDefault varD) {
        generateConclusion(varD, LABEL_VAR, UIC);

    }

    @Override
    public void visit(VarStepWithLet varL) {
        generateConclusion(varL, LABEL_VAR, UIC);

    }

    @Override
    public void visit(LetStepDefault letD) {
        generateConclusion(letD, LABEL_LET, UIC);

    }
}
