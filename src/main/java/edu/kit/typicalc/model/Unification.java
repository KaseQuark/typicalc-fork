package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.UnificationActions;
import edu.kit.typicalc.util.Result;

import java.util.*;

/**
 * Models the unification of a type inference with its individual steps.
 * It grants access to the list of unification steps and a list of the substitutions that result from the unification.
 */
public class Unification {
    private final List<UnificationStep> steps;
    private final Result<List<Substitution>, UnificationError> substitutionsResult;

    /**
     * Initializes a new {@link Unification} for the given constraints.
     * The list of unification steps and the resulting substitutions are generated here.
     * @param constraints constraints to execute the unification for
     */
    protected Unification(Deque<Constraint> constraints) { // TODO: document List->Deque
        steps = new ArrayList<>();
        List<Substitution> substitutions = new ArrayList<>();

        steps.add(new UnificationStep(new Result<>(Collections.emptyList()), new ArrayList<>(constraints)));
        while (!constraints.isEmpty()) {
            Constraint c = constraints.removeFirst();
            // calculate the result of this constraint
            Type a = c.getFirstType();
            Type b = c.getSecondType();
            Result<UnificationActions, UnificationError> actions = a.constrainEqualTo(b);
            if (actions.isError()) {
                steps.add(new UnificationStep(new Result<>(actions), new ArrayList<>(constraints)));
                substitutionsResult = new Result<>(actions);
                return;
            }
            UnificationActions thisStep = actions.unwrap();
            Optional<Substitution> substitution = thisStep.getSubstitution();
            // apply substitution to remaining constraints
            if (substitution.isPresent()) {
                Deque<Constraint> updateConstraints = new ArrayDeque<>();
                for (Constraint constraint : constraints) {
                    Type first = constraint.getFirstType();
                    Type second = constraint.getSecondType();
                    updateConstraints.add(new Constraint(
                            first.substitute(substitution.get()),
                            second.substitute(substitution.get())
                    ));
                }
                constraints = updateConstraints;
                substitutions.add(substitution.get());
            }
            // add new constraints to the queue
            for (Constraint constraint : thisStep.getConstraints()) {
                constraints.addFirst(constraint);
            }
            steps.add(new UnificationStep(new Result<>(new ArrayList<>(substitutions)), new ArrayList<>(constraints)));
        }

        substitutionsResult = new Result<>(substitutions);
    }

    /**
     * @return list of the unification steps the unification performs.
     */
    protected List<UnificationStep> getUnificationSteps() {
        return Collections.unmodifiableList(steps);
    }

    /**
     * Returns as a {@link Result} either the list of substitutions that result from the unification,
     * needed for the subsequent calculation of the most general unifier and final type.
     * Or, in case of a constraint list that can't be unified, a {@link UnificationError}.
     *
     * @return a {@link Result} containing either the list of the resulting substitutions or a {@link UnificationError}
     */
    protected Result<List<Substitution>, UnificationError> getSubstitutions() {
        return substitutionsResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unification that = (Unification) o;
        return steps.equals(that.steps) && substitutionsResult.equals(that.substitutionsResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, substitutionsResult);
    }
}
