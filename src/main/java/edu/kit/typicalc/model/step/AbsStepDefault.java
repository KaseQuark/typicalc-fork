package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the abstraction rule
 * is applied and no let rule is applied in the entire tree.
 */
public class AbsStepDefault extends AbsStep {
    /**
     * Initializes a new AbsStepDefault with the given values.
     *
     * @param premise    the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     * @param stepIndex  step number
     */
    public AbsStepDefault(InferenceStep premise, Conclusion conclusion, Constraint constraint, int stepIndex) {
        super(premise, conclusion, constraint, stepIndex);
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
