package edu.kit.typicalc.model;

import edu.kit.typicalc.model.parser.LambdaParser;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.model.parser.TypeAssumptionParser;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.util.Result;

import java.util.Map;

/**
 * Accepts user input and returns a type inference result.
 */
public class ModelImpl implements Model {

    /**
     *Parses the user input given as the lambdaTerm and typeAssumptions and creates
     * a TypeInferer object.
     * @param lambdaTerm the lambda term to type-infer
     * @param typeAssumptions the type assumptions to use
     * @return A TypeInferer object that has calculated the type Inference for the given Lambda Term
     * and type Assumptions
     *
     */
    @Override
    public Result<TypeInfererInterface, ParseError> getTypeInferer(String lambdaTerm,
                                                                   Map<String, String> typeAssumptions) {
        // Parse Lambda Term
        LambdaParser parser = new LambdaParser(lambdaTerm);
        Result<LambdaTerm, ParseError> result = parser.parse();
        if (result.isError()) {
            return new Result<>(null, result.unwrapError());
        }
        //Parse Type Assumptions
        TypeAssumptionParser assumptionParser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> assumptionMap = assumptionParser.parse(typeAssumptions);
        if (assumptionMap.isError()) {
            return new Result<>(null, assumptionMap.unwrapError());
        }
        //Create and return TypeInferer
        TypeInferer typeInferer = new TypeInferer(result.unwrap(), assumptionMap.unwrap());
        return new Result<>(typeInferer, null);
    }
}
