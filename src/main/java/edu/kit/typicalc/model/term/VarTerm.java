package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;
import java.util.Objects;

public class VarTerm extends LambdaTerm {
	private final String name;

	public VarTerm(String name) {
		super();
		this.name = name;
	}

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
		//todo implement
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VarTerm varTerm = (VarTerm) o;
		return Objects.equals(name, varTerm.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
