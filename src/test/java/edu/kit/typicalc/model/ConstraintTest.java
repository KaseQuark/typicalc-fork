package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstraintTest {

    private static final Type TYPE_1 = new TypeVariable(TypeVariableKind.USER_INPUT, 1);
    private static final Type TYPE_2 = new TypeVariable(TypeVariableKind.TREE, 2);

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(Constraint.class).usingGetClass().withIgnoredFields("stepIndex").verify();
    }

    @Test
    void getFirstTest() {
        Constraint con = new Constraint(TYPE_1, TYPE_2);
        assertEquals(TYPE_1, con.getFirstType());
    }

    @Test
    void getSecondTest() {
        Constraint con = new Constraint(TYPE_1, TYPE_2);
        assertEquals(TYPE_2, con.getSecondType());
    }
}
