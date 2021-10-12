package edu.kit.typicalc.model;

import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;
import java.util.Objects;

/**
 * Models the conclusion of an inference rule and consists of a list of type assumptions, a lambda term and a type.
 * This class is used in inference steps to represent the conclusion of that specific application of the inference rule.
 */
public class Conclusion {

    private final Map<VarTerm, TypeAbstraction> typeAssumptions;
    private final LambdaTerm lambdaTerm;
    private final Type type;

    /**
     * Initializes a new Conclusion with the given type assumptions, lambda term and type.
     *
     * @param typeAssumptions the type assumptions used in the conclusion
     * @param lambdaTerm      the lambda term in the conclusion
     * @param type            the type assigned to the lambda term in the conclusion
     */
    public Conclusion(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm, Type type) {
        this.typeAssumptions = typeAssumptions;
        this.lambdaTerm = lambdaTerm;
        this.type = type;
    }

    /**
     * Returns the type assumptions used in the conclusion.
     *
     * @return the type assumptions used in the conclusion
     */
    public Map<VarTerm, TypeAbstraction> getTypeAssumptions() {
        return typeAssumptions;
    }

    /**
     * Returns the lambda term in the conclusion.
     *
     * @return the lambda term in the conclusion
     */
    public LambdaTerm getLambdaTerm() {
        return lambdaTerm;
    }

    /**
     * Returns the type assigned to the lambda term in the conclusion.
     *
     * @return the type assigned to the lambda term in the conclusion
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conclusion that = (Conclusion) o;
        return typeAssumptions.equals(that.typeAssumptions)
                && lambdaTerm.equals(that.lambdaTerm) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeAssumptions, lambdaTerm, type);
    }
}
