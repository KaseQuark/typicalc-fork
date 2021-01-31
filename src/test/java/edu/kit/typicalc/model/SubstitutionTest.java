package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubstitutionTest {

    private static final TypeVariable VAR = new TypeVariable(TypeVariableKind.USER_INPUT, 1);
    private static final Type TYPE = new TypeVariable(TypeVariableKind.TREE, 2);

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(Substitution.class).usingGetClass().verify();
    }

    @Test
    void getVariableTest() {
        Substitution sub = new Substitution(VAR, TYPE);
        assertEquals(VAR, sub.getVariable());
    }

    @Test
    void getTypeTest() {
        Substitution sub = new Substitution(VAR, TYPE);
        assertEquals(TYPE, sub.getType());
    }
}
