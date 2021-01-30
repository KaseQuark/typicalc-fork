package edu.kit.typicalc.view.content.typeinferencecontent;


import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;

import java.util.Map;

/**
 * Generates LaTeX-code from a TypeInfererInterface object. Two mostly independent pie-
 * ces of code are generated, one for the constraints/unification and one for the proof tree.
 * The LaTeX-code is created in a form, that it can be rendered by MathJax, so it must
 * only use packages and commands that MathJax supports. The LaTeX code is also usable
 * outside of MathJax, in a normal .tex document.
 */
public class LatexCreator implements StepVisitor, TermVisitor, TypeVisitor {

    private static final String CONST = "Const";

    private static final String NEW_LINE = System.lineSeparator();
    private static final String SPACE = " ";
    private static final String EQUAL_SIGN = "=";
    private static final String DOLLAR_SIGN = "$";
    private static final String CURLY_LEFT = "{";
    private static final String CURLY_RIGHT = "}";
    private static final String UNDERSCORE = "_";
    private static final String PAREN_LEFT = "(";
    private static final String PAREN_RIGHT = ")";

    private static final String LABEL_ABS = "\\LeftLabel{ABS}";
    private static final String LABEL_APP = "\\LeftLabel{APP}";
    private static final String LABEL_CONST = "\\LeftLabel{CONST}";
    private static final String LABEL_VAR = "\\LeftLabel{VAR}";
    private static final String LABEL_LET = "\\LeftLabel{LET}";

    private static final String UIC = "\\UnaryInfC";
    private static final String BIC = "\\BinaryInfC";
    private static final String AXC = "\\AxiomC";

    private static final String TREE_VARIABLE = "\\alpha";
    private static final String GENERATED_ASSUMPTION_VARIABLE = "\\beta";
    private static final String USER_VARIABLE = "\\tau";

    private static final String TEXTTT = "\\texttt";
    private static final String RIGHT_ARROW = "\\rightarrow";
    private static final String INSTANTIATE_SIGN = "\\succeq";
    private static final String LATEX_IN = "\\in";
    private static final String TREE_BEGIN = "\\begin{prooftree}";
    private static final String TREE_END = "\\end{prooftree}";




    private final StringBuilder tree;
    private final boolean stepLabels;

    /**
     * Needed for visitor methods
     */
    private String visitorBuffer = "";

    /**
     * Needed for visitor methods
     */
    private boolean needsParentheses = false;

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

//        typeInferer.getFirstInferenceStep().accept(this);
    }

    /**
     * @return the LaTeX-code for the proof tree
     */
    protected String getTree() {
        return "the $\\LaTeX$ inference tree should be here ";
    } // todo implement

    /**
     * @return the LaTeX-code for constraints nad unification
     */
    protected String[] getUnification() {
        return new String[]{"$\\tau_0$", "$\\tau_1$", "$\\tau_2$", "$\\tau_3$", "$\\tau_4$",
                "$\\tau_5$", "$\\tau_6$", "$\\tau_7$", "$\\tau_8$", "$\\tau_9$", "$\\tau_{10}$", "$\\tau_{11}$",
                "$\\tau_{12}$", "$\\tau_{13}$", "$\\tau_{14}$"};
    } // todo implement

    /**
     * @return the packages needed for the LaTeX-code from getTree() and getUnification()to work
     */
    protected String getLatexPackages() {
        return "the packages should be here";
    } // todo implement

    private String typeAssumptionsToLatex(Map<VarTerm, TypeAbstraction> typeAssumptions) {

        return "{some other text}";
    }

    private String conclusionToLatex(Conclusion conclusion) {
        String typeAssumptions = typeAssumptionsToLatex(conclusion.getTypeAssumptions());
        return "{$some text$}";
    }

    private StringBuilder generateConclusion(InferenceStep step, String label, String command) {
        StringBuilder conclusion = new StringBuilder();
        if (stepLabels) {
            conclusion.append(label).append(NEW_LINE);
        }
        conclusion.append(command)
                .append(conclusionToLatex(step.getConclusion()))
                .append(NEW_LINE);
        return conclusion;
    }

    private String generateConstraint(InferenceStep step) {
        step.getConstraint().getFirstType().accept(this);
        String firstType = visitorBuffer;
        step.getConstraint().getSecondType().accept(this);
        String secondType = visitorBuffer;
        return firstType + SPACE + EQUAL_SIGN + SPACE + secondType;
    }

    private String generateVarStepPremise(VarStep var) {
        String assumptions = typeAssumptionsToLatex(var.getConclusion().getTypeAssumptions());
        var.getConclusion().getLambdaTerm().accept(this);
        String term = visitorBuffer;
        String type = generateTypeAbstraction(var.getTypeAbsInPremise());
        return AXC + CURLY_LEFT + DOLLAR_SIGN + PAREN_LEFT + assumptions + PAREN_RIGHT + PAREN_LEFT + term
                + PAREN_RIGHT + EQUAL_SIGN + type + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
    }

    private String generateTypeAbstraction(TypeAbstraction abs) {
        // todo implement
        return "";
    }


    // todo use generateConstraint
    @Override
    public void visit(AbsStepDefault absD) {
        tree.insert(0, generateConclusion(absD, LABEL_ABS, UIC));
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        tree.insert(0, generateConclusion(absL, LABEL_ABS, UIC));
        absL.getPremise().accept(this);
    }

    @Override
    public void visit(AppStepDefault appD) {
        tree.insert(0, generateConclusion(appD, LABEL_APP, BIC));
        appD.getPremise2().accept(this);
        appD.getPremise1().accept(this);
    }

    @Override
    public void visit(ConstStepDefault constD) {
        tree.insert(0, generateConclusion(constD, LABEL_CONST, UIC));
        constD.getConclusion().getLambdaTerm().accept(this);
        String step = AXC + CURLY_LEFT + DOLLAR_SIGN + visitorBuffer + SPACE + LATEX_IN + SPACE + CONST
                + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, step);
    }

    @Override
    public void visit(VarStepDefault varD) {
        tree.insert(0, generateConclusion(varD, LABEL_VAR, UIC));
        tree.insert(0, generateVarStepPremise(varD));
    }

    @Override
    public void visit(VarStepWithLet varL) {
        tree.insert(0, generateConclusion(varL, LABEL_VAR, BIC));
        String typeAbstraction = generateTypeAbstraction(varL.getTypeAbsInPremise());
        varL.getInstantiatedTypeAbs().accept(this);
        String instantiatedType = visitorBuffer;
        String premiseRight = AXC + CURLY_LEFT + DOLLAR_SIGN + typeAbstraction + INSTANTIATE_SIGN + instantiatedType
                + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, premiseRight);
        String premiseLeft = generateVarStepPremise(varL);
        tree.insert(0, premiseLeft);
    }

    @Override
    public void visit(LetStepDefault letD) {
        tree.insert(0, generateConclusion(letD, LABEL_LET, BIC));
        letD.getPremise().accept(this);
        // todo implement
    }



    @Override
    public void visit(AppTerm appTerm) {

    }

    @Override
    public void visit(AbsTerm absTerm) {

    }

    @Override
    public void visit(LetTerm letTerm) {

    }

    @Override
    public void visit(VarTerm varTerm) {

    }

    @Override
    public void visit(ConstTerm constTerm) {

    }



    @Override
    public void visit(NamedType named) {
        visitorBuffer = TEXTTT + CURLY_LEFT + named.getName() + CURLY_RIGHT;
        needsParentheses = false;
    }

    @Override
    public void visit(TypeVariable variable) {
        String name;
        switch (variable.getKind()) {
            case TREE:
                name = TREE_VARIABLE;
                break;
            case GENERATED_TYPE_ASSUMPTION:
                name = GENERATED_ASSUMPTION_VARIABLE;
                break;
            case USER_INPUT:
                name = USER_VARIABLE;
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
        visitorBuffer = name + UNDERSCORE + CURLY_LEFT + variable.getIndex() + CURLY_RIGHT;
        needsParentheses = false;
    }

    @Override
    public void visit(FunctionType function) {
        function.getParameter().accept(this);
        String parameter = needsParentheses ? PAREN_LEFT + visitorBuffer + PAREN_RIGHT : visitorBuffer;
        function.getOutput().accept(this);
        String output = visitorBuffer;
        visitorBuffer = parameter + SPACE + RIGHT_ARROW + SPACE + output;
        needsParentheses = true;
    }
}
