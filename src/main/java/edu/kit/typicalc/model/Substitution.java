package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;

/**
 * A substitution specifies that some type should be replaced by a different type.
 */
public class Substitution {

    private final TypeVariable a;
    private final Type b;

    /**
     * Creates a new substitution using a type variable a and a type b. When the substitution is applied to a type,
     * all occurring instances of a should be substituted with b.
     *
     * @param a variable to be replaced
     * @param b type to insert
     */
    public Substitution(TypeVariable a, Type b) {
        // TODO: null checks?
        this.a = a;
        this.b = b;
    }

    /**
     * @return the type variable
     */
    public TypeVariable getVariable() {
        return a;
    }

    /**
     * @return the replacement type
     */
    Type getType() {
        return b;
    }
}
