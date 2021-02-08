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

class AppStepDefaultTest {
    static InferenceStep premise1 = null;
    static InferenceStep premise2 = null;
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
        premise1 = new ConstStepDefault(conclusion, constraint);
        premise2 = new ConstStepDefault(conclusion, constraint);
    }
    @Test
    void equalsTest() {
        AppStepDefault step1 = new AppStepDefault(premise1, premise2, conclusion, constraint);
        AppStepDefault step2 = new AppStepDefault(premise1, premise2, conclusion, constraint);
        AppStepDefault step3 = new AppStepDefault(premise1, premise2, conclusion, null);

        assertEquals(step1, step1);
        assertEquals(step1, step2);
        assertNotEquals(step1, premise1);
        assertNotEquals(step1, step3);

    }
    @Test
    void hashCodeTest() {
        AppStepDefault step1 = new AppStepDefault(premise1, premise2, conclusion, constraint);
        AppStepDefault step2 = new AppStepDefault(premise1, premise2, conclusion, constraint);

        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        AppStepDefault step = new AppStepDefault(premise1, premise2, conclusion, constraint);
        step.accept(testStepVisitor);
        assertEquals("AppDef", testStepVisitor.visited);
    }
}