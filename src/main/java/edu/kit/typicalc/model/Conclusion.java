package edu.kit.typicalc.model;

/**
 * Models the conclusion of an inference rule and consists of a list of type assumptions, a lambda term and a type.
 * This class is used in inference steps to represent the conclusion of that specific application of the inference rule.
 */
public class Conclusion {

    /**
     * Initializes a new Conclusion with the given type assumptions, lambda term and type.
     *
     * @param typeAssumptions the type assumptions used in the conclusion
     * @param lambdaTerm the lambda term in the conclusion
     * @param type the type assigned to the lambda term in the conclusion
     */
    protected Conclusion(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm, Type type) {
        // TODO
    }

    /**
     * @return the type assumptions used in the conclusion
     */
    public Map<VarTerm, TypeAbstraction> getTypeAssumptions() {
        return null;
        // TODO
    }

    /**
     * @return the lambda term in the conclusion
     */
    public LambdaTerm getLambdaTerm() {
        return null;
        // TODO
    }

    /**
     * @return the type assigned to the lambda term in the conclusion
     */
    public Type getType() {
        return null;
        // TODO
    }
}
