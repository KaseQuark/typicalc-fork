package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TypeVariableFactoryTest {

    @Test
    void createGivenKind() {
        for (TypeVariableKind kind : TypeVariableKind.values()) {
            TypeVariableFactory factory = new TypeVariableFactory(kind);
            assertEquals(kind, factory.nextTypeVariable().getKind());
        }
    }

    @Test
    void createDistinctTypeVariables100() {
        TypeVariableFactory factory = new TypeVariableFactory(TypeVariableKind.TREE);

        TypeVariable[] typeVariables = new TypeVariable[100];
        for (int i = 0; i < 100; i++) {
            typeVariables[i] = factory.nextTypeVariable();
        }

        for (int i = 0; i < 100; i++) {
            for (int j = i + 1; j < 100; j++) {
                assertNotEquals(typeVariables[i], typeVariables[j]);
            }
        }
    }
}
