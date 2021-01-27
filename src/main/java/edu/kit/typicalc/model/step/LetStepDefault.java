package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.TypeInfererLet;

/**
 * Models one step of the inference tree where the let rule is applied.
 */
public class LetStepDefault extends LetStep {
    /**
     * Initializes a new LetStep with the given values.
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     * @param premise the right premise of this step
     * @param typeInferer the typeInferer that performs the Type Inference for the premise
     *                    that needs its own type Inference.
     */
    public LetStepDefault(Conclusion conclusion, Constraint constraint, InferenceStep premise,
                          TypeInfererLet typeInferer) {
        super(conclusion, constraint, premise, typeInferer);
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
