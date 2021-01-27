package edu.kit.typicalc.model.type;

import com.fasterxml.jackson.databind.jsontype.NamedType;

public interface TypeVisitor {
	/**
	 * Visit a named type.
	 * @param named the type
	 */
	void visit(NamedType named);

	/**
	 * Visit a type variable
	 * @param variable the variable
	 */
	void visit(TypeVariable variable);

	/**
	 * Visit a function.
	 * @param function the function
	 */
	void visit(FunctionType function);
}
