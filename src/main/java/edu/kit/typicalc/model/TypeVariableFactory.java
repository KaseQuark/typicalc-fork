package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.TypeVariable;

/**
 * Provides unique type variables on demand.
 */
public class TypeVariableFactory {

    private static final int FIRST_VARIABLE_INDEX = 1;

    private int nextVariableIndex;

    /**
     * Initializes a new type variable factory.
     */
    protected TypeVariableFactory() {
        nextVariableIndex = FIRST_VARIABLE_INDEX;
    }

    /**
     * Creates a new unique type variable. This method will never return the same variable twice.
     *
     * @return a new unique type variable
     */
    public TypeVariable nextTypeVariable() {
        TypeVariable nextTypeVariable = new TypeVariable(nextVariableIndex);
        nextVariableIndex++;
        return nextTypeVariable;
    }
}
