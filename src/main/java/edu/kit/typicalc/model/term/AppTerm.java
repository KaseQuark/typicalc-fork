package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.*;

/**
 * Representation of an application term consisting of a function and the parameter passed to it.
 */
public class AppTerm extends LambdaTerm {
    private final LambdaTerm left;
    private final LambdaTerm right;

    /**
     * Initializes a new application term with one lambda term for the function and one lambda term for the parameter.
     * @param left the function
     * @param right the parameter
     */
    public AppTerm(LambdaTerm left, LambdaTerm right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return the function used in this application
     */
    public LambdaTerm getFunction() {
        return left;
    }

    /**
     * @return the parameter used in this application
     */
    public LambdaTerm getParameter() {
        return right;
    }

    @Override
    public boolean hasLet() {
        return left.hasLet() || right.hasLet();
    }

    @Override
    public Set<VarTerm> getFreeVariables() {
        Set<VarTerm> set = new HashSet<>(this.left.getFreeVariables());
        set.addAll(this.right.getFreeVariables());
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
        return "(" + left + ")(" + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppTerm appTerm = (AppTerm) o;
        return Objects.equals(left, appTerm.left) && Objects.equals(right, appTerm.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
