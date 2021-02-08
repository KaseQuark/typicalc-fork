package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.TypeAbstraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LetStepDefaultTest {
    static InferenceStep premise = null;
    static Conclusion conclusion = null;
    static Constraint constraint = null;
    static NamedType namedType = null;
    static TypeAbstraction typeAbstraction = null;
    static TestTypeInfererLet typeInferer = null;
    @BeforeAll
    static void setUp() {
        Map<VarTerm, TypeAbstraction> map = new HashMap<>();
        VarTerm term = new VarTerm("test");
        namedType = new NamedType("testType");
        typeAbstraction = new TypeAbstraction(namedType);
        map.put(term, typeAbstraction);
        IntegerTerm integerTerm = new IntegerTerm(1);
        conclusion = new Conclusion(map, integerTerm, namedType);
        NamedType type1 = new NamedType("a");
        NamedType type2 = new NamedType("b");
        constraint = new Constraint(type1, type2);
        premise = new ConstStepDefault(conclusion, constraint);
        typeInferer = new TestTypeInfererLet(integerTerm, map, new TestTypeVariableFactory());
    }
    @Test
    void equalsTest() {
        LetStepDefault step1 = new LetStepDefault(conclusion, constraint, premise, typeInferer);
        LetStepDefault step2 = new LetStepDefault(conclusion, constraint, premise, typeInferer);
        LetStepDefault step3 = new LetStepDefault(conclusion, null, premise, typeInferer);
        LetStepDefault step4 = new LetStepDefault(conclusion, constraint, null, typeInferer);
        LetStepDefault step5 = new LetStepDefault(conclusion, constraint, premise, null);

        assertEquals(step1, step1);
        assertEquals(step1, step2);
        assertNotEquals(new EmptyStep(), step1);
        assertNotEquals(null, step1);
        assertNotEquals(step1, step3);
        assertNotEquals(step1, step4);
        assertNotEquals(step1, step5);

    }
    @Test
    void hashCodeTest() {
        LetStepDefault step1 = new LetStepDefault(conclusion, constraint, premise, typeInferer);
        LetStepDefault step2 = new LetStepDefault(conclusion, constraint, premise, typeInferer);

        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        LetStepDefault step = new LetStepDefault(conclusion, constraint, premise, typeInferer);
        step.accept(testStepVisitor);
        assertEquals("LetDef", testStepVisitor.visited);
    }
}