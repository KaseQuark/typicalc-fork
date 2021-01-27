package edu.kit.typicalc.model.term;

public interface TermVisitor {
	/**
	 * Visit an application term.
	 * @param appTerm the term
	 */
	void visit(AppTerm appTerm);

	/**
	 * Visit an abstraction term.
	 * @param absTerm the term
	 */
	void visit(AbsTerm absTerm);

	/**
	 * Visit a let expression.
	 * @param letTerm the term
	 */
	void visit(LetTerm letTerm);

	/**
	 * Visit a variable.
	 * @param varTerm the variable
	 */
	void visit(VarTerm varTerm);

	/**
	 * Visit a constant.
	 * @param constTerm the constant
	 */
	void visit(ConstTerm constTerm);
}
