package edu.kit.typicalc.model.term;

public class ConstTerm extends LambdaTerm {
	@Override
	public boolean hasLet() {
		return false;
	}

	@Override
	public void accept(TermVisitor termVisitor) {
		termVisitor.visit(this);
	}
}
