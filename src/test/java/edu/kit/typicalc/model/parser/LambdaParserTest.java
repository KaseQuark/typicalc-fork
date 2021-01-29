package edu.kit.typicalc.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.BooleanTerm;
//import edu.kit.typicalc.model.term.ConstTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
//import edu.kit.typicalc.model.type.NamedType;
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
	@Test
	void letTerm() {
		LambdaParser parser = new LambdaParser("let id = λx.x in id 1");
		assertEquals(parser.parse().unwrap(),
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
				));
	}
	@Test
	void complicatedTerm() {
		LambdaParser parser = new LambdaParser("(λx.λy.x y 5)(λz.z)(true)");
		assertEquals(parser.parse().unwrap(),
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
				));
	}
	@Test
	void miscellaneousTerms() {
		LambdaParser parser = new LambdaParser("");
		assertEquals(parser.parse().unwrapError(), ParseError.TOO_FEW_TOKENS);
	}
}
