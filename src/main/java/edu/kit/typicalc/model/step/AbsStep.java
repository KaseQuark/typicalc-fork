package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

import java.util.Objects;

/**
 * Models one step of the inference tree where the abstraction rule is applied.
 */
public abstract class AbsStep extends InferenceStep {

    private final InferenceStep premise;

    /**
     * Initializes a new AbsStep with the given values.
     *
     * @param premise    the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     */
    protected AbsStep(InferenceStep premise, Conclusion conclusion, Constraint constraint) {
        super(conclusion, constraint);
        this.premise = premise;
    }

    /**
     * Getter for the premise of this step.
     *
     * @return premise the premise of this step
     */
    public InferenceStep getPremise() {
        return this.premise;
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
        AbsStep absStep = (AbsStep) o;
        return premise.equals(absStep.premise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), premise);
    }
}
