package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Objects;

/**
 * Models one step of the inference tree where the variable rule is applied. It contains a type
 * abstraction that is identified as the type of the variable in the premise of the step.
 */
public abstract class VarStep extends InferenceStep {

    private final TypeAbstraction typeAbstractionInPremise;
    private final Type instantiatedTypeAbs;

    /**
     * Initializes a new VarStep with the given values.
     *
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     * @param instantiatedTypeAbs      an instantiation of the type abstraction used in this step
     * @param conclusion               the conclusion of this step
     * @param constraint               the constraint added in this step
     * @param stepIndex step number
     */
    protected VarStep(TypeAbstraction typeAbstractionInPremise, Type instantiatedTypeAbs, Conclusion conclusion,
                      Constraint constraint, int stepIndex) {
        super(conclusion, constraint, stepIndex);
        this.typeAbstractionInPremise = typeAbstractionInPremise;
        this.instantiatedTypeAbs = instantiatedTypeAbs;
    }

    /**
     * Returns the type abstraction in the premise of the step, that is identified as the
     * variable’s type.
     *
     * @return the type abstraction in the premise of this step
     */
    public TypeAbstraction getTypeAbsInPremise() {
        return typeAbstractionInPremise;
    }

    /**
     * Returns the instantiation of the type abstraction.
     *
     * @return the instantiation of the type abstraction.
     */
    public Type getInstantiatedTypeAbs() {
        return instantiatedTypeAbs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        VarStep varStep = (VarStep) o;
        return typeAbstractionInPremise.equals(varStep.typeAbstractionInPremise)
                && instantiatedTypeAbs.equals(varStep.instantiatedTypeAbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeAbstractionInPremise, instantiatedTypeAbs);
    }
}
