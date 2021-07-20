package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstraints;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorMode;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorType;

import java.util.ArrayList;
import java.util.List;

public class StepAnnotator implements StepVisitor {
    private final List<String> annotations = new ArrayList<>();

    public List<String> getAnnotations() {
        return annotations;
    }

    @Override
    public void visit(AbsStepDefault absD) {
        var t1 = ((FunctionType) absD.getConstraint().getSecondType()).getParameter();
        var t2 = ((FunctionType) absD.getConstraint().getSecondType()).getOutput();
        annotations.add("$$\\begin{align}"
                + "&" + LatexCreatorConstants.RULE_VARIABLE + "_1 := "
                + new LatexCreatorType(t1, LatexCreatorMode.NORMAL).getLatex() + LatexCreatorConstants.LATEX_NEW_LINE + "\n"
                + "&" + LatexCreatorConstants.RULE_VARIABLE + "_2 := "
                + new LatexCreatorType(t2, LatexCreatorMode.NORMAL).getLatex() + LatexCreatorConstants.LATEX_NEW_LINE + "\n"
                + "&" + LatexCreatorConstraints.createSingleConstraint(absD.getConstraint(), LatexCreatorMode.NORMAL)
                + "\\end{align}$$");
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(absL.getConstraint(), LatexCreatorMode.NORMAL) + "$");
        absL.getPremise().accept(this);
    }

    @Override
    public void visit(AppStepDefault appD) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(appD.getConstraint(), LatexCreatorMode.NORMAL) + "$");
        appD.getPremise1().accept(this);
        appD.getPremise2().accept(this);
    }

    @Override
    public void visit(ConstStepDefault constD) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(constD.getConstraint(), LatexCreatorMode.NORMAL)
                + "$");
    }

    @Override
    public void visit(VarStepDefault varD) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(varD.getConstraint(), LatexCreatorMode.NORMAL) + "$");
    }

    @Override
    public void visit(VarStepWithLet varL) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(varL.getConstraint(), LatexCreatorMode.NORMAL) + "$");
    }

    @Override
    public void visit(LetStepDefault letD) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(letD.getConstraint(), LatexCreatorMode.NORMAL) + "$");
        letD.getTypeInferer().getFirstInferenceStep().accept(this);
        letD.getPremise().accept(this);
    }

    @Override
    public void visit(EmptyStep empty) {
        annotations.add("empty");
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        annotations.add("structural step");
    }
}
