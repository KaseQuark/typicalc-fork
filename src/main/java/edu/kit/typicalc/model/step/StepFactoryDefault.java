package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.TypeInfererLet;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * A factory to create InferenceStep objects when let polymorphism is not used.
 */
public class StepFactoryDefault implements StepFactory {

    /**
     * Creates an AbsStepDefault.
     *
     * @param premise    the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AbsStepDefault
     */
    @Override
    public AbsStepDefault createAbsStep(InferenceStep premise, Conclusion conclusion, Constraint constraint) {
        return new AbsStepDefault(premise, conclusion, constraint);
    }

    /**
     * Creates an AppStepDefault.
     *
     * @param premise1   the first premise of this step
     * @param premise2   the second premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AppStepDefault
     */
    @Override
    public AppStepDefault createAppStep(InferenceStep premise1, InferenceStep premise2,
                                        Conclusion conclusion, Constraint constraint) {
        return new AppStepDefault(premise1, premise2, conclusion, constraint);
    }

    /**
     * Creates an ConstStepDefault.
     *
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created ConstStepDefault
     */
    @Override
    public ConstStepDefault createConstStep(Conclusion conclusion, Constraint constraint) {
        return new ConstStepDefault(conclusion, constraint);
    }

    /**
     * Creates a VarStepDefault.
     *
     * @param typeAbstraction     the type abstraction of this step
     * @param instantiatedTypeAbs an instantiation of the type abstraction used in this step
     * @param conclusion          the conclusion of this step
     * @param constraint          the constraint that can be derived from this step
     * @return the created AbsStepDefault
     */
    @Override
    public VarStepDefault createVarStep(TypeAbstraction typeAbstraction, Type instantiatedTypeAbs,
                                        Conclusion conclusion, Constraint constraint) {
        return new VarStepDefault(typeAbstraction, instantiatedTypeAbs, conclusion, constraint);
    }

    /**
     * Throws an UnsupportedOperationException.
     * This method should never be called, as a StepFactoryDefault should only be used
     * for lambda terms without any let polymorphism and therefore should never have
     * to create a step where the let rule is applied.
     *
     * @param conclusion  the conclusion of this step
     * @param constraint  the constraint that can be derived from this step
     * @param premise     the premise that doesn't need its own type inference
     * @param typeInferer the typeInferer for the premise that needs its own type inference
     * @return nothing
     */
    @Override
    public LetStep createLetStep(Conclusion conclusion, Constraint constraint,
                                 InferenceStep premise, TypeInfererLet typeInferer) {
        throw new UnsupportedOperationException("Not possible to create LetStep when no let is present in the term");
    }
}
