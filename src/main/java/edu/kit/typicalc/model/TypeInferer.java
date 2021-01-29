package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeVaribaleKind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The type inferer is responsible for the whole type inference of a given lambda term, taking
 * into account the given type assumptions. It implements the TypeInfererInterface and
 * therefore can hand out an inference step structure, a list of unification steps, the most
 * general unifier and the final type suiting the type inference of the given lambda term. It is
 * guaranteed that this information can be accessed right after instantiation (no additional
 * initialization is required).
 */
public class TypeInferer implements TypeInfererInterface {

    private final Tree tree;
    private final Unification unification;
    private final Optional<TypeInferenceResult> typeInfResult;

    /**
     * Initializes a new TypeInferer for the given type assumptions and lambda term.
     * The inference step structure, unification steps, the most general unifier and the
     * final type are generated and calculated here.
     *
     * @param lambdaTerm the lambda term to generate the tree for
     * @param typeAssumptions the type assumptions to consider when generating the tree
     */
    protected TypeInferer(LambdaTerm lambdaTerm, Map<VarTerm, TypeAbstraction> typeAssumptions) {
        Map<VarTerm, TypeAbstraction> completeTypeAss = createAssForFreeVariables(lambdaTerm);
        completeTypeAss.putAll(typeAssumptions);
        tree = new Tree(completeTypeAss, lambdaTerm);

        // TODO: Abbrechen bei fehlgeschlagener let-Teilinferenz, evtl. getUnificationSteps() anpassen

        unification = new Unification(tree.getConstraints());

        // cancel algorithm if term can't be typified
        if (!unification.getSubstitutions().isOk()) {
            typeInfResult = Optional.empty();
            // TODO: schönere Methode, mit nicht typisierbar umzugehen?
            //  getter unten anpassen!
            return;
        }

        List<Substitution> substitutions = unification.getSubstitutions().getValue();
        typeInfResult = Optional.of(new TypeInferenceResult(substitutions, tree.getFirstTypeVariable()));
    }

    private Map<VarTerm, TypeAbstraction> createAssForFreeVariables(LambdaTerm lambdaTerm) {
        TypeVariableFactory typeVarFactory = new TypeVariableFactory(TypeVaribaleKind.GENERATED_TYPE_ASSUMPTION);
        Set<VarTerm> freeVariables = lambdaTerm.getFreeVariables();

        Map<VarTerm, TypeAbstraction> generatedTypeAss = new HashMap<>();
        for (VarTerm varTerm : freeVariables) {
            generatedTypeAss.put(varTerm, new TypeAbstraction(typeVarFactory.nextTypeVariable()));
        }

        return generatedTypeAss;
    }


    @Override
    public InferenceStep getFirstInferenceStep() {
        return tree.getFirstInferenceStep();
    }

    @Override
    public List<UnificationStep> getUnificationSteps() {
        return unification.getUnificationSteps();
    }

    @Override
    public Optional<List<Substitution>> getMGU() {
        return typeInfResult.map(TypeInferenceResult::getMGU);
    }

    @Override
    public Optional<Type> getType() {
        return typeInfResult.map(TypeInferenceResult::getType);
    }
}
