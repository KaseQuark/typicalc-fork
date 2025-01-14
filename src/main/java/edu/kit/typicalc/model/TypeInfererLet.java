package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.*;

/**
 * Instances of this implementation of TypeInfererInterface are used to execute the sub-inference starting in let steps.
 * They provide an extended constructor (compared to the standard type inferer) to make sure this sub-inference
 * is consistent with the "outer" inference.
 */
public class TypeInfererLet implements TypeInfererInterface {

    private final Tree tree;
    private final Optional<Unification> unification;
    private final Optional<TypeInferenceResult> typeInfResult;

    /**
     * Initializes a new TypeInfererLet for the given type assumptions, lambda term and type variable factory.
     * The inference step structure, unification steps, the most general unifier and the
     * final type are generated and calculated here.
     *
     * @param lambdaTerm      the lambda term to generate the tree for
     * @param typeAssumptions the type assumptions to consider when generating the tree
     * @param typeVarFactory  the type variable factory that should be used in this inference to guarantee consistency
     *                        with the outer inference
     * @param factory         the step number factory to pass to the Tree
     */
    protected TypeInfererLet(LambdaTerm lambdaTerm, Map<VarTerm, TypeAbstraction> typeAssumptions,
                             TypeVariableFactory typeVarFactory, StepNumberFactory factory) {
        tree = new Tree(typeAssumptions, lambdaTerm, typeVarFactory, true, factory);

        // cancel algorithm if a sub-inference failed
        if (tree.hasFailedSubInference()) {
            unification = Optional.empty();
            typeInfResult = Optional.empty();
            return;
        }

        unification = Optional.of(new Unification(new ArrayDeque<>(tree.getConstraints())));

        // cancel algorithm if term can't be typed
        if (unification.get().getSubstitutions().isError()) {
            typeInfResult = Optional.empty();
            return;
        }

        List<Substitution> substitutions = unification.get().getSubstitutions().unwrap();
        typeInfResult = Optional.of(new TypeInferenceResult(substitutions, tree.getFirstTypeVariable()));
    }

    /**
     * Let σ be the most general unifier resulting from this type inference.
     * The following set of constraints is then returned (needed in the outer inference):
     * C := { αi = σ(αi) | σ defined for αi }
     *
     * @return the constraints needed in the outer inference
     * @throws IllegalStateException if the method is called despite missing MGU
     */
    public List<Constraint> getLetConstraints() {
        if (this.getMGU().isEmpty()) {
            throw new IllegalStateException("getLetConstraints() should never be called when no mgu was found");
        }
        List<Constraint> letConstraints = new ArrayList<>();
        for (Substitution substitution : this.getMGU().orElseThrow(IllegalStateException::new)) {
            Constraint constraint = new Constraint(substitution.getVariable(), substitution.getType());
            letConstraints.add(constraint);
        }
        return letConstraints;
    }

    @Override
    public InferenceStep getFirstInferenceStep() {
        return tree.getFirstInferenceStep();
    }

    @Override
    public Optional<List<UnificationStep>> getUnificationSteps() {
        return unification.map(Unification::getUnificationSteps);
    }

    @Override
    public Optional<List<Substitution>> getMGU() {
        return typeInfResult.map(TypeInferenceResult::getMGU);
    }

    @Override
    public Optional<Type> getType() {
        return typeInfResult.map(TypeInferenceResult::getType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeInfererLet that = (TypeInfererLet) o;
        return tree.equals(that.tree) && unification.equals(that.unification)
                && typeInfResult.equals(that.typeInfResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree, unification, typeInfResult);
    }
}