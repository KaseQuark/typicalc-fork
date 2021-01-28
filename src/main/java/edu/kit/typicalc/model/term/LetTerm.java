package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

/**
 * Representation of a let term with its variable, the lambda term assigned
 * to this variable and the lambda term the variable is used in.
 */
public class LetTerm extends LambdaTerm {
	private final VarTerm variable;
	private final LambdaTerm definition;
	private final LambdaTerm body;

	/**
	 * Initializes a new let term with its variable and two lambda terms.
	 * @param variable the variable of the let term
	 * @param definition the lambda term assigned to the variable
	 * @param body the lambda term the variable may be used in
	 */
	public LetTerm(VarTerm variable, LambdaTerm definition, LambdaTerm body) {
		this.variable = variable;
		this.definition = definition;
		this.body = body;
	}

	@Override
	public boolean hasLet() {
		return true;
	}

	@Override
	public void accept(TermVisitor termVisitor) {
		termVisitor.visit(this);
	}

	@Override
	public InferenceStep accept(TermVisitorTree visitor, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
		return visitor.visit(this, assumptions, type);
	}

	/**
	 * @return the variable defined in this let expression
	 */
	public VarTerm getVariable() {
		return variable;
	}

	/**
	 * @return definition of the variable
	 */
	public LambdaTerm getVariableDefinition() {
		return definition;
	}

	/**
	 * @return the inner lambda term (where the variable is used)
	 */
	public LambdaTerm getInner() {
		return body;
	}
}
