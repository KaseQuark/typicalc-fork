package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.util.Result;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaParserTest {
    private static final VarTerm X = new VarTerm("x");
    private static final VarTerm Y = new VarTerm("y");

    @Test
    void varTerm() {
        LambdaParser parser = new LambdaParser("x");
        Result<LambdaTerm, ParseError> term = parser.parse();
        assertEquals(new VarTerm("x"), term.unwrap());
        parser = new LambdaParser("b1 a1");
        term = parser.parse();
        assertEquals(new AppTerm(new VarTerm("b1"), new VarTerm("a1")), term.unwrap());
    }
    @Test
    void absTerm() {
        LambdaParser parser = new LambdaParser("λx.x");
        LambdaTerm term = parser.parse().unwrap();
        assertEquals(new AbsTerm(new VarTerm("x"), new VarTerm("x")), term);
        assertEquals("λx.x", term.toString());
    }
    @Test
    void appTerm() {
        LambdaParser parser = new LambdaParser("(λx.x)(λx.x)");
        LambdaTerm term = parser.parse().unwrap();
        assertEquals(
                new AppTerm(new AbsTerm(new VarTerm("x"), new VarTerm("x")),
                        new AbsTerm(new VarTerm("x"), new VarTerm("x"))),
                term
        );
        assertEquals("(λx.x)(λx.x)", term.toString());
    }
    @Test
    void letTerm() {
        LambdaParser parser = new LambdaParser("let id = λx.x in id 1");
        assertEquals(
                new LetTerm(
                        new VarTerm("id"),
                        new AbsTerm(
                                new VarTerm("x"),
                                new VarTerm("x")
                        ),
                        new AppTerm(
                                new VarTerm("id"),
                                new IntegerTerm(1)
                        )
                ),
                parser.parse().unwrap()
        );
    }
    @Test
    void complicatedTerm() {
        LambdaParser parser = new LambdaParser("(λx.λy.x y 5)(λz.z)(true)");
        Result<LambdaTerm, ParseError> term = parser.parse();
        if (term.isError()) {
            System.err.println(term.unwrapError());
            System.err.println(term.unwrapError().getCause());
        }
        assertEquals(
                new AppTerm(
                        new AppTerm(
                                new AbsTerm(
                                        new VarTerm("x"),
                                        new AbsTerm(
                                                new VarTerm("y"),
                                                new AppTerm(
                                                        new AppTerm(
                                                                new VarTerm("x"),
                                                                new VarTerm("y")
                                                        ),
                                                        new IntegerTerm(5)
                                                )
                                        )
                                ),
                                new AbsTerm(
                                        new VarTerm("z"),
                                        new VarTerm("z")
                                )
                        ),
                        new BooleanTerm(true)
                ),
                term.unwrap()
        );
    }
    @Test
    void errorHandling() {
        LambdaParser parser = new LambdaParser("");
        assertEquals(ParseError.TOO_FEW_TOKENS, parser.parse().unwrapError());
        parser = new LambdaParser("x)");
        ParseError error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "x)", 1), error.getCause().get());
        parser = new LambdaParser("??");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("aλ");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("ä");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("123333333333333");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("x 123333333333333");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_CHARACTER, error);
        assertEquals(new Token(TokenType.NUMBER, "123333333333333", "x 123333333333333", 2),
                error.getCause().get());
        parser = new LambdaParser("λ)");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "λ)", 1), error.getCause().get());
        parser = new LambdaParser("λx=");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.EQUALS, "=", "λx=", 2), error.getCause().get());
        parser = new LambdaParser("λx..");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.DOT, ".", "λx..", 3), error.getCause().get());
        assertEquals(ExpectedInput.TERM, error.getExpectedInput().get());
        parser = new LambdaParser("let ) =");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "let ) =", 4), error.getCause().get());
        parser = new LambdaParser("let x .");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.DOT, ".", "let x .", 6), error.getCause().get());
        parser = new LambdaParser("let x = )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "let x = )",8), error.getCause().get());
        parser = new LambdaParser("let x = y )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "let x = y )", 10), error.getCause().get());
        parser = new LambdaParser("let x = y in )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", "let x = y in )", 13), error.getCause().get());
    }

    @Test
    void bugFoundByThomas() {
        LambdaParser parser = new LambdaParser("(λx. x) λx. x");
        Result<LambdaTerm, ParseError> term = parser.parse();
        if (term.isError()) {
            System.err.println(term.unwrapError());
            System.err.println(term.unwrapError().getCause());
        }
        assertEquals(new AppTerm(new AbsTerm(X, X), new AbsTerm(X, X)), term.unwrap());
    }

    @Test
    void bugFoundByJohanna() {
        // original term: (λx.λy.y (x y)) (λz. λa. z g a) let f = λx. let g = λy. y in g x in f 3
        // reduced:
        LambdaParser parser = new LambdaParser("(λx.x) let id = λy.y in id");
        Result<LambdaTerm, ParseError> term = parser.parse();
        if (term.isError()) {
            System.err.println(term.unwrapError());
            System.err.println(term.unwrapError().getCause());
        }
        assertEquals(new AppTerm(
                new AbsTerm(X, X),
                new LetTerm(new VarTerm("id"), new AbsTerm(Y, Y), new VarTerm("id"))
                ),
                term.unwrap());
    }

    @Test
    void complicatedIdentity() {
        LambdaParser parser = new LambdaParser("(λx. x) (λx. x) λx. x");
        Result<LambdaTerm, ParseError> term = parser.parse();
        if (term.isError()) {
            System.err.println(term.unwrapError());
            System.err.println(term.unwrapError().getCause());
        }
        assertEquals(new AppTerm(new AppTerm(new AbsTerm(X, X), new AbsTerm(X, X)), new AbsTerm(X, X)), term.unwrap());
    }

    @Test
    void usefulErrors() {
        LambdaParser parser = new LambdaParser("λx..");
        ParseError error = parser.parse().unwrapError();
        assertEquals(ExpectedInput.TERM, error.getExpectedInput().get());
        assertEquals(3, error.getPosition());
    }

    @Test
    void equality() {
        EqualsVerifier.forClass(Token.class).usingGetClass().verify();
    }
}
