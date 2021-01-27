package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
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
    private Conclusion conclusion;
    private Constraint constraint;

    /**
     * Initializes a new InferenceStep with the given values.
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     */
    protected InferenceStep(Conclusion conclusion, Constraint constraint) {
        this.conclusion = conclusion;
        this.constraint = constraint;
    }

    /**
     * Getter for the Conclusion of this step.
     * @return conclusion the conclusion of this step
     */
    public Conclusion getConclusion() {
        return conclusion;
    }

    /**
     * Getter for the Constraint added in this step.
     * @return conclusion the constraint added in this step
     */
    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * Accepts a visitor.
     * @param stepVisitor the visitor that wants to visit this object
     */
    public abstract void accept(StepVisitor stepVisitor);
}
