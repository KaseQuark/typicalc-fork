package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

/**
 * Models one step of the inference tree where the variable rule is applied and
 * let polymorphism is not used in the entire tree.
 */
public class VarStepDefault extends VarStep {
    /**
     * Initializes a new VarStep with the given values.
     *
     * @param typeAbstractionInPremise the type abstraction in the premise of this step
     * @param instantiatedTypeAbs      an instantiation of the type abstraction used in this step
     * @param conclusion               the conclusion of this step
     * @param constraint               the constraint added in this step
     * @param stepIndex step number
     */
    public VarStepDefault(TypeAbstraction typeAbstractionInPremise, Type instantiatedTypeAbs, Conclusion conclusion,
                          Constraint constraint, int stepIndex) {
        super(typeAbstractionInPremise, instantiatedTypeAbs, conclusion, constraint, stepIndex);
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
