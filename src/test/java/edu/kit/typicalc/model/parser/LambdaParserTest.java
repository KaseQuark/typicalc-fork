package edu.kit.typicalc.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

class LambdaParserTest {
	@Test
	void varTerm() {
		LambdaParser parser = new LambdaParser("x");
		Result<LambdaTerm, ParseError> term = parser.parse();
		System.out.println(term);
		assertEquals(term.unwrap(), new VarTerm("x"));
	}
	@Test
	void absTerm() {
		LambdaParser parser = new LambdaParser("λx.x");
		assertEquals(parser.parse().unwrap(), new AbsTerm(new VarTerm("x"), new VarTerm("x")));
	}
	@Test
	void appTerm() {
		LambdaParser parser = new LambdaParser("(λx.x)(λx.x)");
		assertEquals(parser.parse().unwrap(),
				new AppTerm(new AbsTerm(new VarTerm("x"), new VarTerm("x")),
						new AbsTerm(new VarTerm("x"), new VarTerm("x"))));
	}
}
