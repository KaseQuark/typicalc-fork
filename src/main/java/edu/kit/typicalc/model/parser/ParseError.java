package edu.kit.typicalc.model.parser;

public enum ParseError {

    /**
     *  the lambda term didn't meet the specified syntax
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
