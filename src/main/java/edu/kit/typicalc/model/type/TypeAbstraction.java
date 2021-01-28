package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.TypeVariableFactory;

import java.util.List;

public class TypeAbstraction {
    public TypeAbstraction(Type type, List<TypeVariable> quantifiedVariables) {

    }
    public TypeAbstraction(Type type) {

    }
    public Type instantiate(TypeVariableFactory typeVarFactory) {
        return null;
    }
}
