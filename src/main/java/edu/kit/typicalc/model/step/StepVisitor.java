package edu.kit.typicalc.model.step;

/**
 * StepVisitor can be implemented to process InferenceStep objects.
 */
public interface StepVisitor {

    /**
     * Visits an AbsStepDefault.
     * @param absD the AbsStepDefault to visit
     */
    void visitAbsStepDefault(AbsStepDefault absD);

    /**
     * Visits an AbsStepWithLet.
     * @param absL the AbsStepWithLet to visit
     */
    void visitAbsStepWithLet(AbsStepWithLet absL);

    /**
     * Visits an AppStepDefault.
     * @param appD the AppStepDefault to visit
     */
    void visitAppStepDefault(AppStepDefault appD);

    /**
     * ConstStepDefault.
     * @param constD the ConstStepDefault to visit
     */
    void visitConstStepDefault(ConstStepDefault constD);

    /**
     * Visits a VarStepDefault.
     * @param varD the VarStepDefault to visit
     */
    void visitVarStepDefault(VarStepDefault varD);

    /**
     * Visits a VarStepWithLet.
     * @param varL the VarStepWithLet to visit
     */
    void visitVarStepWithLet(VarStepWithLet varL);

    /**
     * Visits a LetStepDefault.
     * @param letD the LetStepDefault to visit
     */
    void visitLetStepDefault(LetStepDefault letD);





}
