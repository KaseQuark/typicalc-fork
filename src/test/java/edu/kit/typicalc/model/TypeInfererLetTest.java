package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.step.VarStepWithLet;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeInfererLetTest {

    @Test
    void inferX() {
        VarTerm x = new VarTerm("x");
        TypeVariable generated1 = new TypeVariable(TypeVariableKind.GENERATED_TYPE_ASSUMPTION, 1);
        TypeVariable variable2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeAbstraction generated1Abs = new TypeAbstraction(generated1);

        Map<VarTerm, TypeAbstraction> typeAssumptions = new LinkedHashMap<>();
        typeAssumptions.put(x, generated1Abs);

        TypeVariableFactory refFac = new TypeVariableFactory(TypeVariableKind.TREE);
        refFac.nextTypeVariable();
        TypeInfererLet typeInfererLet = new TypeInfererLet(x, typeAssumptions, refFac);

        Conclusion varLeftConclusion = new Conclusion(typeAssumptions, x, variable2);
        Constraint varLeftConstraint = new Constraint(variable2, generated1);
        InferenceStep varLeftStep = new VarStepWithLet(generated1Abs, generated1,
                varLeftConclusion, varLeftConstraint);

        assertEquals(varLeftStep, typeInfererLet.getFirstInferenceStep());
        assertTrue(typeInfererLet.getType().isPresent());
        assertEquals(generated1, typeInfererLet.getType().get());
        List<Substitution> expectedMGU
                = new ArrayList<>(Collections.singletonList(new Substitution(variable2, generated1)));
        assertTrue(typeInfererLet.getMGU().isPresent());
        assertEquals(expectedMGU, typeInfererLet.getMGU().get());
        List<Constraint> expectedLetConstraints
                = new ArrayList<>(Collections.singletonList(new Constraint(variable2, generated1)));
        assertEquals(expectedLetConstraints, typeInfererLet.getLetConstraints());
    }

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(TypeInfererLet.class).usingGetClass().verify();
    }
}
