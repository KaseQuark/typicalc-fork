package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

public class LetTerm extends LambdaTerm {
	public LetTerm(VarTerm var, LambdaTerm def, LambdaTerm body) {
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
	public InferenceStep accept(TermVisitorTree termVisitorTree, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
		//todo implement
		return null;
	}
}
