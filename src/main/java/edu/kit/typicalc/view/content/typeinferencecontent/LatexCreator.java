package edu.kit.typicalc.view.content.typeinferencecontent;


import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.model.UnificationStep;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

/**
 * Generates LaTeX-code from a TypeInfererInterface object. Two mostly independent pie-
 * ces of code are generated, one for the constraints/unification and one for the proof tree.
 * The LaTeX-code is created in a form, that it can be rendered by MathJax, so it must
 * only use packages and commands that MathJax supports. The LaTeX code is also usable
 * outside of MathJax, in a normal .tex document.
 */
public class LatexCreator implements StepVisitor {
    private final TypeInfererInterface typeInferer;
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
        this.typeInferer = typeInferer;
        this.tree = new StringBuilder();
        this.stepLabels = stepLabels;
    }

    /**
     * @return the LaTeX-code for the proof tree
     */
    protected String getTree() {
        typeInferer.getFirstInferenceStep().accept(this);
        return TREE_BEGIN + NEW_LINE + tree.toString() + TREE_END;
    }

    /**
     * @return the LaTeX-code for constraints nad unification
     */
    protected String[] getUnification() {
        List<String> steps = new ArrayList<>();
        for (UnificationStep step : typeInferer.getUnificationSteps()) {
            Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();
            if (subs.isError()) {
                continue; // TODO
            }
            StringBuilder latex = new StringBuilder();
            latex.append(DOLLAR_SIGN);
            latex.append("\\begin{align}");
            List<Substitution> substitutions = subs.unwrap();
            for (Substitution s : substitutions) {
                latex.append(new LatexCreatorType(s.getVariable()).getLatex());
                latex.append(RIGHT_ARROW);
                latex.append(new LatexCreatorType(s.getType()).getLatex());
                latex.append("\\\\");
            }
            List<Constraint> constraints = step.getConstraints();
            for (Constraint c : constraints) {
                latex.append(new LatexCreatorType(c.getFirstType()).getLatex());
                latex.append(EQUALS);
                latex.append(new LatexCreatorType(c.getSecondType()).getLatex());
                latex.append("\\\\");
            }
            latex.append("\\end{align}");
            latex.append(DOLLAR_SIGN);
            steps.add(latex.toString());
        }
        return steps.toArray(new String[0]);
    } // todo implement

    /**
     * @return the packages needed for the LaTeX-code from getTree() and getUnification()to work
     */
    protected String getLatexPackages() {
        return "the packages should be here";
    } // todo implement

    private String typeAssumptionsToLatex(Map<VarTerm, TypeAbstraction> typeAssumptions) {
//        typeAssumptions.forEach(((varTerm, typeAbstraction) -> {
//            varTerm.accept(this);
//            visitorBuffer
//        }));
        return "{some other text}";
    }

    private String conclusionToLatex(Conclusion conclusion) {
        String typeAssumptions = typeAssumptionsToLatex(conclusion.getTypeAssumptions());
        String term = new LatexCreatorTerm(conclusion.getLambdaTerm()).getLatex();
        String type = new LatexCreatorType(conclusion.getType()).getLatex();
        return DOLLAR_SIGN + GAMMA + VDASH + term + COLON + type + DOLLAR_SIGN;
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

    private String generateConstraint(InferenceStep step) {
        String firstType = new LatexCreatorType(step.getConstraint().getFirstType()).getLatex();
        String secondType = new LatexCreatorType(step.getConstraint().getSecondType()).getLatex();
        return firstType + SPACE + EQUALS + SPACE + secondType;
    }

    private String generateVarStepPremise(VarStep var) {
        String assumptions = typeAssumptionsToLatex(var.getConclusion().getTypeAssumptions());
        String term = new LatexCreatorTerm(var.getConclusion().getLambdaTerm()).getLatex();
        String type = generateTypeAbstraction(var.getTypeAbsInPremise());
        return AXC + CURLY_LEFT + DOLLAR_SIGN + PAREN_LEFT + assumptions + PAREN_RIGHT + PAREN_LEFT + term
                + PAREN_RIGHT + EQUALS + type + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
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
        String visitorBuffer = new LatexCreatorTerm(constD.getConclusion().getLambdaTerm()).getLatex();
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
        String instantiatedType = new LatexCreatorType(varL.getInstantiatedTypeAbs()).getLatex();
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
    public void visit(EmptyStep empty) {
        // TODO
    }
}
