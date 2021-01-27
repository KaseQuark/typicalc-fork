package edu.kit.typicalc.model.term;

import java.util.Objects;

public class AbsTerm extends LambdaTerm {
	private final VarTerm var;
	private final LambdaTerm body;

	public AbsTerm(VarTerm var, LambdaTerm body) {
		this.var = var;
		this.body = body;
	}

	public VarTerm getVariable() {
		return var;
	}

	public LambdaTerm getInner() {
		return body;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AbsTerm absTerm = (AbsTerm) o;
		return Objects.equals(var, absTerm.var) && Objects.equals(body, absTerm.body);
	}

	@Override
	public int hashCode() {
		return Objects.hash(var, body);
	}
}
