package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.StepNumberFactory;
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

class LetStepDefaultTest {
    static InferenceStep premise = null;
    static Conclusion conclusion = null;
    static Constraint constraint = null;
    static NamedType namedType = null;
    static TypeAbstraction typeAbstraction = null;
    static TestTypeInfererLet typeInferer = null;
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
        premise = new ConstStepDefault(conclusion, constraint, 0);
        typeInferer = new TestTypeInfererLet(integerTerm, map, new TestTypeVariableFactory(), new StepNumberFactory());
    }
    @Test
    void equalsTest() {
        LetStepDefault step1 = new LetStepDefault(conclusion, constraint, premise, typeInferer, 0);
        LetStepDefault step2 = new LetStepDefault(conclusion, constraint, premise, typeInferer, 0);

        assertEquals(step1, step1);
        assertEquals(step1, step2);
        assertNotEquals(new EmptyStep(), step1);

    }
    @Test
    void hashCodeTest() {
        LetStepDefault step1 = new LetStepDefault(conclusion, constraint, premise, typeInferer, 0);
        LetStepDefault step2 = new LetStepDefault(conclusion, constraint, premise, typeInferer, 0);

        assertEquals(step1.hashCode(), step2.hashCode());
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        LetStepDefault step = new LetStepDefault(conclusion, constraint, premise, typeInferer, 0);
        step.accept(testStepVisitor);
        assertEquals("LetDef", testStepVisitor.visited);
    }
}