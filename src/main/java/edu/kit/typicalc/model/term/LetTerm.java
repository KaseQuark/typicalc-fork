package edu.kit.typicalc.model.term;

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
}
