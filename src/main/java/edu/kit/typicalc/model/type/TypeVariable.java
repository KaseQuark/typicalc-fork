package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

import java.util.*;

/**
 * Models a type variable
 */
public class TypeVariable extends Type implements Comparable<TypeVariable> {

    private final TypeVariableKind kind;
    private final int index;

    /**
     * Initializes a new TypeVariable with the given index.
     *
     * @param kind the kind of type variable
     * @param index the index of this variable
     */
    public TypeVariable(TypeVariableKind kind, int index) {
        this.kind = kind;
        this.index = index;
    }

    /**
     * Returns the kind of the type variable.
     * @return the variable's kind
     */
    public TypeVariableKind getKind() {
        return kind;
    }

    /**
     * Returns the index of the type variable as an integer
     * @return the variable's index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Checks whether some type occurs in this type.
     * @param x the type to look for
     * @return whether the specified type is equal to this type
     */
    @Override
    public boolean contains(Type x) {
        return this.equals(x);
    }

    @Override
    public Set<TypeVariable> getFreeTypeVariables() {
        return new HashSet<>(Collections.singletonList(this));
    }

    /**
     * Substitutes a type variable for a different type.
     * @param a the type to replace
     * @param b the type to insert
     * @return itself, or b if a is equal to this object
     */
    @Override
    public Type substitute(TypeVariable a, Type b) {
        if (this.equals(a)) {
        return b;
        } else {
            return this;
        }
    }

    /**
     * Accepts a visitor.
     * @param typeVisitor the visitor that wants to visit this
     */
    public void accept(TypeVisitor typeVisitor) {
        typeVisitor.visit(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with
     * another. This method uses the constrainEqualToVariable method on the other
     * type.
     * @param type  the other type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualTo(Type type) {
        return type.constrainEqualToVariable(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * function type.
     * @param type the function type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToFunction(FunctionType type) {
        return UnificationUtil.functionVariable(type, this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * named type.
     * @param type the named type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type) {
        return UnificationUtil.variableNamed(this, type);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * type variable.
     * @param type the type variable
     * @return the unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type) {
        return UnificationUtil.variableVariable(type, this);
    }

    @Override
    public String toString() {
        return "TypeVariable[" + kind + "_" + index + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeVariable that = (TypeVariable) o;
        return index == that.index && kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, index);
    }

    @Override
    public int compareTo(TypeVariable var) {
        return Comparator.comparing(TypeVariable::getKind)
                .thenComparing(TypeVariable::getIndex)
                .compare(this, var);
    }
}
