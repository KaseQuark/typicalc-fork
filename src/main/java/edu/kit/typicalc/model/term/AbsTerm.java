package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;
import java.util.Objects;

/**
 * Representation of an abstraction term with its two sub-lambda terms.
 */
public class AbsTerm extends LambdaTerm {
    private final VarTerm var;
    private final LambdaTerm body;

    /**
     * Initializes a new abstraction term with the variable bound
     * by the abstraction and the lambda term of the abstraction.
     * @param var the variable bound by the abstraction
     * @param body the lambda term of the abstraction
     */
    public AbsTerm(VarTerm var, LambdaTerm body) {
        this.var = var;
        this.body = body;
    }

    /**
     * @return the variable of this abstraction
     */
    public VarTerm getVariable() {
        return var;
    }

    /**
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
    public void accept(TermVisitor termVisitor) {
        termVisitor.visit(this);
    }

    @Override
    public InferenceStep accept(TermVisitorTree termVisitorTree, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
        return termVisitorTree.visit(this, assumptions, type);
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
        return Objects.equals(var, absTerm.var) && Objects.equals(body, absTerm.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var, body);
    }
}
