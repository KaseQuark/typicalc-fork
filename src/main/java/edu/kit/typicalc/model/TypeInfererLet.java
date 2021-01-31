package edu.kit.typicalc.model;

import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @throws IllegalStateException if the method is called despite missing mgu
     */
    public List<Constraint> getLetConstraints() {
        if (this.getMGU().isEmpty()) {
            throw new IllegalStateException("getLetConstraints() should never be called when no mgu was found");
        }
        List<Constraint> letConstraints = new ArrayList<>();
        for (Substitution substitution : this.getMGU().get()) {
            Constraint constraint = new Constraint(substitution.getVariable(), substitution.getType());
            letConstraints.add(constraint);
        }
        return letConstraints;
    }

}
