package edu.kit.typicalc.model;

/**
 * A substitution specifies that some type should be replaced by a different type.
 */
public class Substitution {
    /**
     * Creates a new substitution using a type variable a and a type b. When the substitution is applied to a type,
     * all occurring instances of a should be substituted with b.
     *
     * @param a variable to be replaced
     * @param b type to insert
     */
    public Substitution(TypeVariable a, Type b) {
        // TODO
    }

    /**
     * @return the type variable
     */
    public TypeVariable getVariable() {
        return null;
        // TODO
    }

    /**
     * @return the replacement type
     */
    Type getType() {
        return null;
        // TODO
    }
}
