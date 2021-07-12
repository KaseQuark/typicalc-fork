package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

import java.util.Objects;

/**
 * Models one step of the inference tree where the application rule is applied. The first
 * premise contains the sub-tree of inference steps for the function of the application term.
 * The second premise contains the sub-tree of inference steps for the input of the application
 * term.
 */
public abstract class AppStep extends InferenceStep {
    private final InferenceStep premise1;
    private final InferenceStep premise2;

    /**
     * Initializes a new AbsStep with the given values.
     *
     * @param premise1   the first premise of this step
     * @param premise2   the second premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint constraint that can be derived from this step
     * @param stepIndex  step number
     */
    protected AppStep(InferenceStep premise1, InferenceStep premise2, Conclusion conclusion, Constraint constraint,
                      int stepIndex) {
        super(conclusion, constraint, stepIndex);
        this.premise1 = premise1;
        this.premise2 = premise2;
    }

    /**
     * Getter for the first premise of this Step.
     *
     * @return premise1 the first premise of this Step.
     */
    public InferenceStep getPremise1() {
        return premise1;
    }

    /**
     * Getter for the second premise of this Step.
     *
     * @return premise2 the second premise of this Step.
     */
    public InferenceStep getPremise2() {
        return premise2;
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
        AppStep appStep = (AppStep) o;
        return premise1.equals(appStep.premise1) && premise2.equals(appStep.premise2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), premise1, premise2);
    }
}
