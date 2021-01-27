package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * A factory to create InferenceStep objects when let polymorphism is used.
 */
public class StepFactoryWithLet implements StepFactory {
    /**
     * Creates an AbsStepWithLet.
     * @param premise the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AbsStepWithLet
     */
    public AbsStepWithLet createAbsStep(InferenceStep premise, Conclusion conclusion, Constraint constraint) {
        return new AbsStepWithLet(premise, conclusion, constraint);
    }

    /**
     * Creates an AppStepDefault.
     * @param premise1 the first premise of this step
     * @param premise2 the second premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AppStepDefault
     */
    public AppStepDefault createAppStep(InferenceStep premise1, InferenceStep premise2,
                                        Conclusion conclusion, Constraint constraint) {
        return new AppStepDefault(premise1, premise2, conclusion, constraint);
    }
    /**
     * Creates an ConstStepDefault.
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created ConstStepDefault
     */
    public ConstStepDefault createConstStep(Conclusion conclusion, Constraint constraint) {
        return new ConstStepDefault(conclusion, constraint);
    }
    /**
     * Creates a VarStepWithLet.
     * @param typeAbstraction the type abstraction of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created VarStepWithLet
     */
    public  VarStepWithLet createVarStep(TypeAbstraction typeAbstraction, Conclusion conclusion,
                                         Constraint constraint) {
        return new VarStepWithLet(typeAbstraction, conclusion, constraint);
    }
}
