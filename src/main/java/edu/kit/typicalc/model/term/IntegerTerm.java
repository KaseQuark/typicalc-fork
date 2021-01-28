package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.type.NamedType;

/**
 * Representation of a constant integer lambda term: e.g. -1, 0 or 16.
 */
public class IntegerTerm extends ConstTerm {
	private final int value;

	/**
	 * Initializes a new integer lambda term with the given value.
	 * @param value an integer
	 */
	public IntegerTerm(int value) {
		this.value = value;
	}

	@Override
	public NamedType getType() {
		return NamedType.INT;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
