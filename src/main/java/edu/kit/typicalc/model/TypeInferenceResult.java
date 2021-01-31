package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the final result of the type inference with the most general unifier (mgu) and
 * the final inferred type of the lambda term. It is guaranteed that these members can be
 * accessed right after instantiation (no additional initialization is required).
 */
public class TypeInferenceResult {

    private final List<Substitution> MGU;
    private final Type finalType;

    /**
     * Initializes a new TypeInferenceResult for the given substitutions (resulting from
     * the unification) and the given type variable belonging to the original lambda term
     * of the type inference. The final type is obtained by applying the most general unifier
     * to this given type variable. The mgu and the final type are generated here.
     *
     * @param substitutions the substitutions to generate the mgu and the final type
     * @param typeVar the type variable belonging to the original lambda term
     */
    protected TypeInferenceResult(List<Substitution> substitutions, TypeVariable typeVar) {
        MGU = new ArrayList<>(substitutions);
        findMGU();
        finalType = findFinalType(typeVar);
    }

    private void findMGU() {
        for (int i = 0; i < MGU.size(); i++) {
            Substitution applySub = MGU.get(i);
            for (int j = 0; j < MGU.size(); j++) {
                Substitution toSub = MGU.get(j);
                if (i == j) {
                    continue;
                } else if (applySub.getVariable().equals(toSub.getVariable())) {
                    throw new IllegalStateException(
                            "MGU cannot be calculated for two substitutions with same Variable:\n"
                                    + applySub.toString() + "\n"
                                    + toSub.toString());
                }

                Substitution resultSub = new Substitution(toSub.getVariable(), toSub.getType().substitute(applySub));
                MGU.set(j, resultSub);
            }
        }
    }

    private Type findFinalType(TypeVariable typeVar) {
        for (Substitution substitution : MGU) {
            if (substitution.getVariable().equals(typeVar)) {
                return substitution.getType();
            }
        }
        throw new IllegalStateException("MGU has to contain substitution for original type variable: "
                + typeVar.toString());
    }

    /**
     * Returns the most general unifier (mgu), as a list of substitutions. If no valid type
     * (correlating with no valid mgu) can be found for the lambda term to type-infer, null is returned.
     *
     * @return the most general unifier, null if there is no valid mgu
     */
    protected List<Substitution> getMGU() {
        return MGU;
    }

    /**
     * Returns the type that is the result of the type inference. It is obtained by applying
     * the mgu to the type variable the type inference result was given in its construstor.
     * If no valid type can be found for the lambda term to type-infer, null is returned.
     *
     * @return the final type of the lambda term, null if there is no valid type
     */
    protected Type getType() {
        return finalType;
    }
}
