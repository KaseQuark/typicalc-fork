package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.TypeInfererLet;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * A factory to create InferenceStep objects of a specific subclass
 */
public interface StepFactory {
    /**
     * Creates an AbsStep.
     *
     * @param premise    the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AbsStep
     */
    AbsStep createAbsStep(InferenceStep premise, Conclusion conclusion, Constraint constraint);

    /**
     * Creates an AppStep.
     *
     * @param premise1   the first premise of this step
     * @param premise2   the second premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AppStep
     */
    AppStep createAppStep(InferenceStep premise1, InferenceStep premise2,
                          Conclusion conclusion, Constraint constraint);

    /**
     * Creates an ConstStep.
     *
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created ConstStep
     */
    ConstStep createConstStep(Conclusion conclusion, Constraint constraint);

    /**
     * Creates a VarStep.
     *
     * @param typeAbstraction     the type abstraction of this step
     * @param instantiatedTypeAbs an instantiation of the type abstraction used in this step
     * @param conclusion          the conclusion of this step
     * @param constraint          the constraint that can be derived from this step
     * @return the created AbsStep
     */
    VarStep createVarStep(TypeAbstraction typeAbstraction, Type instantiatedTypeAbs,
                          Conclusion conclusion, Constraint constraint);

    /**
     * Creates a LetStep.
     *
     * @param conclusion  the conclusion of this step
     * @param constraint  the constraint that can be derived from this step
     * @param premise     the premise that doesn't need its own type inference
     * @param typeInferer the typeInferer for the premise that needs its own type inference
     * @return the created AppStep
     */
    LetStep createLetStep(Conclusion conclusion, Constraint constraint,
                          InferenceStep premise, TypeInfererLet typeInferer);
}
