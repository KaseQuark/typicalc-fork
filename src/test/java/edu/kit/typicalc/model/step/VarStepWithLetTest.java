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

class VarStepWithLetTest {
    static InferenceStep premise = null;
    static Conclusion conclusion = null;
    static Constraint constraint = null;
    static NamedType namedType = null;
    static TypeAbstraction typeAbstraction = null;
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
    }
    @Test
    void equalsTest() {
        VarStepWithLet step1 = new VarStepWithLet(typeAbstraction, namedType, conclusion, constraint);
        VarStepWithLet step2 = new VarStepWithLet(typeAbstraction, namedType, conclusion, constraint);
        VarStepWithLet step3 = new VarStepWithLet(typeAbstraction, namedType, conclusion, null);
        VarStepWithLet step4 = new VarStepWithLet(null, namedType, conclusion, constraint);
        VarStepWithLet step5 = new VarStepWithLet(typeAbstraction, null, conclusion, constraint);

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
        VarStepWithLet step1 = new VarStepWithLet(typeAbstraction, namedType, conclusion, constraint);
        VarStepWithLet step2 = new VarStepWithLet(typeAbstraction, namedType, conclusion, constraint);

        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        VarStepWithLet step = new VarStepWithLet(typeAbstraction, namedType, conclusion, constraint);
        step.accept(testStepVisitor);
        assertEquals("VarLet", testStepVisitor.visited);
    }
}