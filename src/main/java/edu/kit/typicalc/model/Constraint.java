package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;

/**
 * Constrains two types to be equal.
 */
public class Constraint {

    private final Type a;
    private final Type b;

    /**
     * Creates a new constraint using the two types.
     *
     * @param a first type
     * @param b second type
     */
    public Constraint(Type a, Type b) {
        this.a = a;
        this.b = b;
    }

    /**
     * @return the first type
     */
    public Type getFirstType() {
        return a;
    }

    /**
     * @return the second type
     */
    public Type getSecondType() {
        return b;
    }

}
