package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static edu.kit.typicalc.model.type.NamedType.BOOLEAN;
import static edu.kit.typicalc.model.type.NamedType.INT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeAssumptionParserTest {
    @Test
    void simpleType() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("a", "int");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("a"), assumption.getKey());
        assertEquals(new TypeAbstraction(INT), assumption.getValue());
    }

    @Test
    void typeVariablesOneDigitIndex() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("x", "t1");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption.getKey());
        assertEquals(new TypeAbstraction(new TypeVariable(TypeVariableKind.USER_INPUT, 1)), assumption.getValue());

        HashMap<String, String> assumptions2 = new HashMap<>();
        assumptions2.put("x", "t001");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type2 = parser.parse(assumptions2);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types2 = type2.unwrap();
        assertEquals(1, types2.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption2 = types2.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption2.getKey());
        assertEquals(new TypeAbstraction(new TypeVariable(TypeVariableKind.USER_INPUT, 1)), assumption2.getValue());
    }

    @Test
    void typeVariablesMultipleDigitIndex() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("x", "t123456");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption.getKey());
        assertEquals(new TypeAbstraction(new TypeVariable(TypeVariableKind.USER_INPUT, 123456)), assumption.getValue());
    }

    @Test
    void namedTypeStartingWithT() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("x", "tau1");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption.getKey());
        assertEquals(new TypeAbstraction(new NamedType("tau1")), assumption.getValue());
    }

    @Test
    void functionType() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("id", "int -> int");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("id"), assumption.getKey());
        assertEquals(new TypeAbstraction(new FunctionType(INT, INT)), assumption.getValue());

        HashMap<String, String> assumptions2 = new HashMap<>();
        assumptions2.put("f", "int -> int -> int");
        type = parser.parse(assumptions2);
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
                )), assumption.getValue());
    }

    @Test
    void functionTypeWithVariables() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("fun", "t0 -> t0");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("fun"), assumption.getKey());
        TypeVariable expectedVar = new TypeVariable(TypeVariableKind.USER_INPUT, 0);
        assertEquals(new TypeAbstraction(new FunctionType(expectedVar, expectedVar)), assumption.getValue());
    }

    @Test
    void complicatedTypes() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("id", "(int -> int) -> (boolean -> boolean)");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
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
                )), assumption.getValue());
        parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions2 = new HashMap<>();
        assumptions2.put("id", "((int -> int) -> (boolean -> boolean)) -> ((int2 -> boolean2) -> (boolean2 -> int2))");
        type = parser.parse(assumptions2);
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
                )), assumption.getValue());
    }

    @Test
    void longFunction() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        HashMap<String, String> assumptions = new HashMap<>();
        assumptions.put("fun", "(a -> b -> c) -> d");
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse(assumptions);
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("fun"), assumption.getKey());
        assertEquals(new TypeAbstraction(
                new FunctionType(
                new FunctionType(new NamedType("a"), new FunctionType(new NamedType("b"), new NamedType("c"))),
                new NamedType("d")
                )), assumption.getValue());
    }
}
