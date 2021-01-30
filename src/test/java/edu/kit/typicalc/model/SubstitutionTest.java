package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVaribaleKind;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubstitutionTest {

    private static final TypeVariable var = new TypeVariable(TypeVaribaleKind.USER_INPUT, 1);
    private static final Type type = new TypeVariable(TypeVaribaleKind.TREE, 2);

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(Substitution.class).usingGetClass().verify();
    }

    @Test
    void getVariableTest() {
        Substitution sub = new Substitution(var,type);
        assertEquals(var, sub.getVariable());
    }

    @Test
    void getTypeTest() {
        Substitution sub = new Substitution(var,type);
        assertEquals(type, sub.getType());
    }
}
