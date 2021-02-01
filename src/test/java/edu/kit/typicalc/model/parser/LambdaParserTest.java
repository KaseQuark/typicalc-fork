package edu.kit.typicalc.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.BooleanTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.util.Result;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class LambdaParserTest {
    @Test
    void varTerm() {
        LambdaParser parser = new LambdaParser("x");
        Result<LambdaTerm, ParseError> term = parser.parse();
        assertEquals(new VarTerm("x"), term.unwrap());
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
                parser.parse().unwrap()
        );
    }
    @Test
    void errorHandling() {
        LambdaParser parser = new LambdaParser("");
        assertEquals(ParseError.TOO_FEW_TOKENS, parser.parse().unwrapError());
        parser = new LambdaParser("x)");
        ParseError error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 1), error.getCause());
        parser = new LambdaParser("??");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("123333333333333");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("x 123333333333333");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_CHARACTER, error);
        assertEquals(new Token(TokenType.NUMBER, "123333333333333", 2), error.getCause());
        parser = new LambdaParser("λ)");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 1), error.getCause());
        parser = new LambdaParser("λx=");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.EQUALS, "=", 2), error.getCause());
        parser = new LambdaParser("λx..");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.DOT, ".", 3), error.getCause());
        parser = new LambdaParser("let ) =");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 4), error.getCause());
        parser = new LambdaParser("let x .");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.DOT, ".", 6), error.getCause());
        parser = new LambdaParser("let x = )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 8), error.getCause());
        parser = new LambdaParser("let x = y )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 10), error.getCause());
        parser = new LambdaParser("let x = y in )");
        error = parser.parse().unwrapError();
        assertEquals(ParseError.UNEXPECTED_TOKEN, error);
        assertEquals(new Token(TokenType.RIGHT_PARENTHESIS, ")", 13), error.getCause());
    }

    @Test
    void equality() {
        EqualsVerifier.forClass(Token.class).usingGetClass().verify();
    }
}
