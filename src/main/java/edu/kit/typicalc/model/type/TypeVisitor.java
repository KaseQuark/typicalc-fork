package edu.kit.typicalc.model.type;

/**
 * Can be implemented to process Type objects.
 */
public interface TypeVisitor {
    /**
     * Visit a named type.
     *
     * @param named the type to be visited
     */
    void visit(NamedType named);

    /**
     * Visit a type variable
     *
     * @param variable the variable to be visited
     */
    void visit(TypeVariable variable);

    /**
     * Visit a function.
     *
     * @param function the function to be visited
     */
    void visit(FunctionType function);
}
