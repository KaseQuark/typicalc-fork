package edu.kit.typicalc.model.term;

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
}
