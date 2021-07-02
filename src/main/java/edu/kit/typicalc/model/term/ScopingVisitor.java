package edu.kit.typicalc.model.term;

import java.util.HashMap;
import java.util.Map;

/**
 * Visitor to add unique indices to variables of the same name.
 *
 * @see VarTerm#uniqueIndex()
 */
public class ScopingVisitor implements TermVisitor {
    private int counter = 0;
    private Map<String, Integer> index = new HashMap<>();

    public ScopingVisitor() {

    }

    private ScopingVisitor(int counter, Map<String, Integer> index) {
        this.counter = counter;
        this.index = index;
    }

    @Override
    public void visit(AppTerm appTerm) {
        ScopingVisitor a = new ScopingVisitor(counter, new HashMap<>(index));
        appTerm.getFunction().accept(a);
        this.counter = a.counter;
        appTerm.getParameter().accept(this);
    }

    @Override
    public void visit(AbsTerm absTerm) {
        VarTerm v = absTerm.getVariable();
        v.setUniqueIndex(counter++);
        index.put(v.getName(), v.uniqueIndex());
        absTerm.getInner().accept(this);
    }

    @Override
    public void visit(LetTerm letTerm) {
        VarTerm v = letTerm.getVariable();
        v.setUniqueIndex(counter++);
        letTerm.getVariableDefinition().accept(this);
        index.put(v.getName(), v.uniqueIndex());
        letTerm.getInner().accept(this);
    }

    @Override
    public void visit(VarTerm varTerm) {
        if (index.containsKey(varTerm.getName())) {
            varTerm.setUniqueIndex(index.get(varTerm.getName()));
        } // else: unscoped variables share a single type variable
    }

    @Override
    public void visit(IntegerTerm intTerm) {
    }

    @Override
    public void visit(BooleanTerm boolTerm) {
    }
}
