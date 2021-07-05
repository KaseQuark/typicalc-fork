package edu.kit.typicalc.model;

import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.util.Result;

/**
 * This interface accepts user input and returns a type inference result.
 */
public interface Model {
    /**
     * Given the user input, an implementation of this method should compute the type
     * inference results.
     *
     * @param lambdaTerm      the lambda term to type-infer
     * @param typeAssumptions the type assumptions to use
     * @return either an error or a TypeInfererInterface on success
     */
    Result<TypeInfererInterface, ParseError> getTypeInferer(String lambdaTerm,
                                                            String typeAssumptions);
}
