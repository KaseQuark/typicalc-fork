package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract representation of a constant lambda term that has a predetermined type and a value of that type.
 */
public abstract class ConstTerm extends LambdaTerm {
    /**
     * @return the named type of the constant
     */
    public abstract NamedType getType();

    @Override
    public Set<VarTerm> getFreeVariables() {
        return new HashSet<>();
    }

    @Override
    public boolean hasLet() {
        return false;
    }

    @Override
    public InferenceStep accept(TermVisitorTree visitor, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
        return visitor.visit(this, assumptions, type);
    }
}
