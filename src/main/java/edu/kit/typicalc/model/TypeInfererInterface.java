package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;

import java.util.List;
import java.util.Optional;

/**
 * Interface for a type inferer with getter methods used to extract the information about
 * the type inference that is needed to visualize it step by step.
 */
public interface TypeInfererInterface {
    /**
     * Returns the first (on the undermost level) inference step of the proof tree of the
     * type inference, presenting an entry point for the tree-like inference step structure.
     *
     * @return the first inference step of the proof tree
     */
    InferenceStep getFirstInferenceStep();

    /**
     * Returns the list of unification steps of this type inference.
     *
     * @return the list of unification steps
     */
    Optional<List<UnificationStep>> getUnificationSteps();

    /**
     * Returns the most general unifier (mgu) for the lambda term that is type-inferred,
     * as a list of substitutions. If no valid type (correlating with no valid mgu) can be
     * found for the lambda term to type-infer, null is returned.
     *
     * @return the most general unifier, empty if there is no valid mgu
     */
    Optional<List<Substitution>> getMGU();

    /**
     * Returns the type that is the result of the type inference.
     * If no valid type can be found for the lambda term to type-infer, null is returned.
     *
     * @return the final type of the lambda term, empty if there is no valid type
     */
    Optional<Type> getType();
}
