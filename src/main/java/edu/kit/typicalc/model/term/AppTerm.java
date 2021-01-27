package edu.kit.typicalc.model.term;

import java.util.Objects;

public class AppTerm extends LambdaTerm {
	private final LambdaTerm left;
	private final LambdaTerm right;

	public AppTerm(LambdaTerm left, LambdaTerm right) {
		this.left = left;
		this.right = right;
	}

	public LambdaTerm getLeft() {
		return left;
	}

	public LambdaTerm getRight() {
		return right;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AppTerm appTerm = (AppTerm) o;
		return Objects.equals(left, appTerm.left) && Objects.equals(right, appTerm.right);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}
}
