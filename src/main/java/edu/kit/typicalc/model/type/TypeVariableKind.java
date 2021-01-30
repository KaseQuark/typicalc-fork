package edu.kit.typicalc.model.type;

/**
 * Describes the kind of type variable (the context it was generated / it is used in).
 */
public enum TypeVariableKind {

    /**
     * Used if the type variable was created from user input.
     */
    USER_INPUT,

    /**
     * Used if the type variable was generated while building the proof tree.
     */
    TREE,

    /**
     * Used if the type variable was generated for a type assumption of a free variable in the input lambda term.
     */
    GENERATED_TYPE_ASSUMPTION
}
