package edu.kit.typicalc.model.step;

import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstraints;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorMode;

import java.util.ArrayList;
import java.util.List;

public class StepAnnotator implements StepVisitor {
    private final List<String> annotations = new ArrayList<>();

    public List<String> getAnnotations() {
        return annotations;
    }

    @Override
    public void visit(AbsStepDefault absD) {
        annotations.add("$"
                + LatexCreatorConstraints.createSingleConstraint(absD.getConstraint(), LatexCreatorMode.NORMAL) + "$");
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
