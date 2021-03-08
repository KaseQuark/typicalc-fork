package edu.kit.typicalc.model.parser;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.util.Result;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.pholser.junit.quickcheck.*;

@RunWith(JQF.class)
public class LambdaParserFuzzTest {
    /**
     * Runs the type inference algorithm and gets the first inference step.
     * Only validates that the algorithm produced something.
     *
     * @param term lambda term
     */
    @Fuzz
    public void testInference(@From(LambdaTermGenerator.class) String term) {
        Model model = new ModelImpl();
        Result<TypeInfererInterface, ParseError> typer = model.getTypeInferer(term, new HashMap<>());
        InferenceStep first = typer.unwrap().getFirstInferenceStep();
    }

    // this is how to rerun a specific fuzz result
    /*
    @Fuzz(repro="target/fuzz-results/edu.kit.typicalc.model.parser.LambdaParserFuzzTest/testInference/corpus/id_000066")
    public void testWithGenerator(@From(LambdaTermGenerator.class) String code) {
        System.out.println(code);
    }
    */

    @Fuzz
    @Ignore // remove if you intend to use AFL
    public void testLambdaParserAFL(InputStream input) {
        String term;
        try {
            term = new String(input.readAllBytes());
        } catch (Throwable t) {
            return;
        }
        LambdaParser parser = new LambdaParser(term);
        Result<LambdaTerm, ParseError> result = parser.parse();
        if (result.isOk()) {
            LambdaTerm term1 = result.unwrap();
            String sameTerm = term1.toString();
            LambdaParser parser2 = new LambdaParser(sameTerm);
            Result<LambdaTerm, ParseError> result2 = parser2.parse();
            LambdaTerm term2 = result2.unwrap();
            if (!term2.equals(term1)) {
                throw new IllegalStateException("results differ, when parsing: " + term);
            }
        }
    }
}
