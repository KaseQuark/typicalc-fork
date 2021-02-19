package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Models a simple named type.
 */
public class NamedType extends Type {
    /**
     * boolean type
     */
    public static final NamedType BOOLEAN = new NamedType("boolean");
    /**
     * int type
     */
    public static final NamedType INT = new NamedType("int");

    private final String name;

    /**
     * Initializes a new NamedType with the given name.
     *
     * @param name the name of this type
     */
    public NamedType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the named type.
     *
     * @return the name of this type
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether some type occurs in this type.
     *
     * @param x the type to look for
     * @return whether the specified type is equal to this type
     */
    public boolean contains(Type x) {
        return this.equals(x);
    }

    @Override
    public Set<TypeVariable> getFreeTypeVariables() {
        return new HashSet<>();
    }

    /**
     * Substitutes a type variable for a different type.
     *
     * @param a the type to replace
     * @param b the type to insert
     * @return itself, or b if a is equal to this object
     */
    @Override
    public Type substitute(TypeVariable a, Type b) {
        return this;
    }

    /**
     * Accepts a visitor.
     *
     * @param typeVisitor the visitor that wants to visit this
     */
    @Override
    public void accept(TypeVisitor typeVisitor) {
        typeVisitor.visit(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with
     * another. This method uses the constrainEqualToNamedType method on the other
     * type.
     *
     * @param type the other type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualTo(Type type) {
        return type.constrainEqualToNamedType(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * function type.
     *
     * @param type the function type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToFunction(FunctionType type) {
        return UnificationUtil.functionNamed(type, this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * named type.
     *
     * @param type the named type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type) {
        return UnificationUtil.namedNamed(this, type);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * type variable.
     *
     * @param type the type variable
     * @return the unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type) {
        return UnificationUtil.variableNamed(type, this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NamedType namedType = (NamedType) o;
        return Objects.equals(name, namedType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
