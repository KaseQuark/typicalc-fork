package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeInferenceResultTest {

    private static final TypeVariable A_1 = new TypeVariable(TypeVariableKind.TREE, 1);
    private static final TypeVariable A_2 = new TypeVariable(TypeVariableKind.TREE, 2);
    private static final TypeVariable A_3 = new TypeVariable(TypeVariableKind.TREE, 3);
    private static final TypeVariable A_4 = new TypeVariable(TypeVariableKind.TREE, 4);
    private static final TypeVariable A_5 = new TypeVariable(TypeVariableKind.TREE, 5);
    private static final TypeVariable A_6 = new TypeVariable(TypeVariableKind.TREE, 6);
    private static final TypeVariable A_7 = new TypeVariable(TypeVariableKind.TREE, 7);

    @Test
    void getMGU() {
        // rectified example from introduction slides
        List<Substitution> substitutions = new ArrayList<>(Arrays.asList(
                new Substitution(A_7, A_2),
                new Substitution(A_4, new FunctionType(A_7, A_5)),
                new Substitution(A_6, new FunctionType(A_7, A_5)),
                new Substitution(A_3, new FunctionType(A_4, A_5)),
                new Substitution(A_1, new FunctionType(A_2, A_3))
        ));
        TypeInferenceResult result = new TypeInferenceResult(substitutions, A_1);

        List<Substitution> expectedMGU = new ArrayList<>(Arrays.asList(
                new Substitution(A_1, new FunctionType(A_2, new FunctionType(new FunctionType(A_2, A_5), A_5))),
                new Substitution(A_3, new FunctionType(new FunctionType(A_2, A_5), A_5)),
                new Substitution(A_4, new FunctionType(A_2, A_5)),
                new Substitution(A_6, new FunctionType(A_2, A_5)),
                new Substitution(A_7, A_2)
        ));

        assertEquals(expectedMGU, result.getMGU());

        // tests if mgu-generation works for substitutions given in reverse order
        List<Substitution> substitutions2 = new ArrayList<>(Arrays.asList(
                new Substitution(A_1, new FunctionType(A_2, A_3)),
                new Substitution(A_3, new FunctionType(A_4, A_5)),
                new Substitution(A_6, new FunctionType(A_7, A_5)),
                new Substitution(A_4, new FunctionType(A_7, A_5)),
                new Substitution(A_7, A_2)
        ));
        TypeInferenceResult result2 = new TypeInferenceResult(substitutions2, A_1);

        assertEquals(expectedMGU, result2.getMGU());
    }

    @Test
    void getType() {
        List<Substitution> substitutions = new ArrayList<>(Arrays.asList(
                new Substitution(A_7, A_2),
                new Substitution(A_4, new FunctionType(A_7, A_5)),
                new Substitution(A_6, new FunctionType(A_7, A_5)),
                new Substitution(A_3, new FunctionType(A_4, A_5)),
                new Substitution(A_1, new FunctionType(A_2, A_3))
        ));
        TypeInferenceResult result = new TypeInferenceResult(substitutions, A_1);

        Type expectedType = new FunctionType(A_2, new FunctionType(new FunctionType(A_2, A_5), A_5));
        assertEquals(expectedType, result.getType());
    }
}
