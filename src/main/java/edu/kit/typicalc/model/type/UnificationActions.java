package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;

import java.util.Collection;
import java.util.Optional;

/**
 * Models the neccessary actions to process a constraint.
 */
public class UnificationActions {
    private Collection<Constraint> constraints;
    private Optional<Substitution> substitution;

    /**
     * Initializes this object using the provided constraints and substitution.
     * @param constraints added constraints, if any
     * @param substitution neccessary substitution, if any
     */
    protected UnificationActions(Collection<Constraint> constraints, Optional<Substitution> substitution) {
        this.constraints = constraints;
        this.substitution = substitution;
    }

    /**
     * Getter for constraints
     * @return the constraints stored in this object
     */
    public Collection<Constraint> getConstraints() {
        return constraints;
    }

    public Optional<Substitution> getSubstitution() {
        return substitution;
    }
}
