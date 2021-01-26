package edu.kit.typicalc.model;

import java.util.List;

/**
 * Instances of this subclass of TypeInferer are used to execute the sub-inference starting in let steps.
 * They provide an extended constructor to make sure this sub-inference is consistent with the „outer“ inference.
 */
public class TypeInfererLet extends TypeInferer {
    /**
     * Initializes a new TypeInfererLet for the given type assumptions, lambda term and type variable factory.
     *
     * @param lambdaTerm      the lambda term to generate the tree for
     * @param typeAssumptions the type assumptions to consider when generating the tree
     * @param typeVarFactory the type variable factory that should be used in this inference to guarantee consistecy
     *                       with the outer inference
     */
    protected TypeInfererLet(LambdaTerm lambdaTerm, Map<VarTerm, TypeAbstraction> typeAssumptions,
                             TypeVariableFactory typeVarFactory) {
        super(lambdaTerm, typeAssumptions);
        // TODO
    }

    /**
     * Let σ be the most general unifier resulting from this type inference.
     * The following set of constraints is then returned (needed in the outer inference):
     * C := { αi = σ(αi) | σ defined for αi }
     *
     * @return the constraints needed in the outer inference
     */
    public List<Constraint> getLetConstraints() {
        return null;
        // TODO
    }

}
