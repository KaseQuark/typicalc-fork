package edu.kit.typicalc.model.step;

public class TestStepVisitor implements StepVisitor {
    public String visited = "";
    @Override
    public void visit(AbsStepDefault absD) {
        visited = "AbsDef";
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        visited = "AbsLet";
    }

    @Override
    public void visit(AppStepDefault appD) {
        visited = "AppDef";
    }

    @Override
    public void visit(ConstStepDefault constD) {
        visited = "ConstDef";
    }

    @Override
    public void visit(VarStepDefault varD) {
        visited = "VarDef";
    }

    @Override
    public void visit(VarStepWithLet varL) {
        visited = "VarLet";
    }

    @Override
    public void visit(LetStepDefault letD) {
        visited = "LetDef";
    }

    @Override
    public void visit(EmptyStep empty) {
        visited = "empty";
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        visited = "onlyConclusion";
    }
}
