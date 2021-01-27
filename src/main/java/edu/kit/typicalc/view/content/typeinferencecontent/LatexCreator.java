package edu.kit.typicalc.view.content.typeinferencecontent;


import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.StepVisitor;

/**
 * Generates LaTeX-code from a TypeInfererInterface object. Two mostly independent pie-
 * ces of code are generated, one for the constraints/unification and one for the proof tree.
 * The LaTeX-code is created in a form, that it can be rendered by MathJax, so it must
 * only use packages and commands that MathJax supports. The LaTeX code is also usable
 * outside of MathJax, in a normal .tex document.
 */
public class LatexCreator implements StepVisitor, TermVisitor, TypeVisitor {

    /**
     * Generate the pieces of LaTeX-code from the type inferer.
     *
     * @param typeInferer theTypeInfererInterface to create the LaTeX-code from
     */
    protected LatexCreator(TypeInfererInterface typeInferer) {
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


    @Override
    public void visitAbsStepDefault(AbsStepDefault absD) {

    }

    @Override
    public void visitAbsStepWithLet(AbsStepWithLet absL) {

    }

    @Override
    public void visitAppStepDefault(AppStepDefault appD) {

    }

    @Override
    public void visitConstStepDefault(ConstStepDefault constD) {

    }

    @Override
    public void visitVarStepDefault(VarStepDefault varD) {

    }

    @Override
    public void visitVarStepWithLet(AbsStepDefault varD) {

    }

    @Override
    public void visitLetStepDefault(LetStepDefault letD) {

    }
}
