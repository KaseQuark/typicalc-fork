package edu.kit.typicalc.model.parser;

/**
 * Errors that can occur when parsing a lambda term.
 *
 * @see LambdaLexer
 * @see LambdaParser
 */
public enum ParseError {

    /**
     * the lambda term didn't meet the specified syntax
     */
    UNEXPECTED_TOKEN,

    /**
     * some tokens were required, but not provided
     */
    TOO_FEW_TOKENS,

    /**
     * the string contained a character not allowed in that context
     */
    UNEXPECTED_CHARACTER;

    private Token cause = new Token(Token.TokenType.EOF, "", -1);

    /**
     * Attach a token to this error.
     *
     * @param cause the token that caused the error
     * @return this object
     */
    public ParseError withToken(Token cause) {
        this.cause = cause;
        return this;
    }

    /**
     * @return the token associated with this error, or null if none
     */
    public Token getCause() { // TODO: document
        return cause;
    }

    ParseError() {

    }
}
