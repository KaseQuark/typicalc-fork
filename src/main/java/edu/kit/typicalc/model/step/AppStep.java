package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the application rule is applied. The first
 * premise contains the sub-tree of inference steps for the function of the application term.
 * The second premise contains the sub-tree of inference steps for the input of the application
 * term.
 */
public abstract class AppStep extends InferenceStep {
    private InferenceStep premise1;
    private InferenceStep premise2;

    protected AppStep(InferenceStep premise1, InferenceStep premise2, Conclusion conclusion, Constraint constraint) {
        super(conclusion, constraint);
        this.premise1 = premise1;
        this.premise2 = premise2;
    }

    /**
     * Getter for the first premise of this Step.
     * @return premise1 the first premise of this Step.
     */
    public InferenceStep getPremise1() {
        return premise1;
    }
    /**
     * Getter for the second premise of this Step.
     * @return premise2 the second premise of this Step.
     */
    public InferenceStep getPremise2() {
        return premise2;
    }
}
