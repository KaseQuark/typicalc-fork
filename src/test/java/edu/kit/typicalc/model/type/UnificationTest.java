package edu.kit.typicalc.model.type;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;
import edu.kit.typicalc.model.UnificationError;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnificationTest {
    @Test
    void all() {
        TypeVariable a1 = new TypeVariable(TypeVaribaleKind.TREE, 1);
        TypeVariable a2 = new TypeVariable(TypeVaribaleKind.TREE, 2);
        TypeVariable a3 = new TypeVariable(TypeVaribaleKind.TREE, 3);
        FunctionType id = new FunctionType(a1, a1);
        FunctionType fun = new FunctionType(a2, a3);
        NamedType string = new NamedType("string");
        NamedType object = new NamedType("object");

        // Function constraints
        UnificationError error = id.constrainEqualTo(a1).unwrapError();
        assertEquals(UnificationError.INFINITE_TYPE, error);

        UnificationActions actions = id.constrainEqualTo(a2).unwrap();
        assertEquals(new Substitution(a2, id), actions.getSubstitution().get());
        assertTrue(actions.getConstraints().isEmpty());

        error = id.constrainEqualTo(string).unwrapError();
        assertEquals(UnificationError.DIFFERENT_TYPES, error);

        actions = id.constrainEqualTo(fun).unwrap();
        assertTrue(actions.getSubstitution().isEmpty());
        Collection<Constraint> constraints = actions.getConstraints();
        assertEquals(2, constraints.size());
        assertTrue(constraints.contains(new Constraint(a1, a2)));
        assertTrue(constraints.contains(new Constraint(a1, a3)));

        // Variable constraints
        actions = a1.constrainEqualTo(a1).unwrap();
        assertTrue(actions.getConstraints().isEmpty());
        assertTrue(actions.getSubstitution().isEmpty());

        actions = a1.constrainEqualTo(a2).unwrap();
        assertTrue(actions.getConstraints().isEmpty());
        assertEquals(new Substitution(a1, a2), actions.getSubstitution().get());

        error = a1.constrainEqualTo(id).unwrapError();
        assertEquals(UnificationError.INFINITE_TYPE, error);

        actions = a2.constrainEqualTo(id).unwrap();
        assertEquals(new Substitution(a2, id), actions.getSubstitution().get());
        assertTrue(actions.getConstraints().isEmpty());

        actions = a1.constrainEqualTo(string).unwrap();
        assertTrue(actions.getConstraints().isEmpty());
        assertEquals(new Substitution(a1, string), actions.getSubstitution().get());

        // Named type constraints
        actions = string.constrainEqualTo(string).unwrap();
        assertTrue(actions.getConstraints().isEmpty());
        assertTrue(actions.getSubstitution().isEmpty());

        error = string.constrainEqualTo(object).unwrapError();
        assertEquals(UnificationError.DIFFERENT_TYPES, error);

        error = string.constrainEqualTo(id).unwrapError();
        assertEquals(UnificationError.DIFFERENT_TYPES, error);

        actions = string.constrainEqualTo(a1).unwrap();
        assertTrue(actions.getConstraints().isEmpty());
        assertEquals(new Substitution(a1, string), actions.getSubstitution().get());
    }
}
