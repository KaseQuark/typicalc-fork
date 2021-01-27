package edu.kit.typicalc.model.parser;

/**
 * A token of the Prolog language.
 */
public class Token {
    /**
     * Used to distinguish what kind of token we have.
     * Most of them stand for exactly one character.
     * VARIABLE and NUMBER have a regular expression associated with them.
     * EOF is a special token to indicate that the end of file is reached.
     */
    enum TokenType {
        LAMBDA, // λ or a backslash
        VARIABLE, // [a-z][a-zA-Z0-9]* except "let" or "in" or constants
        LET, // let
        IN, // in
        TRUE, // true
        FALSE, // false
        NUMBER, // [0-9]+
        LP, // (
        RP, // )
        DOT, // .
        EQ, // =
        EOF // pseudo token if end of file is reached
    }

    /**
     * token type of this Token
     */
    private final TokenType type;
    /**
     * the text of this token in the source code
     */
    private final String text;
    private final int pos;

    /**
     * Constructs a token.
     * @param type the token type
     * @param text text of this token in the source code
     * @param pos position this token begins
     */
    public Token(TokenType type, String text, int pos) {
        this.type = type;
        this.text = text;
        this.pos = pos;
    }

    /**
     * Returns the token type
     * @return token type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Returns the text of this token in the source code
     * @return text of this token in the source code
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the position this token is in
     * @return position this token is in
     */
    public int getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return type + "(\"" + text + "\")";
    }
}
