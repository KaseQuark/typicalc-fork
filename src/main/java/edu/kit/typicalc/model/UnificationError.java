package edu.kit.typicalc.model;

/**
 * Errors that can occur during unification.
 *
 * @see Unification
 * @see UnificationStep
 */
public enum UnificationError {
    /**
     * Unification would lead to an infinite type.
     */
    INFINITE_TYPE,
    /**
     * Some {@link edu.kit.typicalc.model.type.NamedType} is not equal to another type.
     */
    DIFFERENT_TYPES
}
