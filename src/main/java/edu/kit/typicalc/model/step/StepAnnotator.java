package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StepAnnotator implements StepVisitor {
    private final List<String> annotations = new ArrayList<>();

    public List<String> getAnnotations() {
        return annotations;
    }

    @Override
    public void visit(AbsStepDefault absD) {
        visitAbs(absD);
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        visitAbs(absL);
        absL.getPremise().accept(this);
    }

    private void visitAbs(AbsStep absStep) {
        var t1 = ((FunctionType) absStep.getConstraint().getSecondType()).getParameter();
        var t2 = ((FunctionType) absStep.getConstraint().getSecondType()).getOutput();
        visitGeneric(t1, t2, absStep.getConstraint());
    }

    private void visitGeneric(Type t1, Type t2, Constraint c) {
        visitGeneric2(List.of(Pair.of("_1", t1), Pair.of("_2", t2)), c);
    }

    private void visitGeneric2(List<Pair<String, Type>> types, Constraint c) {
        visitGeneric3(
                types.stream()
                        .map(pair -> Pair.of(pair.getLeft(),
                                new LatexCreatorType(pair.getRight(), LatexCreatorMode.NORMAL).getLatex()))
                        .collect(Collectors.toList()),
                c
        );
    }

    private void visitGeneric3(List<Pair<String, String>> types, Constraint c) {
        var sb = new StringBuilder("$$\\begin{align}");
        for (var pair : types) {
            sb.append("&" + LatexCreatorConstants.RULE_VARIABLE)
                    .append(pair.getLeft())
                    .append(" := ")
                    .append(pair.getRight())
                    .append(LatexCreatorConstants.LATEX_NEW_LINE)
                    .append("\n");
        }
        sb.append("&").append(LatexCreatorConstraints.createSingleConstraint(c, LatexCreatorMode.NORMAL));
        annotations.add(sb + "\\end{align}$$");
    }

    @Override
    public void visit(AppStepDefault appD) {
        visitGeneric(
                appD.getPremise2().getConclusion().getType(),
                appD.getConclusion().getType(),
                appD.getConstraint());
        appD.getPremise1().accept(this);
        appD.getPremise2().accept(this);
    }

    @Override
    public void visit(ConstStepDefault constD) {
        visitGeneric2(List.of(Pair.of("", constD.getConstraint().getSecondType())), constD.getConstraint());
    }

    @Override
    public void visit(VarStepDefault varD) {
        visitGeneric2(List.of(
                Pair.of("", varD.getInstantiatedTypeAbs())),
                varD.getConstraint());
    }

    @Override
    public void visit(VarStepWithLet varL) {
        visitGeneric3(List.of(
                Pair.of("'",
                        AssumptionGeneratorUtil.generateTypeAbstraction(varL.getTypeAbsInPremise(),
                                LatexCreatorMode.NORMAL)),
                Pair.of("",
                        new LatexCreatorType(varL.getInstantiatedTypeAbs(), LatexCreatorMode.NORMAL).getLatex())),
                varL.getConstraint());
    }

    @Override
    public void visit(LetStepDefault letD) {
        visitGeneric2(List.of(
                Pair.of("_1", letD.getTypeInferer().getFirstInferenceStep().getConclusion().getType()),
                Pair.of("_2", letD.getPremise().getConclusion().getType())),
                letD.getConstraint());
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
