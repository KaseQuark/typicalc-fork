package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.util.Result;

/**
 * Models a simple named type.
 */
public class NamedType extends Type {
	/**
	 * boolean type
	 */
	public static final NamedType BOOLEAN = new NamedType("boolean");
	/**
	 * int type
	 */
	public static final NamedType INT = new NamedType("int");

	private String name;

	/**
	 * Initializes a new NamedType with the given name.
	 * @param name the name of this type
	 */
	public NamedType(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the named type.
	 * @return the name of this type
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks whether some type occurs in this type.
	 * @param x the type to look for
	 * @return whether the specified type is equal to this type
	 */
	public boolean contains(Type x) {
		return this.equals(x);
	}

	/**
	 * Substitutes a type variable for a different type.
	 * @param a the type to replace
	 * @param b the type to insert
	 * @return itself, or b if a is equal to this object
	 */
	@Override
	public Type substitute(TypeVariable a, Type b) {
		//TODO: Methode überhaupt sinnvoll?
		return null;
	}

	/**
	 * Accepts a visitor.
	 * @param typeVisitor the visitor that wants to visit this
	 */
	@Override
	public void accept(TypeVisitor typeVisitor) {
		typeVisitor.visit(this);
	}

	/**
	 * Computes the necessary constraints (and substitution) to unify this type with
	 * another. This method uses the constrainEqualToNamedType method on the other
	 * type.
	 * @param type  the other type
	 * @return unification steps necessary, or an error if that is impossible
	 */
	public Result<UnificationActions, UnificationError> constrainEqualTo(Type type) {
		//TODO
		return null;
	}

	/**
	 * Computes the necessary constraints (and substitution) to unify this type with a
	 * function type.
	 * @param type the function type
	 * @return unification steps necessary, or an error if that is impossible
	 */
	public Result<UnificationActions, UnificationError> constrainEqualToFunction(Type type) {
		//TODO
		return null;
	}

	/**
	 * Computes the necessary constraints (and substitution) to unify this type with a
	 * named type.
	 * @param type the named type
	 * @return unification steps necessary, or an error if that is impossible
	 */
	public Result<UnificationActions, UnificationError> constrainEqualToNamedType(NamedType type) {
		//TODO
		return null;
	}

	/**
	 * Computes the necessary constraints (and substitution) to unify this type with a
	 * type variable.
	 * @param type the type variable
	 * @return the unification steps necessary, or an error if that is impossible
	 */
	public Result<UnificationActions, UnificationError> constrainEqualToVariable(TypeVariable type) {
		//TODO
		return null;
	}
}
