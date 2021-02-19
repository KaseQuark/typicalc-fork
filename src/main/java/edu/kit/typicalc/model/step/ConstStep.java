package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the constant rule is applied.
 */
public abstract class ConstStep extends InferenceStep {
    /**
     * Initializes a new ConstStep with the given values.
     *
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     */
    protected ConstStep(Conclusion conclusion, Constraint constraint) {
        super(conclusion, constraint);
    }
}
