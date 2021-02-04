package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.type.NamedType;

import java.util.Objects;

/**
 * Representation of a constant boolean lambda term: either false or true.
 */
public class BooleanTerm extends ConstTerm {
    private final boolean value;

    /**
     * Initializes a new boolean lambda term with the given value.
     * @param value true or false
     */
    public BooleanTerm(boolean value) {
        this.value = value;
    }

    /**
     * Returns the value of the boolean constant term.
     * @return the value of the term
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public NamedType getType() {
        return NamedType.BOOLEAN;
    }

    @Override
    public void accept(TermVisitor termVisitor) {
        termVisitor.visit(this);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BooleanTerm that = (BooleanTerm) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
