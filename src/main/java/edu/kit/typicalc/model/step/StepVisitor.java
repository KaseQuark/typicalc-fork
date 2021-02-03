package edu.kit.typicalc.model.step;

/**
 * StepVisitor can be implemented to process InferenceStep objects.
 */
public interface StepVisitor {

    /**
     * Visits an AbsStepDefault.
     * @param absD the AbsStepDefault to visit
     */
    void visit(AbsStepDefault absD);

    /**
     * Visits an AbsStepWithLet.
     * @param absL the AbsStepWithLet to visit
     */
    void visit(AbsStepWithLet absL);

    /**
     * Visits an AppStepDefault.
     * @param appD the AppStepDefault to visit
     */
    void visit(AppStepDefault appD);

    /**
     * ConstStepDefault.
     * @param constD the ConstStepDefault to visit
     */
    void visit(ConstStepDefault constD);

    /**
     * Visits a VarStepDefault.
     * @param varD the VarStepDefault to visit
     */
    void visit(VarStepDefault varD);

    /**
     * Visits a VarStepWithLet.
     * @param varL the VarStepWithLet to visit
     */
    void visit(VarStepWithLet varL);

    /**
     * Visits a LetStepDefault.
     * @param letD the LetStepDefault to visit
     */
    void visit(LetStepDefault letD);

    /**
     * Visits an empty step
     * @param empty the empty step to visit
     */
    void visit(EmptyStep empty);
}
