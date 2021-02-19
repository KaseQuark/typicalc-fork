package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.type.NamedType;

import java.util.Objects;

/**
 * Representation of a constant integer lambda term: e.g. -1, 0 or 16.
 */
public class IntegerTerm extends ConstTerm {
    private final int value;

    /**
     * Initializes a new integer lambda term with the given value.
     *
     * @param value an integer
     */
    public IntegerTerm(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the integer constant term.
     *
     * @return the value of the term
     */
    public int getValue() {
        return value;
    }

    @Override
    public NamedType getType() {
        return NamedType.INT;
    }

    @Override
    public void accept(TermVisitor termVisitor) {
        termVisitor.visit(this);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntegerTerm that = (IntegerTerm) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
