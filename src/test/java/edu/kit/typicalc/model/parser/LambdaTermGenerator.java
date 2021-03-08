package edu.kit.typicalc.model.parser;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;

public class LambdaTermGenerator extends Generator<String> {

    public LambdaTermGenerator() {
        super(String.class); // Register the type of objects that we can create
    }

    public static final String[] VARIABLES = new String[]{"x", "y", "z"};

    /**
     * Generate a random lambda term.
     *
     * @param random source of randomness
     * @param status not used
     * @return a random lambda term
     */
    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        return generateReal(random).toString();
    }

    private LambdaTerm generateReal(SourceOfRandomness random) {
        if (random.nextInt(1, 10) < 3) {
            LambdaTerm one = generateReal(random);
            LambdaTerm two = generateReal(random);
            if (random.nextInt(1, 10) < 8) {
                return new AppTerm(one, two);
            } else {
                return new LetTerm(new VarTerm(random.choose(VARIABLES)), one, two);
            }
        } else {
            if (random.nextBoolean()) {
                LambdaTerm one = generateReal(random);
                return new AbsTerm(new VarTerm(random.choose(VARIABLES)), one);
            } else if (random.nextBoolean()) {
                return new VarTerm(random.choose(VARIABLES));
            } else {
                return new IntegerTerm(5);
            }
        }
    }
}
