package edu.kit.typicalc.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.BooleanTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.util.Result;
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
        assertEquals(new AbsTerm(new VarTerm("x"), new VarTerm("x")), parser.parse().unwrap());
    }
    @Test
    void appTerm() {
        LambdaParser parser = new LambdaParser("(λx.x)(λx.x)");
        assertEquals(
                new AppTerm(new AbsTerm(new VarTerm("x"), new VarTerm("x")),
                        new AbsTerm(new VarTerm("x"), new VarTerm("x"))),
                parser.parse().unwrap()
        );
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
    void miscellaneousTerms() {
        LambdaParser parser = new LambdaParser("");
        assertEquals(ParseError.TOO_FEW_TOKENS, parser.parse().unwrapError());
        parser = new LambdaParser("x)");
        assertEquals(ParseError.UNEXPECTED_TOKEN, parser.parse().unwrapError());
        parser = new LambdaParser("??");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("123333333333333");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
        parser = new LambdaParser("x 123333333333333");
        assertEquals(ParseError.UNEXPECTED_CHARACTER, parser.parse().unwrapError());
    }
}
