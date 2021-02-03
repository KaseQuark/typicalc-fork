package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static edu.kit.typicalc.model.type.NamedType.BOOLEAN;
import static edu.kit.typicalc.model.type.NamedType.INT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeAssumptionParserTest {
    @Test
    void simpleType() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("a", "int");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("a"), assumption.getKey());
        assertEquals(new TypeAbstraction(INT, Collections.emptyList()), assumption.getValue());
    }

    @Test
    void functionType() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id", "int -> int");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("id"), assumption.getKey());
        assertEquals(new TypeAbstraction(new FunctionType(INT, INT), Collections.emptyList()), assumption.getValue());

        type = parser.parse("f", "int -> int -> int");
        if (type.isError()) {
            System.err.println(type.unwrapError());
            System.err.println(type.unwrapError().getCause());
        }
        assertTrue(type.isOk());
        types = type.unwrap();
        assertEquals(1, types.size());
        assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("f"), assumption.getKey());
        assertEquals(new TypeAbstraction(
                new FunctionType(
                        INT,
                        new FunctionType(INT, INT)
                ),
                Collections.emptyList()), assumption.getValue());
    }

    @Test
    void complicatedTypes() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(
                "id", "(int -> int) -> (boolean -> boolean)");
        if (type.isError()) {
            System.err.println(type.unwrapError());
            System.err.println(type.unwrapError().getCause());
        }
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("id"), assumption.getKey());
        assertEquals(new TypeAbstraction(
                new FunctionType(
                        new FunctionType(INT, INT),
                        new FunctionType(BOOLEAN, BOOLEAN)
                ),
                Collections.emptyList()), assumption.getValue());
        parser = new TypeAssumptionParser();
        type = parser.parse(
                "id", "((int -> int) -> (boolean -> boolean)) -> ((int2 -> boolean2) -> (boolean2 -> int2))");
        if (type.isError()) {
            System.err.println(type.unwrapError());
            System.err.println(type.unwrapError().getCause());
        }
        assertTrue(type.isOk());
        types = type.unwrap();
        assertEquals(1, types.size());
        assumption = types.entrySet().stream().findFirst().get();
        Type int2 = new NamedType("int2");
        Type boolean2 = new NamedType("boolean2");
        assertEquals(new VarTerm("id"), assumption.getKey());
        assertEquals(new TypeAbstraction(
                new FunctionType(
                new FunctionType(
                        new FunctionType(INT, INT),
                        new FunctionType(BOOLEAN, BOOLEAN)
                ),
                        new FunctionType(
                                new FunctionType(int2, boolean2),
                                new FunctionType(boolean2, int2)
                        )
                ),
                Collections.emptyList()), assumption.getValue());
    }
}
