package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.model.step.*;

import java.util.List;
import java.util.function.Function;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.generateTypeAbstraction;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.typeAssumptionsToLatex;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;

/**
 * Generates LaTeX code from a TypeInfererInterface object. Two mostly independent pieces
 * of code are generated: one for the constraints/unification and one for the proof tree.
 * The LaTeX code can be rendered by MathJax, so it must only use packages and commands that MathJax supports.
 * The LaTeX code is also usable outside of MathJax, in a normal LaTeX document.
 *
 * @see InferenceStep
 */
public class LatexCreator implements StepVisitor {
    private final StringBuilder tree;
    private final LatexCreatorMode mode;
    private final boolean stepLabels;
    private final LatexCreatorConstraints constraintsCreator;

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer         theTypeInfererInterface to create the LaTeX-code from
     * @param translationProvider translation text provider for {@link UnificationError}
     * @param mode                LaTeX creation mode
     */
    public LatexCreator(TypeInfererInterface typeInferer, Function<UnificationError, String> translationProvider,
                        LatexCreatorMode mode) {
        this(typeInferer, true, translationProvider, mode);
    }

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer         theTypeInfererInterface to create the LaTeX code from
     * @param stepLabels          turns step labels on or off
     * @param translationProvider translation text provider for {@link UnificationError}
     * @param mode                LaTeX creation mode
     */
    public LatexCreator(TypeInfererInterface typeInferer, boolean stepLabels,
                        Function<UnificationError, String> translationProvider,
                        LatexCreatorMode mode) {
        this.tree = new StringBuilder();
        this.mode = mode;
        this.stepLabels = stepLabels;
        typeInferer.getFirstInferenceStep().accept(this);
        this.constraintsCreator = new LatexCreatorConstraints(typeInferer, translationProvider, mode);
    }

    /**
     * Returns the proof tree.
     *
     * @return the LaTeX-code for the proof tree
     */
    public String getTree() {
        return TREE_BEGIN + NEW_LINE + tree.toString() + TREE_END;
    }

    /**
     * Returns the LaTeX-code for constraints, unification, MGU and final type.
     *
     * @return the LaTeX-code for constraints and unification
     */
    public String[] getUnification() {
        return constraintsCreator.getEverything().toArray(new String[0]);
    }

    /**
     * Returns a list of numbers which correlate to the steps should be in during certain steps of the unification.
     *
     * @return a list of step indices
     */
    public List<Integer> getTreeNumbers() {
        return constraintsCreator.getTreeNumbers();
    }

    private String conclusionToLatex(Conclusion conclusion) {
        String typeAssumptions = typeAssumptionsToLatex(conclusion.getTypeAssumptions(), mode);
        String term = new LatexCreatorTerm(conclusion.getLambdaTerm(), mode).getLatex();
        String type = new LatexCreatorType(conclusion.getType(), mode).getLatex();
        return DOLLAR_SIGN + typeAssumptions + VDASH + term + COLON + type + DOLLAR_SIGN;
    }

    private StringBuilder generateConclusion(InferenceStep step, String label, String command) {
        StringBuilder conclusion = new StringBuilder();
        if (stepLabels) {
            conclusion.append(label).append(NEW_LINE);
        }
        conclusion.append(command)
                .append(CURLY_LEFT)
                .append(conclusionToLatex(step.getConclusion()))
                .append(CURLY_RIGHT)
                .append(NEW_LINE);
        return conclusion;
    }

    private String generateVarStepPremise(VarStep var) {
        String assumptions = typeAssumptionsToLatex(var.getConclusion().getTypeAssumptions(), mode);
        String term = new LatexCreatorTerm(var.getConclusion().getLambdaTerm(), mode).getLatex();
        String type = generateTypeAbstraction(var.getTypeAbsInPremise(), mode);
        return PAREN_LEFT + assumptions + PAREN_RIGHT + PAREN_LEFT + term
                + PAREN_RIGHT + EQUALS + type;
    }


    @Override
    public void visit(AbsStepDefault absD) {
        tree.insert(0, generateConclusion(absD,
                mode == LatexCreatorMode.MATHJAX
                        ? "\\LeftLabel{\\class{typicalc-label typicalc-label-abs typicalc-step-" + absD.getStepIndex()
                            + "}{\\textrm A{\\small BS}}}" : LABEL_ABS,
                UIC));
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        tree.insert(0, generateConclusion(absL,
                mode == LatexCreatorMode.MATHJAX
                        ? "\\LeftLabel{\\class{typicalc-label typicalc-label-abs-let typicalc-step-" + absL.getStepIndex()
                                + "}{\\textrm A{\\small BS}}}" : LABEL_ABS,
                UIC));
        absL.getPremise().accept(this);
    }

    @Override
    public void visit(AppStepDefault appD) {
        tree.insert(0, generateConclusion(appD,
                mode == LatexCreatorMode.MATHJAX
                        ? "\\LeftLabel{\\class{typicalc-label typicalc-label-app typicalc-step-" + appD.getStepIndex()
                                + "}{\\textrm A{\\small PP}}}" : LABEL_APP,
                BIC));
        appD.getPremise2().accept(this);
        appD.getPremise1().accept(this);
    }

    @Override
    public void visit(ConstStepDefault constD) {
        tree.insert(0, generateConclusion(constD,
                mode == LatexCreatorMode.MATHJAX
                        ? "\\LeftLabel{\\class{typicalc-label typicalc-label-const typicalc-step-"
                                + constD.getStepIndex() + "}{\\textrm C{\\small ONST}}}" : LABEL_CONST,
                UIC));
        String visitorBuffer = new LatexCreatorTerm(constD.getConclusion().getLambdaTerm(), mode).getLatex();
        String step = AXC + CURLY_LEFT + DOLLAR_SIGN + visitorBuffer + SPACE + LATEX_IN + SPACE + CONST
                + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, step);
    }

    @Override
    public void visit(VarStepDefault varD) {
        String latex = "";
        if (mode == LatexCreatorMode.MATHJAX) {
            latex = "\\LeftLabel{\\class{typicalc-label typicalc-label-var "
                    + "typicalc-step-" + varD.getStepIndex() + "}{\\textrm V{\\small AR}}}";
        } else {
            latex = LABEL_VAR;
        }
        tree.insert(0, generateConclusion(varD, latex, UIC));
        tree.insert(0, AXC + CURLY_LEFT + DOLLAR_SIGN + generateVarStepPremise(varD)
                + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE);
    }

    @Override
    public void visit(VarStepWithLet varL) {
        String latex = "";
        if (mode == LatexCreatorMode.MATHJAX) {
            latex = "\\LeftLabel{\\class{typicalc-label typicalc-label-var-let "
                    + "typicalc-step-" + varL.getStepIndex() + "}{\\textrm V{\\small AR}}}";
        } else {
            latex = LABEL_VAR;
        }
        tree.insert(0, generateConclusion(varL, latex, UIC));
        String typeAbstraction = generateTypeAbstraction(varL.getTypeAbsInPremise(), mode);
        String instantiatedType = new LatexCreatorType(varL.getInstantiatedTypeAbs(), mode).getLatex();
        String premiseRight = typeAbstraction + INSTANTIATE_SIGN + instantiatedType;
        String premiseLeft = AXC + CURLY_LEFT + DOLLAR_SIGN + ALIGN_BEGIN
                + generateVarStepPremise(varL)
                + SPACE + LATEX_NEW_LINE + SPACE
                + premiseRight
                + ALIGN_END + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, premiseLeft);
    }

    @Override
    public void visit(LetStepDefault letD) {
        String latex = "";
        if (mode == LatexCreatorMode.MATHJAX) {
            latex = "\\LeftLabel{\\class{typicalc-label typicalc-label-let "
                    + "typicalc-step-" + letD.getStepIndex() + "}{\\textrm L{\\small ET}}}";
        } else {
            latex = LABEL_LET;
        }
        tree.insert(0, generateConclusion(letD,
                latex,
                BIC));
        letD.getPremise().accept(this);
        letD.getTypeInferer().getFirstInferenceStep().accept(this);
    }

    @Override
    public void visit(EmptyStep empty) {
        String step = AXC + CURLY_LEFT + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, step);
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        String step = AXC + CURLY_LEFT + conclusionToLatex(onlyConc.getConclusion()) + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, step);
    }
}
