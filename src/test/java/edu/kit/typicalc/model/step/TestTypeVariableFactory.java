package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.TypeVariableFactory;
import edu.kit.typicalc.model.type.TypeVariableKind;

public class TestTypeVariableFactory extends TypeVariableFactory {
    public TestTypeVariableFactory() {
        super(TypeVariableKind.USER_INPUT);
    }
}
