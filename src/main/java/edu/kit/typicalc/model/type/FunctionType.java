package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

import java.util.Objects;

/**
 * Models the type of an abstraction/function.
 */
public class FunctionType extends Type {

    private final Type parameter;
    private final Type output;
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
        return (parameter.contains(x) || output.contains(x));
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
        return new FunctionType(parameter.substitute(a, b), output.substitute(a, b));
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
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualTo(Type type) {
        return type.constrainEqualToFunction(this);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * function type.
     * @param type the function type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToFunction(FunctionType type) {
        return UnificationUtil.functionFunction(this, type);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * named type.
     * @param type the named type
     * @return unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type) {
        return UnificationUtil.functionNamed(this, type);
    }

    /**
     * Computes the necessary constraints (and substitution) to unify this type with a
     * type variable.
     * @param type the type variable
     * @return the unification steps necessary, or an error if that is impossible
     */
    @Override
    public Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type) {
        return UnificationUtil.functionVariable(this, type);
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

    @Override
    public String toString() {
        return "Function[" + parameter + " -> " + output + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionType that = (FunctionType) o;
        return Objects.equals(parameter, that.parameter) && Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, output);
    }
}
