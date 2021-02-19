package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.TypeInfererLet;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * A factory to create InferenceStep objects when let polymorphism is used.
 */
public class StepFactoryWithLet implements StepFactory {

    /**
     * Creates an AbsStepWithLet.
     *
     * @param premise    the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint that can be derived from this step
     * @return the created AbsStepWithLet
     */
    @Override
    public AbsStepWithLet createAbsStep(InferenceStep premise, Conclusion conclusion, Constraint constraint) {
        return new AbsStepWithLet(premise, conclusion, constraint);
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
     * Creates a VarStepWithLet.
     *
     * @param typeAbstraction     the type abstraction of this step
     * @param instantiatedTypeAbs an instantiation of the type abstraction used in this step
     * @param conclusion          the conclusion of this step
     * @param constraint          the constraint that can be derived from this step
     * @return the created VarStepWithLet
     */
    @Override
    public VarStepWithLet createVarStep(TypeAbstraction typeAbstraction, Type instantiatedTypeAbs,
                                        Conclusion conclusion, Constraint constraint) {
        return new VarStepWithLet(typeAbstraction, instantiatedTypeAbs, conclusion, constraint);
    }

    /**
     * Creates a LetStepDefault.
     *
     * @param conclusion  the conclusion of this step
     * @param constraint  the constraint that can be derived from this step
     * @param premise     the premise that doesn't need its own type inference
     * @param typeInferer the typeInferer for the premise that needs its own type inference
     * @return the created LetStepDefault
     */
    @Override
    public LetStepDefault createLetStep(Conclusion conclusion, Constraint constraint,
                                        InferenceStep premise, TypeInfererLet typeInferer) {
        return new LetStepDefault(conclusion, constraint, premise, typeInferer);
    }
}
