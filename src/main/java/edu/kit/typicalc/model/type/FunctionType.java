package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

/**
 * Models the type of an abstraction/function.
 */
public class FunctionType extends Type {

    private Type parameter;
    private Type output;
    /**
     * Initializes a new FunctionType with the given parameter and output types.
     * @param parameter the type of this function’s parameter
     * @param output the type of this function’s output
     */
    public FunctionType(Type parameter, Type output) {
        this.parameter = parameter;
        this.output = output;
    }

    /**
     * Checks whether some type occurs in the parameter or output of this function.
     * @param x the type to look for
     * @return whether the specified type occurs in this type
     */
    @Override
    public boolean contains(Type x) {
        return (contains(parameter) || contains(output));
    }

    /**
     * Substitutes a type variable for a different type.
     * @param a  the type variable to replace
     * @param b the type to insert
     * @return a new FunctionType that is created by substituting a and b in the parameter
     * and output type
     */
    @Override
    public FunctionType substitute(TypeVariable a, Type b) {
        boolean first = false;
        boolean second = false;
        if (this.parameter.equals(a)) {
            first = true;
        }
        if (this.output.equals(a)) {
             second = true;
        }
        if (first && second) {
            return new FunctionType(b, b);
        } else if (first) {
            return new FunctionType(b, output);
        } else if (second) {
            return new FunctionType(parameter, b);
        } else {
            return this;
        }
    }

    /**
     * Accepts a visitor.
     * @param typeVisitor the visitor that wants to visit this
     */
    @Override
    public void accept(TypeVisitor typeVisitor) {
        typeVisitor.visit(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with
     * another. This method uses the constrainEqualToFunction method on the other
     * type.
     * @param type  the other type
     * @return unification steps necessary, or an error if that is impossible
     */
    public Result<UnificationActions, UnificationError> constrainEqualTo(Type type) {
        //TODO
        return null;
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * function type.
     * @param type the function type
     * @return unification steps necessary, or an error if that is impossible
     */
    public Result<UnificationActions, UnificationError> constrainEqualToFunction(Type type) {
        //TODO
        return null;
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * named type.
     * @param type the named type
     * @return unification steps necessary, or an error if that is impossible
     */
    public Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type) {
        //TODO
        return null;
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * type variable.
     * @param type the type variable
     * @return the unification steps necessary, or an error if that is impossible
     */
    public Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type) {
        //TODO
        return null;
    }

    /**
     * Getter for output
     * @return output
     */
    public Type getOutput() {
        return output;
    }

    /**
     * Getter for parameter
     * @return parameter
     */
    public Type getParameter() {
        return parameter;
    }
}
