package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

public class VarStepWithLet extends VarStep {
    /**
     * Initializes a new VarStep with the given values.
     *
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     * @param instantiatedTypeAbs an instantiation of the type abstraction used in this step
     * @param conclusion the conclusion of this step
     * @param constraint the constraint added in this step
     */
    public VarStepWithLet(TypeAbstraction typeAbstractionInPremise, Type instantiatedTypeAbs, Conclusion conclusion,
                          Constraint constraint) {
        super(typeAbstractionInPremise, instantiatedTypeAbs, conclusion, constraint);
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
