package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

import java.util.*;

import static edu.kit.typicalc.model.type.NamedType.BOOLEAN;
import static edu.kit.typicalc.model.type.NamedType.INT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeAssumptionParserTest {
    @Test
    void simpleType() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("a: int");
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("x: t1");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption.getKey());
        assertEquals(new TypeAbstraction(new TypeVariable(TypeVariableKind.USER_INPUT, 1)), assumption.getValue());

        Result<Map<VarTerm, TypeAbstraction>, ParseError> type2 = parser.parse("x: t009");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types2 = type2.unwrap();
        assertEquals(1, types2.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption2 = types2.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("x"), assumption2.getKey());
        assertEquals(new TypeAbstraction(new TypeVariable(TypeVariableKind.USER_INPUT, 9)), assumption2.getValue());
    }

    @Test
    void typeVariablesMultipleDigitIndex() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("x: t123456");
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("x: tau1");
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id: int -> int");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("id"), assumption.getKey());
        assertEquals(new TypeAbstraction(new FunctionType(INT, INT)), assumption.getValue());
    }

    @Test
    void functionType2() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("f: int -> int -> int");
        if (type.isError()) {
            System.err.println(type.unwrapError());
            System.err.println(type.unwrapError().getCause());
        }
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("fun: t0 -> t0");
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id: (int -> int) -> (boolean -> boolean)");
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
        type = parser.parse("id: ((int -> int) -> (boolean -> boolean)) -> ((int2 -> boolean2) -> (boolean2 -> int2))");
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
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("fun: (a -> b -> c) -> d");
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

    @Test
    void allQuantified() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id: ∀ t1 . t1 -> t1");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(1, types.size());
        Map.Entry<VarTerm, TypeAbstraction> assumption = types.entrySet().stream().findFirst().get();
        assertEquals(new VarTerm("id"), assumption.getKey());
        Set<TypeVariable> quantified = new HashSet<>();
        quantified.add(new TypeVariable(TypeVariableKind.USER_INPUT, 1));
        assertEquals(new TypeAbstraction(
                new FunctionType(
                        new TypeVariable(TypeVariableKind.USER_INPUT, 1),
                        new TypeVariable(TypeVariableKind.USER_INPUT, 1)
                ), quantified), assumption.getValue());
    }

    @Test
    void allQuantifiedCommaSeparation() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("transmute: ∀ t1, t2 . t1 -> t2, createAny: ∀ t1 . t1 ");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> types = type.unwrap();
        assertEquals(2, types.size());
        Set<TypeVariable> quantified = new HashSet<>();
        quantified.add(new TypeVariable(TypeVariableKind.USER_INPUT, 1));
        quantified.add(new TypeVariable(TypeVariableKind.USER_INPUT, 2));
        assertEquals(new TypeAbstraction(
                new FunctionType(
                        new TypeVariable(TypeVariableKind.USER_INPUT, 1),
                        new TypeVariable(TypeVariableKind.USER_INPUT, 2)
                ), quantified), types.get(new VarTerm("transmute")));
        quantified.remove(new TypeVariable(TypeVariableKind.USER_INPUT, 2));
        assertEquals(new TypeAbstraction(
                new TypeVariable(TypeVariableKind.USER_INPUT, 1),
                quantified), types.get(new VarTerm("createAny")));
    }

    @Test
    void usefulErrors() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id: ∀ t1");
        assertTrue(type.isError());
        ParseError error = type.unwrapError();
        assertEquals(ParseError.ErrorCause.UNEXPECTED_TOKEN, error.getCauseEnum());
        assertEquals(Token.TokenType.EOF, error.getCause().get().getType());
        Collection<Token.TokenType> expected = error.getExpected().get();
        assertEquals(2, expected.size());
        assertTrue(expected.contains(Token.TokenType.DOT));
        assertTrue(expected.contains(Token.TokenType.COMMA));
    }

    @Test
    void doesntAcceptDuplicateQuantifiedVariables() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("id: ∀ t1, t1 : t1 -> t1");
        assertTrue(type.isError());
        ParseError error = type.unwrapError();
        assertEquals(ParseError.ErrorCause.UNEXPECTED_TOKEN, error.getCauseEnum());
        assertEquals(Token.TokenType.VARIABLE, error.getCause().get().getType());
        assertEquals(10, error.getCause().get().getPos());
    }

    @Test
    void correctlyScopesQuantifiedTypeVariables() {
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type =
                parser.parse("id: ∀ t1 . t1 -> t1, fun: ∀t1,t2.t1->t2 ");
        assertTrue(type.isOk());
        Map<VarTerm, TypeAbstraction> res = type.unwrap();
        TypeAbstraction id = res.get(new VarTerm("id"));
        TypeVariable[] idVariables = id.getQuantifiedVariables().toArray(new TypeVariable[0]);
        assertEquals(0, idVariables[0].getUniqueIndex());
        TypeAbstraction fun = res.get(new VarTerm("fun"));
        TypeVariable[] funVariables = fun.getQuantifiedVariables().toArray(new TypeVariable[0]);
        assertEquals(1, funVariables[0].getUniqueIndex());
        assertEquals(1, funVariables[1].getUniqueIndex());
        assertEquals(1, ((TypeVariable) ((FunctionType) fun.getInnerType()).getParameter()).getUniqueIndex());
        assertEquals(1, ((TypeVariable) ((FunctionType) fun.getInnerType()).getOutput()).getUniqueIndex());
    }

    @Test
    void errors() {
        Map<String, ParseError> tests = new LinkedHashMap<>();
        tests.put("",
                ParseError.unexpectedToken(new Token(Token.TokenType.EOF, "", "type1:", 6),
                        ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                        .expectedInput(ExpectedInput.TYPE)
                        .expectedType(Token.TokenType.UNIVERSAL_QUANTIFIER));
        tests.put("ö", ParseError.unexpectedCharacter2('ö', 6, "type1:ö", ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
        tests.put("(x",
                ParseError.unexpectedToken(new Token(Token.TokenType.EOF, "", "type1:(x", 8),
                        ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                        .expectedTypes(List.of(Token.TokenType.ARROW, Token.TokenType.RIGHT_PARENTHESIS)));
        tests.put("-> x",
                ParseError.unexpectedToken(new Token(Token.TokenType.ARROW, "->", "type1:-> x", 6),
                        ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                        .expectedInput(ExpectedInput.TYPE)
                        .expectedType(Token.TokenType.UNIVERSAL_QUANTIFIER)
        );
        tests.put("x 11",
                ParseError.unexpectedToken(new Token(Token.TokenType.NUMBER, "11", "type1:x 11", 8),
                        ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                        .expectedType(Token.TokenType.ARROW));
        tests.put("x )", ParseError.unexpectedToken(new Token(Token.TokenType.RIGHT_PARENTHESIS, ")", "type1:x )", 8),
                ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                .expectedType(Token.TokenType.COMMA));
        tests.put("x -> (x) )", ParseError.unexpectedToken(new Token(Token.TokenType.RIGHT_PARENTHESIS, ")", "type1:x -> (x) )", 15),
                ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                .expectedType(Token.TokenType.COMMA));
        for (Map.Entry<String, ParseError> entry : tests.entrySet()) {
            TypeAssumptionParser parser = new TypeAssumptionParser();
            Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("type1:" + entry.getKey());
            assertTrue(type.isError());
            assertEquals(entry.getValue(), type.unwrapError());
            if (entry.getValue().getCause().isPresent()) {
                assertEquals(entry.getValue().getCause(), type.unwrapError().getCause());
            }
        }
        TypeAssumptionParser parser = new TypeAssumptionParser();
        Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("föhn: int");
        assertTrue(type.isError());
        assertEquals(ParseError.ErrorCause.UNEXPECTED_CHARACTER, type.unwrapError().getCauseEnum());
        assertEquals(1, type.unwrapError().getPosition());
        parser = new TypeAssumptionParser();
        type = parser.parse("1typ:int");
        assertTrue(type.isError());
        assertEquals(ParseError.ErrorCause.UNEXPECTED_TOKEN, type.unwrapError().getCauseEnum());
        assertEquals(0, type.unwrapError().getPosition());
    }

    @Test
    void variousTypes() {
        Type x = new NamedType("x");
        Type xx = new FunctionType(x, x);
        Type xxx = new FunctionType(x, xx);
        Type xxxx = new FunctionType(x, xxx);
        Type xxxxx = new FunctionType(xx, xxx);
        Type xxxxxx = new FunctionType(xxx, xxx);
        Map<String, TypeAbstraction> tests = new HashMap<>();
        tests.put("x", new TypeAbstraction(x));
        tests.put("( ( x ) )", new TypeAbstraction(x));
        tests.put("x -> x", new TypeAbstraction(xx));
        tests.put("x -> (x)", new TypeAbstraction(xx));
        tests.put("x -> ((x))", new TypeAbstraction(xx));
        tests.put("x -> x -> x", new TypeAbstraction(xxx));
        tests.put("x -> (x -> x)", new TypeAbstraction(xxx));
        tests.put("x -> x -> x -> x", new TypeAbstraction(xxxx));
        tests.put("x -> (x -> x -> x)", new TypeAbstraction(xxxx));
        tests.put("x -> (x -> (x -> x))", new TypeAbstraction(xxxx));
        tests.put("x -> (x -> (x -> (x)))", new TypeAbstraction(xxxx));
        tests.put("(x -> x) -> (x -> (x -> (x)))", new TypeAbstraction(xxxxx));
        tests.put("((x) -> ((x)) -> (x)) -> (x -> (x -> (x)))", new TypeAbstraction(xxxxxx));
        for (Map.Entry<String, TypeAbstraction> entry : tests.entrySet()) {
            TypeAssumptionParser parser = new TypeAssumptionParser();
            Result<Map<VarTerm, TypeAbstraction>, ParseError> type = parser.parse("type1:" + entry.getKey());
            assertTrue(type.isOk());
            assertEquals(entry.getValue(), type.unwrap().get(new VarTerm("type1")));
        }
    }

    @Test
    void errorCase1() {
        ParseError e = parse("id: a ->");
        assertEquals(ExpectedInput.TYPE, e.getExpectedInput().get());
        assertEquals(Token.TokenType.EOF, e.getCause().get().getType());
    }

    @Test
    void errorCase2() {
        ParseError e = parse("id: ");
        assertEquals(ExpectedInput.TYPE, e.getExpectedInput().get());
        assertTrue(e.getExpected().get().contains(Token.TokenType.UNIVERSAL_QUANTIFIER));
        assertEquals(Token.TokenType.EOF, e.getCause().get().getType());
    }

    @Test
    void errorCase3() {
        ParseError e = parse("s");
        assertEquals(ParseError
                .unexpectedToken(new Token(Token.TokenType.EOF, "", "s", 1),
                        ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                .expectedType(Token.TokenType.COLON),
                e);
    }

    @Test
    void errorCase4() {
        ParseError e = parse("s:()");
        assertEquals(ParseError
                        .unexpectedToken(new Token(Token.TokenType.RIGHT_PARENTHESIS, ")", "s:()", 3),
                                ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                        .expectedInput(ExpectedInput.TYPE),
                e);
    }

    static ParseError parse(String input) {
        return new TypeAssumptionParser().parse(input).unwrapError();
    }
}
