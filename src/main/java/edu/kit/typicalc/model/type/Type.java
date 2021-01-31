package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.Substitution;
import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

/**
 * Models the type of a lambda term.
 */
public abstract class Type {
    /**
     * Checks whether some type occurs in this type.
     * @param x the type to look for
     * @return whether the specified type occurs in this type
     */
    public abstract boolean contains(Type x);

    /**
     * Substitutes a type Variable for a different type.
     * @param a the type to replace
     * @param b the type to insert
     * @return a Type that is created by replacing a with b
     */
    public abstract Type substitute(TypeVariable a, Type b);

    /**
     * Applies the given substitution to the type.
     * @param substitution the substitution to apply to the type
     * @return the substituted type
     */
    public Type substitute(Substitution substitution) {
        return substitute(substitution.getVariable(), substitution.getType());
    }

    /**
     * Accepts a visitor
     * @param typeVisitor the visitor that wants to visit this
     */
    public abstract void accept(TypeVisitor typeVisitor);

    /**
     * Computes the necessary constraints (and substitution) to unify this type with
     * another type.
     * @param type  the other type
     * @return unification steps necessary, or an error if that is impossible
     */
    public abstract Result<UnificationActions, UnificationError> constrainEqualTo(Type type);

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * function type.
     * @param type the function type
     * @return unification steps necessary, or an error if that is impossible
     */
    public abstract Result<UnificationActions, UnificationError> constrainEqualToFunction(FunctionType type);

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * named type.
     * @param type the named type
     * @return unification steps necessary, or an error if that is impossible
     */
    public abstract Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type);

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * type variable.
     * @param type the type variable
     * @return the unification steps necessary, or an error if that is impossible
     */
    public abstract Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type);
}
