package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.util.Result;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class lexes a term given as String into tokens.
 * Tokens are lexed all at once (to catch errors early),
 * and passed to the parser on demand.
 */
public class LambdaLexer {
    /**
     * the given term as a String
     */
    private final String term;
    private final ParseError.ErrorType errorType;
    /**
     * current position in the term
     */
    private int pos = 0;
    private final Result<Deque<Token>, ParseError> result;

    /**
     * Constructs a lexer that lexes the given term
     *
     * @param term the term to lex
     */
    public LambdaLexer(String term, ParseError.ErrorType errorType) {
        this.term = term;
        this.errorType = errorType;
        Deque<Token> tokens = new ArrayDeque<>();
        while (true) {
            Result<Token, ParseError> token = parseNextToken();
            if (token.isError()) {
                result = new Result<>(null, token.unwrapError());
                return;
            }
            Token value = token.unwrap();
            tokens.add(value);
            if (value.getType() == TokenType.EOF) {
                break;
            }
        }
        result = new Result<>(tokens);
    }

    /**
     * Advances the current char to the next char in the term.
     */
    private void advance() {
        pos += 1;
    }

    /**
     * Returns the next token and advances the lexer position.
     *
     * @return the next token
     */
    public Result<Token, ParseError> nextToken() {
        if (result.isError()) {
            return new Result<>(null, result.unwrapError());
        }
        Deque<Token> tokens = result.unwrap();
        if (!tokens.isEmpty()) {
            Token token = tokens.removeFirst();
            return new Result<>(token);
        } else {
            return new Result<>(new Token(TokenType.EOF, "", term, term.length()));
        }
    }

    private Result<Token, ParseError> parseNextToken() {
        while (pos < term.length() && Character.isWhitespace(term.charAt(pos))) {
            advance();
        }
        if (pos >= term.length()) {
            // term ended, return EOF
            return new Result<>(new Token(TokenType.EOF, "", term, pos));
        }
        Token t;
        char c = term.charAt(pos);
        switch (c) {
            case '-':
                if (pos + 1 < term.length()) {
                    if (term.charAt(pos + 1) == '>') {
                        t = new Token(TokenType.ARROW, "->", term, pos);
                        advance();
                        advance();
                        return new Result<>(t);
                    } else {
                        return new Result<>(null, ParseError.unexpectedCharacter2(
                                term.charAt(pos + 1), pos + 1, term, errorType));
                    }
                } else {
                    return new Result<>(null,
                            ParseError.unexpectedCharacter2(' ', term.length(), term, errorType));
                }
            // bunch of single-character tokens
            case '.':
                t = new Token(TokenType.DOT, ".", term, pos);
                advance();
                return new Result<>(t);
            case ',':
                t = new Token(TokenType.COMMA, ",", term, pos);
                advance();
                return new Result<>(t);
            case ':':
                t = new Token(TokenType.COLON, ":", term, pos);
                advance();
                return new Result<>(t);
            case '(':
                t = new Token(TokenType.LEFT_PARENTHESIS, "(", term, pos);
                advance();
                return new Result<>(t);
            case ')':
                t = new Token(TokenType.RIGHT_PARENTHESIS, ")", term, pos);
                advance();
                return new Result<>(t);
            case '=':
                t = new Token(TokenType.EQUALS, "=", term, pos);
                advance();
                return new Result<>(t);
            case '\\':
            case 'λ':
                t = new Token(TokenType.LAMBDA, Character.toString(c), term, pos);
                advance();
                return new Result<>(t);
            case '!':
            case '∀':
                t = new Token(TokenType.UNIVERSAL_QUANTIFIER, Character.toString(c), term, pos);
                advance();
                return new Result<>(t);
            default:
                return parseAtomToken(c);
        }
    }

    private Result<Token, ParseError> parseAtomToken(char c) {
        // only allow ascii characters in variable names
        if (Character.isLetter(c) && (int) c < 128) {
            int startPos = pos;
            // identifier or keyword
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(term.charAt(pos));
                advance();
            } while (pos < term.length() && Character.isLetterOrDigit(term.charAt(pos))
                    && (int) term.charAt(pos) < 128);
            if (pos < term.length() && (int) term.charAt(pos) >= 128) {
                return new Result<>(null, ParseError.unexpectedCharacter2(term.charAt(pos), pos, term, errorType));
            }
            String s = sb.toString();
            TokenType type;
            switch (s) {
                case "let":
                    type = TokenType.LET;
                    break;
                case "in":
                    type = TokenType.IN;
                    break;
                case "true":
                    type = TokenType.TRUE;
                    break;
                case "false":
                    type = TokenType.FALSE;
                    break;
                default:
                    type = TokenType.VARIABLE;
                    break;
            }
            return new Result<>(new Token(type, sb.toString(), term, startPos));
        } else if (Character.isDigit(c)) {
            int startPos = pos;
            // number literal
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(term.charAt(pos));
                advance();
            } while (pos < term.length() && Character.isDigit(term.charAt(pos)));
            return new Result<>(new Token(TokenType.NUMBER, sb.toString(), term, startPos));
        } else {
            return new Result<>(null, ParseError.unexpectedCharacter2(c, pos, term,
                    errorType));
        }
    }

    /**
     * @return the term that is parsed
     */
    public String getTerm() {
        return term;
    }
}