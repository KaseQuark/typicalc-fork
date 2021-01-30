package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;

/**
 * Provides unique type variables on demand.
 */
public class TypeVariableFactory {

    private static final int FIRST_VARIABLE_INDEX = 1;

    private final TypeVariableKind kind;
    private int nextVariableIndex;

    /**
     * Initializes a new type variable factory for type variables of the given kind.
     *
     * @param kind the kind of the created type variables
     */
    protected TypeVariableFactory(TypeVariableKind kind) {
        this.kind = kind;
        nextVariableIndex = FIRST_VARIABLE_INDEX;
    }

    /**
     * Creates a new unique type variable. This method will never return the same variable twice.
     *
     * @return a new unique type variable
     */
    public TypeVariable nextTypeVariable() {
        TypeVariable nextTypeVariable = new TypeVariable(kind, nextVariableIndex);
        nextVariableIndex++;
        return nextTypeVariable;
    }
}
