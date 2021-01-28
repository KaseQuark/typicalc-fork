package edu.kit.typicalc.model.parser;

public enum ParseError {

    /**
     *  the lambda term didn't meet the specified syntax
     */
    UNEXPECTED_TOKEN,
    
    TOO_MANY_TOKENS,

    /**
     * the String contained a character not allowed in that place
     */
    UNEXPECTED_CHARACTER
}
