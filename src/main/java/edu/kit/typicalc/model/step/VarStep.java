package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * Models one step of the inference tree where the variable rule is applied. It contains a type
 * abstraction that is identified as the type of the variable in the premise of the step.
 */
public abstract class VarStep extends InferenceStep {
    private TypeAbstraction typeAbstractionInPremise;

    /**
     * Initializes a new VarStep with the given values.
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     */
    protected VarStep(TypeAbstraction typeAbstractionInPremise, Conclusion conclusion, Constraint constraint) {
        super(conclusion, constraint);
        this.typeAbstractionInPremise = typeAbstractionInPremise;
    }
    /**
     * Returns the type abstraction in the premise of the step, that is identified as the
     * variable’s type.
     * @return the type abstraction in the premise of this step
     */
    public TypeAbstraction getTypeAbsInPremise() {
        return typeAbstractionInPremise;
    }
}
