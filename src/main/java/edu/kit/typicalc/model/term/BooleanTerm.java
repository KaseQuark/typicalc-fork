package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.type.NamedType;

/**
 * Representation of a constant boolean lambda term: either false or true.
 */
public class BooleanTerm extends ConstTerm {
	private final boolean value;

	/**
	 * Initializes a new boolean lambda term with the given value.
	 * @param value true or false
	 */
	public BooleanTerm(boolean value) {
		this.value = value;
	}

	@Override
	public NamedType getType() {
		return NamedType.BOOLEAN;
	}

	@Override
	public boolean hasLet() {
		return false;
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}
