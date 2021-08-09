package edu.kit.typicalc.model.parser;

/**
 * Attached to a {@link ParseError} to signify what kind of input is expected.
 */
public enum ExpectedInput {
    /**
     * Any kind of lambda term.
     */
    TERM,

    /**
     * Any kind of type.
     */
    TYPE,
    /**
     * // t[0-9]+
     */
    VARTYPE
}
