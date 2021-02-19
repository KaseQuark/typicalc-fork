package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;

import java.util.Objects;

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
     * Returns the first type
     *
     * @return the first type
     */
    public Type getFirstType() {
        return a;
    }

    /**
     * Returns the second type
     *
     * @return the second type
     */
    public Type getSecondType() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Constraint that = (Constraint) o;
        return (a.equals(that.a) && b.equals(that.b))
                || (a.equals(that.b) && b.equals(that.a));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
