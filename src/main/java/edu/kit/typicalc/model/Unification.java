package edu.kit.typicalc.model;

import edu.kit.typicalc.util.Result;

import java.util.List;

public class Unification {
    protected Unification(List<Constraint> constraints) {

    }

    protected List<UnificationStep> getUnificationSteps() {
        return null;
    }

    protected Result<List<Substitution>, UnificationError> getSubstitutions() {
        return null;
    }

}
