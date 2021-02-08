package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.*;
import edu.kit.typicalc.model.term.IntegerTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.TypeAbstraction;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

class StepFactoryDefaultTest {

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
        premise = new ConstStepDefault(conclusion, constraint);
    }

    @Test
    void createAbsStep() {
        StepFactoryDefault factory = new StepFactoryDefault();
        AbsStepDefault step = factory.createAbsStep(premise, conclusion, constraint);
        assertEquals(premise, step.getPremise());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createAppStep() {
        StepFactoryDefault factory = new StepFactoryDefault();
        AppStepDefault step = factory.createAppStep(premise, premise,  conclusion, constraint);
        assertEquals(premise, step.getPremise1());
        assertEquals(premise, step.getPremise2());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createConstStep() {
        StepFactoryDefault factory = new StepFactoryDefault();
        ConstStepDefault step = factory.createConstStep(conclusion, constraint);
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createVarStep() {
        StepFactoryDefault factory = new StepFactoryDefault();
        VarStepDefault step = factory.createVarStep(typeAbstraction, namedType, conclusion, constraint);
        assertEquals(typeAbstraction, step.getTypeAbsInPremise());
        assertEquals(namedType, step.getInstantiatedTypeAbs());
        assertEquals(conclusion, step.getConclusion());
        assertEquals(constraint, step.getConstraint());
    }

    @Test
    void createLetStep() {
        StepFactoryDefault factory = new StepFactoryDefault();
        boolean thrown = false;
        try {
            LetStep step = factory.createLetStep(conclusion, constraint, premise, null);
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}