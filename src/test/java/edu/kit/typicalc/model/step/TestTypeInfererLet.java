package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.TypeInfererLet;
import edu.kit.typicalc.model.TypeVariableFactory;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

public class TestTypeInfererLet extends TypeInfererLet {
    public TestTypeInfererLet(LambdaTerm term, Map<VarTerm, TypeAbstraction> map, TypeVariableFactory factory) {
        super(term, map, factory);
    }
}
