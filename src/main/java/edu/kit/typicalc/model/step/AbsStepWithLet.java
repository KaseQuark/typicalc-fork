package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the abstraction rule is applied and at least
 * one let rule is applied in the entire tree.
 */
public class AbsStepWithLet extends AbsStep {
    /**
     *Initializes a new AbsStepWithLet with the given values.
     * @param premise the premise of this step
     * @param conclusion the conclusion of this step
     * @param constraint constraint that can be derived from this step
     */
    public AbsStepWithLet(InferenceStep premise, Conclusion conclusion, Constraint constraint) {
        super(premise, conclusion, constraint);
    }

    /**
     * Accepts a visitor.
     * @param stepVisitor the visitor that wants to visit this object
     */
    @Override
    public void accept(StepVisitor stepVisitor) {
        stepVisitor.visit(this);
    }
}
