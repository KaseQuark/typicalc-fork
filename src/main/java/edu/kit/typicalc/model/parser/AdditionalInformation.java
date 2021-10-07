package edu.kit.typicalc.model.parser;

/**
 * Can be attached to a parse error to indicate a specific error condition.
 *
 * @see ParseError
 */
public enum AdditionalInformation {
    /**
     * Duplicate VarType after Universal Quantifier.
     */
    DUPLICATETYPE,

    /**
     * An integer constant overflows the storage type.
     */
    INT_OVERFLOW
}
