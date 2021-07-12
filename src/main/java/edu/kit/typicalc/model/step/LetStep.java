package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.TypeInfererLet;

import java.util.Objects;

/**
 * Models one step of the inference tree where the let rule is applied. A let step contains an
 * additional instance of a type inferer that is responsible for the „sub-inference“ that takes
 * place when applying the let rule. This type inferer grants access to all the information
 * needed to visualize this sub-inference.
 * If the sub-inference fails due to a contradiction or an infinite type forming in its unification,
 * the inference step representing the second premise of the let step should not be created
 * and the „outer“ inference should be interrupted
 */
public abstract class LetStep extends InferenceStep {
    private final InferenceStep premise;
    private final TypeInfererLet typeInferer;

    /**
     * Initializes a new LetStep with the given values.
     *
     * @param conclusion  the conclusion of this step
     * @param constraint  the constraint added in this step
     * @param premise     the right premise of this step
     * @param typeInferer the typeInferer that performs the Type Inference for the premise
     *                    that needs its own type Inference.
     * @param stepIndex   step number
     */
    protected LetStep(Conclusion conclusion, Constraint constraint, InferenceStep premise, TypeInfererLet typeInferer,
                      int stepIndex) {
        super(conclusion, constraint, stepIndex);
        this.premise = premise;
        this.typeInferer = typeInferer;
    }

    /**
     * Returns the premise of the let step that doesn't have its own sub-inference (the
     * one usually placed right in the proof tree).
     *
     * @return premise the right premise of this step
     */
    public InferenceStep getPremise() {
        return premise;
    }

    /**
     * Returns the TypeInferer for the premise which needs its own type Inference.
     *
     * @return typeInferer the type inferer of the sub-inference
     */
    public TypeInfererLet getTypeInferer() {
        return typeInferer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LetStep letStep = (LetStep) o;
        return premise.equals(letStep.premise) && typeInferer.equals(letStep.typeInferer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), premise, typeInferer);
    }
}
