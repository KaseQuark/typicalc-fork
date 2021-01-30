package edu.kit.typicalc.model;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeVariableKind;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    private static final Map<VarTerm, TypeAbstraction> typeAssumptions = new HashMap<>();

    @BeforeAll
    static void setUp() {
        typeAssumptions.put(new VarTerm("var"), new TypeAbstraction(new NamedType("type"), new ArrayList<>()));
    }

    @Test
    void firstTypeVariableNewFactory() {
        Tree tree = new Tree(typeAssumptions, new VarTerm("var"));
        assertEquals(tree.getFirstTypeVariable(), new TypeVariableFactory(TypeVariableKind.TREE).nextTypeVariable());
    }

    @Test
    void firstTypeVariableGivenFactory() {
        TypeVariableFactory factory = new TypeVariableFactory(TypeVariableKind.TREE);
        TypeVariableFactory factoryRef = new TypeVariableFactory(TypeVariableKind.TREE);
        for (int i = 0; i < 10; i++) {
            factory.nextTypeVariable();
            factoryRef.nextTypeVariable();
        }
        Tree tree = new Tree(typeAssumptions, new VarTerm("var"), factory);
        assertEquals(tree.getFirstTypeVariable(), factoryRef.nextTypeVariable());
    }
}
