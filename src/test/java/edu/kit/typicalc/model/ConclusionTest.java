package edu.kit.typicalc.model;

import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConclusionTest {

    private static final Map<VarTerm, TypeAbstraction> TYPE_ASSUMPTIONS = new LinkedHashMap<>();
    private static final LambdaTerm LAMBDA_TERM = new VarTerm("var");
    private static final Type TYPE = new NamedType("type");

    @BeforeAll
    static void setUp() {
        TYPE_ASSUMPTIONS.put(new VarTerm("var2"), new TypeAbstraction(new NamedType("type2")));
    }

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(Conclusion.class).usingGetClass().verify();
    }


    @Test
    void getTypeAssumptionsTest() {
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, LAMBDA_TERM, TYPE);
        assertEquals(TYPE_ASSUMPTIONS, conclusion.getTypeAssumptions());
    }

    @Test
    void getLambdaTermTest() {
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, LAMBDA_TERM, TYPE);
        assertEquals(LAMBDA_TERM, conclusion.getLambdaTerm());
    }

    @Test
    void getTypeTest() {
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, LAMBDA_TERM, TYPE);
        assertEquals(TYPE, conclusion.getType());
    }
}
