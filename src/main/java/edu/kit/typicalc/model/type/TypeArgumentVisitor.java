package edu.kit.typicalc.model.type;

import java.util.LinkedList;
import java.util.Queue;

public class TypeArgumentVisitor implements TypeVisitor {
    
    private final Queue<Type> argumentList = new LinkedList<>();
    
    public Queue<Type> getArguments(Type type) {
        type.accept(this);
        return argumentList;
    }

    @Override
    public void visit(NamedType named) {
        argumentList.clear();
        // no implementation since the NamedType is its argument
    }

    @Override
    public void visit(TypeVariable variable) {
        argumentList.clear();
        // no implementation since the TypeVariable is its argument
    }

    @Override
    public void visit(FunctionType function) {
        argumentList.clear();
        argumentList.add(function.getParameter());
        argumentList.add(function.getOutput());
    }

}
