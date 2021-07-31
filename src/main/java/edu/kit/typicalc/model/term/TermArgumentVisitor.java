package edu.kit.typicalc.model.term;

import java.util.LinkedList;
import java.util.Queue;

public class TermArgumentVisitor implements TermVisitor {
    
    private final Queue<LambdaTerm> argumentList = new LinkedList<>();

    public Queue<LambdaTerm> getArguments(LambdaTerm term) {
        term.accept(this);
        return argumentList;
    }
    
    @Override
    public void visit(AppTerm appTerm) {
        argumentList.clear();
        argumentList.add(appTerm.getFunction());
        argumentList.add(appTerm.getParameter());
    }

    @Override
    public void visit(AbsTerm absTerm) {
        argumentList.clear();
        argumentList.add(absTerm.getVariable());
        argumentList.add(absTerm.getInner());
    }

    @Override
    public void visit(LetTerm letTerm) {
        argumentList.clear();
        argumentList.add(letTerm.getVariable());
        argumentList.add(letTerm.getInner());
        argumentList.add(letTerm.getVariableDefinition());
    }

    @Override
    public void visit(VarTerm varTerm) {
        argumentList.clear();
        // no implementation since the VarTerm is its argument
    }

    @Override
    public void visit(IntegerTerm intTerm) {
        argumentList.clear();
        // no implementation since the IntegerTerm is its argument
    }

    @Override
    public void visit(BooleanTerm boolTerm) {
        argumentList.clear();
        // no implementation since the BooleanTerm is its argument
    }

}
