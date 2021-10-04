package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Representation of an abstraction term with its two sub-lambda terms.
 */
public class AbsTerm extends LambdaTerm {
    private final VarTerm varTerm;
    private final LambdaTerm body;

    /**
     * Initializes a new abstraction term with the variable bound
     * by the abstraction and the lambda term of the abstraction.
     *
     * @param varTerm  the variable bound by the abstraction
     * @param body the lambda term of the abstraction
     */
    public AbsTerm(VarTerm varTerm, LambdaTerm body) {
        this.varTerm = varTerm;
        this.body = body;
    }

    /**
     * Returns the variable of this abstraction
     *
     * @return the variable of this abstraction
     */
    public VarTerm getVariable() {
        return varTerm;
    }

    /**
     * Returns the function body of this abstraction
     *
     * @return the function body of this abstraction
     */
    public LambdaTerm getInner() {
        return body;
    }

    @Override
    public boolean hasLet() {
        return body.hasLet();
    }

    @Override
    public Set<VarTerm> getFreeVariables() {
        Set<VarTerm> set = new HashSet<>(this.body.getFreeVariables());
        set.remove(this.varTerm);
        return set;
    }

    @Override
    public void accept(TermVisitor termVisitor) {
        termVisitor.visit(this);
    }

    @Override
    public InferenceStep accept(TermVisitorTree termVisitorTree, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
        return termVisitorTree.visit(this, assumptions, type);
    }

    @Override
    public String toString() {
        return "λ" + varTerm + "." + body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbsTerm absTerm = (AbsTerm) o;
        return Objects.equals(varTerm, absTerm.varTerm) && Objects.equals(body, absTerm.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varTerm, body);
    }
}
