package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.TypeAbstraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ConstStepDefaultTest {
    static Conclusion conclusion = null;
    static Constraint constraint = null;
    static NamedType namedType = null;
    static TypeAbstraction typeAbstraction = null;

    @BeforeAll
    static void setUp() {
        Map<VarTerm, TypeAbstraction> map = new LinkedHashMap<>();
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
        ConstStepDefault step1 = new ConstStepDefault(conclusion, constraint, 0);
        ConstStepDefault step2 = new ConstStepDefault(conclusion, constraint, 0);

        assertEquals(step1, step1);
        assertEquals(step1, step2);
        assertNotEquals(step1, new EmptyStep());

    }

    @Test
    void hashCodeTest() {
        ConstStepDefault step1 = new ConstStepDefault(conclusion, constraint, 0);
        ConstStepDefault step2 = new ConstStepDefault(conclusion, constraint, 0);

        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        ConstStepDefault step = new ConstStepDefault(conclusion, constraint, 0);
        step.accept(testStepVisitor);
        assertEquals("ConstDef", testStepVisitor.visited);
    }

}