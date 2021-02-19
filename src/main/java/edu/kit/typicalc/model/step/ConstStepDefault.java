package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;

/**
 * Models one step of the inference tree where the constant rule is applied. As the constant
 * rule is the same regardless of whether the tree contains a let step, this is the only subclass
 * of ConstStep.
 */
public class ConstStepDefault extends ConstStep {
    /**
     * Initializes a new ConstStep with the given values.
     *
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     */
    public ConstStepDefault(Conclusion conclusion, Constraint constraint) {
        super(conclusion, constraint);
    }

    /**
     * Accepts a visitor.
     *
     * @param stepVisitor the visitor that wants to visit this object
     */
    @Override
    public void accept(StepVisitor stepVisitor) {
        stepVisitor.visit(this);
    }
}
