package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Models the necessary actions to process a constraint.
 */
public class UnificationActions {
    private final Collection<Constraint> constraints;
    private final Optional<Substitution> substitution;

    /**
     * Initializes this object using the provided constraints and substitution.
     * @param constraints added constraints, if any
     * @param substitution necessary substitution, if any
     */
    protected UnificationActions(Collection<Constraint> constraints, Optional<Substitution> substitution) {
        this.constraints = constraints;
        this.substitution = substitution;
    }

    /**
     * Initializes an empty object.
     */
    protected UnificationActions() {
        this.constraints = Collections.emptyList();
        this.substitution = Optional.empty();
    }

    /**
     * Getter for constraints
     * @return the constraints stored in this object
     */
    public Collection<Constraint> getConstraints() {
        return constraints;
    }
    /**
     * Getter for substitution
     * @return the substitution stored in this object
     */
    public Optional<Substitution> getSubstitution() {
        return substitution;
    }
}
