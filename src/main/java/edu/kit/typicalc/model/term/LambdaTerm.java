package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;
import java.util.Set;

/**
 * Abstract representation of a lambda term.
 * Depending on the subclass used, a lambda term may contain several other lambda terms
 * and thus form a tree-like structure of lambda terms.
 */
public abstract class LambdaTerm {
    /**
     * @return whether the lambda term contains a let expression
     */
    public abstract boolean hasLet();

    /**
     * Returns a set of all free variables occurring in the lambda term.
     * @return all free variables
     */
    public abstract Set<VarTerm> getFreeVariables();

    /**
     * Calls exactly one method on the visitor depending on the lambda term type.
     * @param termVisitor a visitor
     */
    public abstract void accept(TermVisitor termVisitor);

    /**
     * Uses exactly one method of the visitor and provides the arguments passed.
     * @param termVisitorTree the visitor
     * @param assumptions type assumptions
     * @param type a type
     * @return the result returned by the visitor
     */
    public abstract InferenceStep accept(TermVisitorTree termVisitorTree,
                                         Map<VarTerm, TypeAbstraction> assumptions, Type type);
}
