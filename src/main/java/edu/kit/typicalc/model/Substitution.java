package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Substitution[" + a + " => " + b + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Substitution that = (Substitution) o;
        return a.equals(that.a) && b.equals(that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
