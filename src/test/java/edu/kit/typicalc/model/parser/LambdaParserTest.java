package edu.kit.typicalc.model.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.kit.typicalc.model.term.VarTerm;
import org.junit.jupiter.api.Test;

class LambdaParserTest {
	@Test
	void varTerm() {
		LambdaParser parser = new LambdaParser("x");
		assertEquals(parser.parse().unwrap(), new VarTerm("x"));
	}
}
