package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the abstraction rule is applied and no let rule
 * is applied in the entire tree.
 */
public class AppStepDefault extends AppStep {
    /**
     * Initializes a new AbsStepDefault with the given values.
     *
     * @param premise1   the first premise of this step
     * @param premise2   the second premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint constraint that can be derived from this step
     * @param stepIndex  step number
     */
    public AppStepDefault(InferenceStep premise1, InferenceStep premise2,
                          Conclusion conclusion, Constraint constraint, int stepIndex) {
        super(premise1, premise2, conclusion, constraint, stepIndex);
    }

    /**
     * Accepts a visitor.
     *
     * @param stepVisitor – the visitor that wants to visit this object
     */
    @Override
    public void accept(StepVisitor stepVisitor) {
        stepVisitor.visit(this);
    }
}
