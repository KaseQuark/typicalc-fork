package edu.kit.typicalc.model.parser;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Errors that can occur when parsing a lambda term or type assumption.
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

    private Optional<Token> cause = Optional.empty();
    private Optional<Collection<Token.TokenType>> needed = Optional.empty();
    private String term = "";
    private char wrongChar = '\0';
    private char correctChar = '\0';
    private int position = -1;

    /**
     * Attach a token to this error.
     *
     * @param cause the token that caused the error
     * @param term the term that is parsed
     * @return this object
     */
    public ParseError withToken(Token cause, String term) {
        this.cause = Optional.of(cause);
        this.term = term;
        return this;
    }

    /**
     * Attach an expected token type to this error.
     *
     * @param needed the required token type
     * @return this object
     */
    public ParseError expectedType(Token.TokenType needed) {
        this.needed = Optional.of(List.of(needed));
        return this;
    }

    /**
     * Attach expected token types to this error.
     *
     * @param needed the possible token types
     * @return this object
     */
    public ParseError expectedTypes(Collection<Token.TokenType> needed) {
        this.needed = Optional.of(needed);
        return this;
    }

    /**
     * Attach an expected character to this error.
     *
     * @param expected the correct character
     * @return this object
     */
    public ParseError expectedCharacter(char expected) {
        this.correctChar = expected;
        return this;
    }

    /**
     * Attach a character and position to this error.
     *
     * @param cause the character
     * @param position it's position
     * @param term the term that is parsed
     * @return this object
     */
    public ParseError withCharacter(char cause, int position, String term) {
        this.wrongChar = cause;
        this.position = position;
        this.term = term;
        return this;
    }

    /**
     * @return the token associated with this error
     */
    public Optional<Token> getCause() {
        return cause;
    }

    /**
     * @return the expected/possible token(s) if this error is UNEXPECTED_TOKEN
     */
    public Optional<Collection<Token.TokenType>> getExpected() {
        return needed;
    }

    /**
     * @return the wrong character if this error is UNEXPECTED_CHARACTER ('\0' otherwise)
     */
    public char getWrongCharacter() {
        return wrongChar;
    }

    /**
     * @return the correct character if this error is UNEXPECTED_CHARACTER ('\0' if unknown)
     */
    public char getExpectedCharacter() {
        return correctChar;
    }

    /**
     * @return the character position if this error is UNEXPECTED_CHARACTER
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the input term
     */
    public String getTerm() {
        return term;
    }

    ParseError() {

    }
}
