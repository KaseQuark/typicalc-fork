package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;
import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Utility class to avoid unification logic duplication in type methods (constrainEqualTo*).
 *
 * @see Type
 */
final class UnificationUtil {
    private UnificationUtil() { }

    static Result<UnificationActions, UnificationError> functionFunction(FunctionType a, FunctionType b) {
        return new Result<>(new UnificationActions(List.of(
                new Constraint(a.getParameter(), b.getParameter()),
                new Constraint(a.getOutput(), b.getOutput())), Optional.empty()));
    }

    static Result<UnificationActions, UnificationError> functionNamed(FunctionType a, NamedType b) {
        return new Result<>(null, UnificationError.DIFFERENT_TYPES);
    }

    static Result<UnificationActions, UnificationError> functionVariable(FunctionType a, TypeVariable b) {
        if (a.contains(b)) {
            return new Result<>(null, UnificationError.INFINITE_TYPE);
        }
        return new Result<>(new UnificationActions(Collections.emptyList(),
                Optional.of(new Substitution(b, a))));
    }

    static Result<UnificationActions, UnificationError> variableNamed(TypeVariable a, NamedType b) {
        return new Result<>(new UnificationActions(Collections.emptyList(),
                Optional.of(new Substitution(a, b))));
    }

    static Result<UnificationActions, UnificationError> variableVariable(TypeVariable a, TypeVariable b) {
        if (!a.equals(b)) {
            return new Result<>(new UnificationActions(Collections.emptyList(),
                    Optional.of(new Substitution(a, b))));
        } else {
            return new Result<>(new UnificationActions());
        }
    }

    static Result<UnificationActions, UnificationError> namedNamed(NamedType a, NamedType b) {
        if (!a.equals(b)) {
            return new Result<>(null, UnificationError.DIFFERENT_TYPES);
        } else {
            return new Result<>(new UnificationActions());
        }
    }
}
