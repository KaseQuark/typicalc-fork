package edu.kit.typicalc.model;

import edu.kit.typicalc.util.Result;

import java.util.ArrayList;
import java.util.List;

public class Unification {
    protected Unification(List<Constraint> constraints) {

    }

    protected List<UnificationStep> getUnificationSteps() {
        return new ArrayList<>(); // TODO
    }

    protected Result<List<Substitution>, UnificationError> getSubstitutions() {
        return new Result<>(null, UnificationError.INFINITE_TYPE); // TODO
    }

}
