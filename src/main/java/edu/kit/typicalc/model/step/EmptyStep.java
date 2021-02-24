package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;

import java.util.Collections;

/**
 * Empty steps are used if the sub-inference that is started when creating a let step failed and the second premise of
 * this let step therefore cannot be calculated.
 */
public class EmptyStep extends InferenceStep {

    /**
     * Initializes a new empty step.
     */
    public EmptyStep() {
        super(
                new Conclusion(Collections.emptyMap(), new VarTerm(""), new NamedType("")),
                new Constraint(new NamedType(""), new NamedType(""))
        );
    }

    @Override
    public Conclusion getConclusion() {
        throw new IllegalStateException("getConclusion() should never be called on an empty step");
    }

    @Override
    public Constraint getConstraint() {
        throw new IllegalStateException("getConstraint() should never be called on an empty step");
    }

    @Override
    public void accept(StepVisitor stepVisitor) {
        stepVisitor.visit(this);
    }
}
