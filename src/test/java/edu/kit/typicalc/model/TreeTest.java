package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.AbsStepDefault;
import edu.kit.typicalc.model.step.ConstStepDefault;
import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.step.VarStepDefault;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.ConstTerm;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeTest {

    private static final Map<VarTerm, TypeAbstraction> TYPE_ASSUMPTIONS = new HashMap<>();
    private static final ConstTerm CONST = new IntegerTerm(10);
    private static final VarTerm VAR = new VarTerm("var");
    private static final AbsTerm ABS = new AbsTerm(VAR, VAR);
    private static final NamedType TYPE = new NamedType("type");
    private static final TypeAbstraction TYPE_ABS = new TypeAbstraction(TYPE);

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
    void visitAbs() {
        TypeVariable variable2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable variable3 = new TypeVariable(TypeVariableKind.TREE, 3);

        Tree tree = new Tree(TYPE_ASSUMPTIONS, ABS);
        Map<VarTerm, TypeAbstraction> varTypeAss = new HashMap<>(TYPE_ASSUMPTIONS);
        varTypeAss.put(VAR, new TypeAbstraction(variable2));
        Conclusion varConclusion = new Conclusion(varTypeAss, VAR, variable3);
        InferenceStep varStep = new VarStepDefault(new TypeAbstraction(variable2), variable2, varConclusion,
                new Constraint(variable2, variable3));

        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, ABS, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new AbsStepDefault(varStep, conclusion,
                new Constraint(tree.getFirstTypeVariable(), new FunctionType(variable2, variable3)));
        assertEquals(expectedStep, tree.getFirstInferenceStep());
    }

    @Test
    void visitConst() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, CONST);
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, CONST, tree.getFirstTypeVariable());
        Constraint constraint = new Constraint(NamedType.INT, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new ConstStepDefault(conclusion, constraint);
        assertEquals(expectedStep, tree.getFirstInferenceStep());
        assertEquals(Collections.singletonList(constraint), tree.getConstraints());
    }

    @Test
    void visitVar() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR);
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, VAR, tree.getFirstTypeVariable());
        Constraint constraint = new Constraint(TYPE, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new VarStepDefault(TYPE_ABS, TYPE, conclusion, constraint);
        assertEquals(expectedStep, tree.getFirstInferenceStep());
        assertEquals(Collections.singletonList(constraint), tree.getConstraints());
    }
}
