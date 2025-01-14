package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;

import java.util.Objects;

/**
 * Constrains two types to be equal.
 */
public class Constraint {

    private final Type a;
    private final Type b;
    private int stepIndex = -1;

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

    /**
     * Set the number of the step that caused this constraint.
     * @param index step number
     */
    public void setStepIndex(int index) {
        this.stepIndex = index;
    }

    /**
     * @return the step index
     */
    public int getStepIndex() {
        return stepIndex;
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
