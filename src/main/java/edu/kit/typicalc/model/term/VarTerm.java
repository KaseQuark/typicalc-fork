package edu.kit.typicalc.model.term;

import java.util.Objects;

public class VarTerm extends LambdaTerm {
	private final String name;

	public VarTerm(String name) {
		super();
		this.name = name;
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
