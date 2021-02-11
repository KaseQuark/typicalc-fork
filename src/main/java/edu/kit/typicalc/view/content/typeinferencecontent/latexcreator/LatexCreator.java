package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.List;
import java.util.Map;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;

/**
 * Generates LaTeX code from a TypeInfererInterface object. Two mostly independent pieces
 * of code are generated: one for the constraints/unification and one for the proof tree.
 * The LaTeX code can be rendered by MathJax, so it must only use packages and commands that MathJax supports.
 * The LaTeX code is also usable outside of MathJax, in a normal LaTeX document.
 */
public class LatexCreator implements StepVisitor {
    private final StringBuilder tree;
    private final boolean stepLabels;
    private final LatexCreatorConstraints constraintsCreator;

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer theTypeInfererInterface to create the LaTeX-code from
     */
    public LatexCreator(TypeInfererInterface typeInferer) {
        this(typeInferer, true);
    }

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer theTypeInfererInterface to create the LaTeX-code from
     * @param stepLabels turns step labels on (true) or off (false)
     */
    public LatexCreator(TypeInfererInterface typeInferer, boolean stepLabels) {
        this.tree = new StringBuilder();
        this.stepLabels = stepLabels;
        typeInferer.getFirstInferenceStep().accept(this);
        this.constraintsCreator = new LatexCreatorConstraints(typeInferer);
    }

    /**
     * Returns the proof tree
     *
     * @return the LaTeX-code for the proof tree
     */
    public String getTree() {
        return TREE_BEGIN + NEW_LINE + tree.toString() + TREE_END;
    }

    /**
     * Returns the LaTeX-code for constraints, unification, MGU and final type
     *
     * @return the LaTeX-code for constraints and unification
     */
    public String[] getUnification() {
        return constraintsCreator.getEverything().toArray(new String[0]);
    }

    public List<Integer> getTreeNumbers() {
        return constraintsCreator.getTreeNumbers();
    }

    /**
     * Returns needed LaTeX packages
     *
     * @return the packages needed for the LaTeX-code from getTree() and getUnification() to work
     */
    public String getLatexPackages() {
        return BUSSPROOFS;
    } // todo implement



    private String typeAssumptionsToLatex(Map<VarTerm, TypeAbstraction> typeAssumptions) {
        if (typeAssumptions.isEmpty()) {
            return "";
        } else {
            StringBuilder assumptions = new StringBuilder();
            typeAssumptions.forEach(((varTerm, typeAbstraction) -> {
                String termLatex = new LatexCreatorTerm(varTerm).getLatex();
                String abstraction = generateTypeAbstraction(typeAbstraction);
                assumptions.append(termLatex)
                        .append(COLON)
                        .append(abstraction)
                        .append(COMMA);
            }));
            assumptions.deleteCharAt(assumptions.length() - 1);
            return assumptions.toString();
        }
    }


    private String conclusionToLatex(Conclusion conclusion) {
        String typeAssumptions = typeAssumptionsToLatex(conclusion.getTypeAssumptions());
        String term = new LatexCreatorTerm(conclusion.getLambdaTerm()).getLatex();
        String type = new LatexCreatorType(conclusion.getType()).getLatex();
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
        String assumptions = typeAssumptionsToLatex(var.getConclusion().getTypeAssumptions());
        String term = new LatexCreatorTerm(var.getConclusion().getLambdaTerm()).getLatex();
        String type = generateTypeAbstraction(var.getTypeAbsInPremise());
        return DOLLAR_SIGN + PAREN_LEFT + assumptions + PAREN_RIGHT + PAREN_LEFT + term
                + PAREN_RIGHT + EQUALS + type + DOLLAR_SIGN;
    }

    private String generateTypeAbstraction(TypeAbstraction abs) {
        StringBuilder abstraction = new StringBuilder();
        if (abs.hasQuantifiedVariables()) {
            abstraction.append(FOR_ALL);
            abs.getQuantifiedVariables().forEach(typeVariable -> {
                String variableTex = new LatexCreatorType(typeVariable).getLatex();
                abstraction.append(variableTex).append(COMMA);
            });
            abstraction.deleteCharAt(abstraction.length() - 1);
            abstraction.append(DOT_SIGN);
        }
        abstraction.append(new LatexCreatorType(abs.getInnerType()).getLatex());
        return abstraction.toString();
    }


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
        tree.insert(0, AXC + CURLY_LEFT + generateVarStepPremise(varD) + CURLY_RIGHT + NEW_LINE);
    }

    @Override
    public void visit(VarStepWithLet varL) {
        tree.insert(0, generateConclusion(varL, LABEL_VAR, UIC));
        String typeAbstraction = generateTypeAbstraction(varL.getTypeAbsInPremise());
        String instantiatedType = new LatexCreatorType(varL.getInstantiatedTypeAbs()).getLatex();
        String premiseRight = DOLLAR_SIGN + typeAbstraction + INSTANTIATE_SIGN + instantiatedType
                + DOLLAR_SIGN + NEW_LINE;
        String premiseLeft = AXC + CURLY_LEFT + DOLLAR_SIGN + ALIGN_BEGIN
                + generateVarStepPremise(varL).replace(DOLLAR_SIGN, "")
                + SPACE + LATEX_NEW_LINE + SPACE // TODO: less replacement fixups
                + premiseRight.replace(DOLLAR_SIGN, "")
                + ALIGN_END + DOLLAR_SIGN + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, premiseLeft);
    }

    @Override
    public void visit(LetStepDefault letD) {
        tree.insert(0, generateConclusion(letD, LABEL_LET, BIC));
        letD.getPremise().accept(this);
        letD.getTypeInferer().getFirstInferenceStep().accept(this);
        // todo correct?
    }

    @Override
    public void visit(EmptyStep empty) {
        String step = AXC + CURLY_LEFT + CURLY_RIGHT + NEW_LINE;
        tree.insert(0, step);
    }
}
