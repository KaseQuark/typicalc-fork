package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.BooleanTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.util.Result;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * Parser for lambda terms.
 *
 * @see LambdaTerm
 */
public class LambdaParser {
    /**
     * lexer to translate a String into tokens
     */
    private final LambdaLexer lexer;
    /**
     * Next token to use while parsing.
     * The following invariant holds:
     * When calling a parseX method, token is the first token of X
     * (as opposed to the last token of the previous construct).
     */
    private Token token = new Token(TokenType.EOF, "", -1);

    private static final Set<TokenType> ATOM_START_TOKENS
            = EnumSet.of(TokenType.VARIABLE, TokenType.NUMBER, TokenType.TRUE,
            TokenType.FALSE, TokenType.LEFT_PARENTHESIS, TokenType.LAMBDA, TokenType.LET);

    /**
     * Constructs a parser with the specified String
     *
     * @param term String to parse
     */
    public LambdaParser(String term) {
        this.lexer = new LambdaLexer(term);
    }

    /**
     * Sets token to the next available token.
     */
    private Optional<ParseError> nextToken() {
        Result<Token, ParseError> nextToken = lexer.nextToken();
        if (nextToken.isError()) {
            return Optional.of(nextToken.unwrapError());
        }
        token = nextToken.unwrap();
        return Optional.empty();
    }

    /**
     * Checks that the token type of current token matches the token type given as parameter.
     * If successful, returns that token and advances to the next token.
     * Returns false otherwise.
     *
     * @param type the token type to compare the current token type to
     */
    private Optional<ParseError> expect(TokenType type) {
        Token lastToken = token;
        TokenType current = token.getType();
        Optional<ParseError> error = nextToken();
        if (current != type) {
            return Optional.of(ParseError.UNEXPECTED_TOKEN.withToken(lastToken, lexer.getTerm(),
                    ParseError.ErrorType.TERM_ERROR).expectedType(type));
        }
        return error;
    }

    /**
     * Parses the String given in the constructor as a term.
     *
     * @return the term, or an error
     */
    public Result<LambdaTerm, ParseError> parse() {
        Result<LambdaTerm, ParseError> t = parseTerm(true);
        if (t.isError()) {
            return t;
        }
        Token last = token;
        Optional<ParseError> next = expect(TokenType.EOF);
        if (next.isEmpty()) {
            return t;
        }
        return new Result<>(null,
                (last.getType() == TokenType.EOF ? ParseError.TOO_FEW_TOKENS : ParseError.UNEXPECTED_TOKEN)
                    .withToken(last, lexer.getTerm(), ParseError.ErrorType.TERM_ERROR)
                    .expectedTypes(ATOM_START_TOKENS));
    }

    /**
     * Parses a term.
     *
     * @return the term, or an error
     */
    private Result<LambdaTerm, ParseError> parseTerm(boolean next) {
        if (next) {
            Optional<ParseError> error = nextToken();
            if (error.isPresent()) {
                return new Result<>(null, error.get());
            }
        }
        if (token.getType() == TokenType.EOF) {
            return new Result<>(null, ParseError.TOO_FEW_TOKENS);
        }
        return parseApplication();
    }

    private Result<AbsTerm, ParseError> parseAbstraction() {
        nextToken();
        Result<VarTerm, ParseError> var = parseVar();
        if (var.isError()) {
            return new Result<>(var);
        }
        Optional<ParseError> next = expect(TokenType.DOT);
        if (next.isPresent()) {
            return new Result<>(null, next.get());
        }
        Result<LambdaTerm, ParseError> body = parseTerm(false);
        if (body.isError()) {
            return new Result<>(body);
        }
        return new Result<>(new AbsTerm(var.unwrap(), body.unwrap()));
    }

    /**
     * Parses an application or constructs of higher precedence.
     *
     * @return the term, or an error
     */
    private Result<LambdaTerm, ParseError> parseApplication() {
        Result<LambdaTerm, ParseError> left = parsePart();
        if (left.isError()) {
            return left;
        }
        while (ATOM_START_TOKENS.contains(token.getType())) {
            Result<LambdaTerm, ParseError> atom = parsePart();
            if (atom.isError()) {
                return atom;
            }
            left = new Result<>(new AppTerm(left.unwrap(), atom.unwrap()));
        }
        return left;
    }

    private Result<LetTerm, ParseError> parseLet() {
        Optional<ParseError> error = expect(TokenType.LET);
        if (error.isPresent()) {
            return new Result<>(null, error.get());
        }
        Result<VarTerm, ParseError> var = parseVar();
        if (var.isError()) {
            return new Result<>(var);
        }
        error = expect(TokenType.EQUALS);
        if (error.isPresent()) {
            return new Result<>(null, error.get());
        }
        Result<LambdaTerm, ParseError> def = parseTerm(false);
        if (def.isError()) {
            return new Result<>(def);
        }
        error = expect(TokenType.IN);
        if (error.isPresent()) {
            return new Result<>(null, error.get());
        }
        Result<LambdaTerm, ParseError> body = parseTerm(false);
        if (body.isError()) {
            return new Result<>(body);
        }
        return new Result<>(new LetTerm(var.unwrap(), def.unwrap(), body.unwrap()));
    }

    /**
     * Parses a part of an expression (variable, constants, abstraction, let).
     *
     * @return the term
     */
    private Result<LambdaTerm, ParseError> parsePart() {
        switch (token.getType()) {
            case VARIABLE:
                Result<VarTerm, ParseError> var = parseVar();
                return new Result<>(var.unwrap()); // variable token can always be parsed
            case LAMBDA:
                return new Result<>(parseAbstraction());
            case LET:
                return new Result<>(parseLet());
            case TRUE:
            case FALSE:
                String boolText = token.getText();
                boolean b = Boolean.parseBoolean(boolText);
                Optional<ParseError> error = nextToken();
                if (error.isEmpty()) {
                    return new Result<>(new BooleanTerm(b));
                }
                return new Result<>(null, error.get());
            case NUMBER:
                String number = token.getText();
                int n;
                try {
                    n = Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    return new Result<>(null, ParseError.UNEXPECTED_CHARACTER.withToken(
                            token, lexer.getTerm(), ParseError.ErrorType.TERM_ERROR));
                }
                error = nextToken();
                if (error.isEmpty()) {
                    return new Result<>(new IntegerTerm(n));
                }
                return new Result<>(null, error.get());
            default:
                error = expect(TokenType.LEFT_PARENTHESIS);
                if (error.isPresent()) {
                    return new Result<>(null, error.get());
                }
                Result<LambdaTerm, ParseError> term = parseTerm(false);
                error = expect(TokenType.RIGHT_PARENTHESIS);
                if (error.isEmpty()) {
                    return term;
                }
                return new Result<>(null, error.get());
        }
    }

    private Result<VarTerm, ParseError> parseVar() {
        String s = token.getText();
        Optional<ParseError> next = expect(TokenType.VARIABLE);
        if (next.isEmpty()) {
            return new Result<>(new VarTerm(s));
        }
        return new Result<>(null, next.get());
    }
}
