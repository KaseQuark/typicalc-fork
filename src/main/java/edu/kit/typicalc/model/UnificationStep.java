package edu.kit.typicalc.model;

import edu.kit.typicalc.util.Result;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Models one step of the unification algorithm with a list of constraints and a list of substitutions.
 * These lists contain all the constraints or substitutions that are present in the unification after this step.
 * When detected that this unification step leads to a contradiction or an infinite type,
 * it contains a {@link UnificationError} instead of a list of substitutions.
 */
public class UnificationStep {
    private final Result<List<Substitution>, UnificationError> substitutions;
    private final List<Constraint> constraints;

    /**
     * Initializes a new {@link UnificationStep} with the given lists of constraints and substitutions.
     * 	When detected that this unification step leads to a contradiction or an infinite type,
     * 	it should be passed a {@link UnificationError} instead of a list of substitutions.
     * @param substitutions list of substitutions, or an error
     * @param constraints the list of all constraints of the unification (in the state resulting from this step)
     */
    protected UnificationStep(Result<List<Substitution>, UnificationError> substitutions,
                              List<Constraint> constraints) {
        this.substitutions = substitutions;
        this.constraints = constraints;
    }

    /**
     * Returns as a {@link Result} either the list of all substitutions
     * of the unification (in the state resulting from this step).
     * Or, in case of a detected contradiction or infinite type, a {@link UnificationError}.
     */
    public Result<List<Substitution>, UnificationError> getSubstitutions() {
        return substitutions;
    }

    /**
     * @return a list of all resulting constraints
     */
    public List<Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public String toString() {
        return "UnificationStep{"
                + "substitutions=" + substitutions
                + ", constraints=" + Arrays.toString(constraints.toArray())
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnificationStep that = (UnificationStep) o;
        return substitutions.equals(that.substitutions) && constraints.equals(that.constraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(substitutions, constraints);
    }
}
