package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.step.VarStepDefault;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeTest {

    private static final Map<VarTerm, TypeAbstraction> TYPE_ASSUMPTIONS = new HashMap<>();
    private static final VarTerm VAR = new VarTerm("var");
    private static final NamedType TYPE = new NamedType("type");
    private static final TypeAbstraction TYPE_ABS = new TypeAbstraction(TYPE, new ArrayList<>());

    @BeforeAll
    static void setUp() {
        TYPE_ASSUMPTIONS.put(VAR, TYPE_ABS);
    }

    @Test
    void firstTypeVariableNewFactory() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR);
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
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR, factory);
        assertEquals(tree.getFirstTypeVariable(), factoryRef.nextTypeVariable());
    }

    @Test
    void missingTypeAssumptionForVar() {
        Map<VarTerm, TypeAbstraction> ass = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> {
                new Tree(ass, VAR);
        });
    }

    @Test
    void visitVar() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR);
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, VAR, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new VarStepDefault(TYPE_ABS, TYPE, conclusion,
                new Constraint(TYPE, tree.getFirstTypeVariable()));
        assertEquals(expectedStep, tree.getFirstInferenceStep());
    }
}
