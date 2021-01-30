package edu.kit.typicalc.model;

import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConclusionTest {

    private static final Map<VarTerm, TypeAbstraction> typeAssumptions = new HashMap<>();
    private static final LambdaTerm lambdaTerm = new VarTerm("var");
    private static final Type type = new NamedType("type");

    @BeforeAll
    static void setUp() {
        typeAssumptions.put(new VarTerm("var2"), new TypeAbstraction(new NamedType("type2"), new ArrayList<>()));
    }

    @Test
    void getTypeAssumptionsTest() {
        Conclusion conclusion = new Conclusion(typeAssumptions, lambdaTerm, type);
        assertEquals(typeAssumptions, conclusion.getTypeAssumptions());
    }

    @Test
    void getLambdaTermTest() {
        Conclusion conclusion = new Conclusion(typeAssumptions, lambdaTerm, type);
        assertEquals(lambdaTerm, conclusion.getLambdaTerm());
    }

    @Test
    void getTypeTest() {
        Conclusion conclusion = new Conclusion(typeAssumptions, lambdaTerm, type);
        assertEquals(type, conclusion.getType());
    }
}
