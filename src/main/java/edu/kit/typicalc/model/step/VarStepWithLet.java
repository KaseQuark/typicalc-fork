package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * Models one step of the inference tree where the variable rule is applied and at least one
 * let rule is applied in the entire tree. It additionally constrains a type that is obtained when
 * instantiating the type abstraction in the premise of the step.
 */
public class VarStepWithLet extends VarStep {
    /**
     * Initializes a new VarStep with the given values.
     *
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     * @param instantiatedTypeAbs      an instantiation of the type abstraction used in this step
     * @param conclusion               the conclusion of this step
     * @param constraint               the constraint added in this step
     */
    public VarStepWithLet(TypeAbstraction typeAbstractionInPremise, Type instantiatedTypeAbs, Conclusion conclusion,
                          Constraint constraint) {
        super(typeAbstractionInPremise, instantiatedTypeAbs, conclusion, constraint);
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
