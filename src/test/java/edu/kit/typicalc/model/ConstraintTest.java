package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVaribaleKind;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstraintTest {

    private static final Type type1 = new TypeVariable(TypeVaribaleKind.USER_INPUT, 1);
    private static final Type type2 = new TypeVariable(TypeVaribaleKind.TREE, 2);

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(Constraint.class).usingGetClass().verify();
    }

    @Test
    void getFirstTest() {
        Constraint con = new Constraint(type1,type2);
        assertEquals(type1, con.getFirstType());
    }

    @Test
    void getSecondTest() {
        Constraint con = new Constraint(type1,type2);
        assertEquals(type2, con.getSecondType());
    }
}
