package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.*;

/**
 * Representation of a variable term with its name.
 */
public class VarTerm extends LambdaTerm {
    private final String name;

    /**
     * Initializes a new variable term with its name.
     * @param name the name of the variable
     */
    public VarTerm(String name) {
        super();
        this.name = name;
    }

    /**
     * @return the name of this variable
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean hasLet() {
        return false;
    }

    @Override
    public Set<VarTerm> getFreeVariables() {
        return new HashSet<>(Collections.singletonList(this));
    }

    @Override
    public void accept(TermVisitor termVisitor) {
        termVisitor.visit(this);
    }

    @Override
    public InferenceStep accept(TermVisitorTree visitor, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
        return visitor.visit(this, assumptions, type);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VarTerm varTerm = (VarTerm) o;
        return Objects.equals(name, varTerm.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
