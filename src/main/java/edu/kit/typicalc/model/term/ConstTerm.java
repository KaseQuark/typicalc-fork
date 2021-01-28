package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

public class ConstTerm extends LambdaTerm {
	@Override
	public boolean hasLet() {
		return false;
	}

	@Override
	public void accept(TermVisitor termVisitor) {
		termVisitor.visit(this);
	}

	@Override
	public InferenceStep accept(TermVisitorTree termVisitorTree, Map<VarTerm, TypeAbstraction> assumptions, Type type) {
		// todo implment
		return null;
	}
}
