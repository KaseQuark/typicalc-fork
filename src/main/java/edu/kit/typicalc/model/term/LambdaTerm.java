package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

public abstract class LambdaTerm {
	/**
	 * @return whether the lambda term contains a let expression
	 */
	public abstract boolean hasLet();

	/**
	 * Calls exactly one method on the visitor depending on the lambda term type.
	 * @param termVisitor a visitor
	 */
	public abstract void accept(TermVisitor termVisitor);

    public abstract InferenceStep accept(TermVisitorTree termVisitorTree,
                                         Map<VarTerm, TypeAbstraction> assumptions, Type type);

}
