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
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepFactoryDefaultTest {

    static InferenceStep premise = null;
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
        premise = new ConstStepDefault(conclusion, constraint, 0);
    }

    @Test
    void createAbsStepTest() {
        StepFactoryDefault factory = new StepFactoryDefault();
        AbsStepDefault step = factory.createAbsStep(premise, conclusion, constraint, 0);
        assertEquals(premise, step.getPremise());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createAppStepTest() {
        StepFactoryDefault factory = new StepFactoryDefault();
        AppStepDefault step = factory.createAppStep(premise, premise,  conclusion, constraint, 0);
        assertEquals(premise, step.getPremise1());
        assertEquals(premise, step.getPremise2());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createConstStepTest() {
        StepFactoryDefault factory = new StepFactoryDefault();
        ConstStepDefault step = factory.createConstStep(conclusion, constraint, 0);
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createVarStepTest() {
        StepFactoryDefault factory = new StepFactoryDefault();
        VarStepDefault step = factory.createVarStep(typeAbstraction, namedType, conclusion, constraint, 0);
        assertEquals(typeAbstraction, step.getTypeAbsInPremise());
        assertEquals(namedType, step.getInstantiatedTypeAbs());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

}