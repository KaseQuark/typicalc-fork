package edu.kit.typicalc.model.step;

/**
 * Models one step of the inference tree. Depending on the inference rule that is applied in
 * a step, different subclasses of InferenceStep should be used. A step always contains the
 * Constraint that is added to the set of constraints of the type inference algorithm because
 * of this step and a Conclusion. The subclasses vary in the premise(s) they contain.
 */
public abstract class InferenceStep {
// todo

    /**
     * Accepts a visitor.
     * @param stepVisitor the visitor that wants to visit this object
     */
    public abstract void accept(StepVisitor stepVisitor);

}



