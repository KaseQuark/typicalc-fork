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
    private Token token;

    private static final Set<TokenType> ATOM_START_TOKENS
            = EnumSet.of(TokenType.VARIABLE, TokenType.NUMBER, TokenType.TRUE,
            TokenType.FALSE, TokenType.LP);

    /**
     * Constructs a parser with the specified String
     * @param term String to parse
     */
    public LambdaParser(String term) {
        this.lexer = new LambdaLexer(term);
        nextToken();
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
     * @param type the token type to compare the current token type to
     */
    private boolean expect(TokenType type) {
        TokenType current = token.getType();
        nextToken(); // TODO: Fehlerbehandlung
        return current == type;
    }

    /**
     * Parses the String given in the constructor as a term.
     * @return the term given by the String
     */
    public Result<LambdaTerm, ParseError> parse() {
        Result<LambdaTerm, ParseError> t = parseTerm();
        if (!expect(TokenType.EOF)) {
            return new Result<>(null, ParseError.TOO_MANY_TOKENS);
        }
        return t;
    }

    /**
     * Parses a term.
     * @return the term, or an error
     */
    private Result<LambdaTerm, ParseError> parseTerm() {
        switch (token.getType()) {
            case LAMBDA:
                Result<AbsTerm, ParseError> abs = parseAbstraction();
                return new Result<>(abs.unwrap(), abs.unwrapError());
            case LET:
                Result<LetTerm, ParseError> let = parseLet();
                return new Result<>(let.unwrap(), let.unwrapError());
            default:
                return parseApplication();
        }
    }

    private Result<AbsTerm, ParseError> parseAbstraction() {
        nextToken();
        Result<VarTerm, ParseError> var = parseVar();
        if (!expect(TokenType.DOT)) {
            return new Result<>(null, ParseError.UNEXPECTED_TOKEN);
        }
        Result<LambdaTerm, ParseError> body = parseTerm();
        // TODO: Fehlerbehandlung
        return new Result<>(new AbsTerm(var.unwrap(), body.unwrap()));
    }

    /**
     * Parses an application or constructs of higher precedence.
     * @return the term, or an error
     */
    private Result<LambdaTerm, ParseError> parseApplication() {
        LambdaTerm left = parseAtom().unwrap(); // TODO: Fehlerbehandlung
        while (ATOM_START_TOKENS.contains(token.getType())) {
            LambdaTerm atom = parseAtom().unwrap(); // TODO: Fehlerbehandlung
            left = new AppTerm(left, atom);
        }
        return new Result<>(left);
    }

    private Result<LetTerm, ParseError> parseLet() {
        // TODO: Fehlerbehandlung
        expect(TokenType.LET);
        VarTerm var = parseVar().unwrap();
        expect(TokenType.EQ);
        LambdaTerm def = parseTerm().unwrap();
        expect(TokenType.IN);
        LambdaTerm body = parseTerm().unwrap();
        return new Result<>(new LetTerm(var, def, body));
    }

    /**
     * Parses an atom (variable or number) or a parenthesised expression.
     * @return the term
     */
    private Result<LambdaTerm, ParseError> parseAtom() {
        switch (token.getType()) {
            case VARIABLE:
                Result<VarTerm, ParseError> var = parseVar();
                return new Result<>(var.unwrap(), var.unwrapError());
            case NUMBER:
                String number = token.getText();
                int n;
                try {
                    n = Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    return new Result<>(null, ParseError.UNEXPECTED_CHARACTER);
                }
                nextToken();
                return new Result<>(new IntegerTerm(n));
            case TRUE:
            case FALSE:
                String boolText = token.getText();
                boolean b = Boolean.parseBoolean(boolText);
                nextToken();
                return new Result<>(new BooleanTerm(b));
            default:
                expect(TokenType.LP);
                Result<LambdaTerm, ParseError> term = parseTerm();
                expect(TokenType.RP);
                return term;
        }
    }

    private Result<VarTerm, ParseError> parseVar() {
        String s = token.getText();
        if (!expect(TokenType.VARIABLE)) {
            return new Result<>(null, ParseError.UNEXPECTED_TOKEN);
        }
        return new Result<>(new VarTerm(s));
    }
}
