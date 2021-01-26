package edu.kit.typicalc.model;

import java.util.List;

/**
 * The type inferer is responsible for the whole type inference of a given lambda term, taking
 * into account the given type assumptions. It implements the TypeInfererInterface and
 * therefore can hand out an inference step structure, a list of unification steps, the most
 * general unifier and the final type suiting the type inference of the given lambda term. It is
 * guaranteed that this information can be accessed right after instantiation (no additional
 * initialization is required).
 */
public class TypeInferer implements TypeInfererInterface {

    /**
     * Initializes a new TypeInferer for the given type assumptions and lambda term.
     * The inference step structure, unification steps, the most general unifier and the
     * final type are generated and calculated here.
     *
     * @param lambdaTerm the lambda term to generate the tree for
     * @param typeAssumptions the type assumptions to consider when generating the tree
     */
    protected TypeInferer(LambdaTerm lambdaTerm, Map<VarTerm, TypeAbstraction> typeAssumptions) {
        // TODO
    }


    @Override
    public InferenceStep getFirstInferenceStep() {
        return null;
        // TODO
    }

    @Override
    public List<UnificationStep> getUnificationSteps() {
        return null;
        // TODO
    }

    @Override
    public List<Substitution> getMGU() {
        return null;
        // TODO
    }

    @Override
    public Type getType() {
        return null;
        // TODO
    }
}
