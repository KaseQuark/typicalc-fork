package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

import java.util.Objects;

/**
 * Models one step of the inference tree.
 * Depending on the inference rule that is applied in a step,
 * different subclasses of InferenceStep should be used.
 * A step always contains the Constraint that is added
 * to the set of constraints of the type inference algorithm because
 * of this step and a Conclusion.
 * The subclasses vary in the premise(s) they contain.
 */
public abstract class InferenceStep {
    private final Conclusion conclusion;
    private final Constraint constraint;
    private final int stepIndex;

    /**
     * Initializes a new InferenceStep with the given values.
     *
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     * @param stepIndex step number
     */
    protected InferenceStep(Conclusion conclusion, Constraint constraint, int stepIndex) {
        this.conclusion = conclusion;
        this.constraint = constraint;
        this.stepIndex = stepIndex;
    }

    /**
     * Getter for the Conclusion of this step.
     *
     * @return conclusion the conclusion of this step
     */
    public Conclusion getConclusion() {
        return conclusion;
    }

    /**
     * Getter for the Constraint added in this step.
     *
     * @return conclusion the constraint added in this step
     */
    public Constraint getConstraint() {
        return constraint;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    /**
     * Accepts a visitor.
     *
     * @param stepVisitor the visitor that wants to visit this object
     */
    public abstract void accept(StepVisitor stepVisitor);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InferenceStep that = (InferenceStep) o;
        return conclusion.equals(that.conclusion) && constraint.equals(that.constraint) && stepIndex == that.stepIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conclusion, constraint, stepIndex);
    }
}