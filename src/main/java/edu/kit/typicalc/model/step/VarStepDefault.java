package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.TypeAbstraction;

public class VarStepDefault extends VarStep {
    /**
     * Initializes a new VarStep with the given values.
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     */
    public VarStepDefault(TypeAbstraction typeAbstractionInPremise, Conclusion conclusion, Constraint constraint) {
        super(typeAbstractionInPremise, conclusion, constraint);
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
