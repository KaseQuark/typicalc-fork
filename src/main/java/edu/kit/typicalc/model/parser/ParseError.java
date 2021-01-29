package edu.kit.typicalc.model.parser;

public enum ParseError {

    /**
     *  the lambda term didn't meet the specified syntax
     */
    UNEXPECTED_TOKEN,

    /**
     * some tokens were remaining after parsing a full lambda term
     */
    TOO_MANY_TOKENS,

    /**
     * some tokens were required, but not provided
     */
    TOO_FEW_TOKENS,

    /**
     * the string contained a character not allowed in that context
     */
    UNEXPECTED_CHARACTER
}
