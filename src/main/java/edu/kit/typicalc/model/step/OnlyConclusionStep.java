package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.NamedType;

/**
 * OnlyConclusionSteps consist only of a conclusion and have no premises.
 * They are used if a let sub-inference failed and some branches of the tree should end abruptly
 * due to the termination of the algorithm, but still contain a conclusion.
 */
public class OnlyConclusionStep extends InferenceStep {

    /**
     * Initializes a new step with only a conclusion.
     *
     * @param conclusion the conclusion of the step
     */
    public OnlyConclusionStep(Conclusion conclusion) {
        super(
                conclusion,
                new Constraint(new NamedType(""), new NamedType(""))
        );
    }

    @Override
    public Constraint getConstraint() {
        throw new IllegalStateException("getConstraint() should never be called on an OnlyConclusionStep");
    }

    @Override
    public void accept(StepVisitor stepVisitor) {
        stepVisitor.visit(this);
    }
}
