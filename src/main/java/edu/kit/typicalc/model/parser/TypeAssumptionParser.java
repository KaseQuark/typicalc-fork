package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.util.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TypeAssumptionParser {
    public Result<Map<VarTerm, TypeAbstraction>, ParseError> parse(Map<String, String> oldAssumptions) {
        Map<VarTerm, TypeAbstraction> typeAssumptions = new HashMap<>();
        for (Map.Entry<String, String> entry : oldAssumptions.entrySet()) {
            VarTerm var = new VarTerm(entry.getKey());
            Result<TypeAbstraction, ParseError> typeAbs = parseType(entry.getValue());
            if (typeAbs.isError()) {
                return new Result<>(typeAbs);
            }
            typeAssumptions.put(var, typeAbs.unwrap());
        }
        return new Result<>(typeAssumptions);
    }

    private Result<TypeAbstraction, ParseError> parseType(String type) {
        LambdaLexer lexer = new LambdaLexer(type);
        Result<Pair<Type, Integer>, ParseError> parsedType = parseType(lexer, 0);
        if (parsedType.isError()) {
            return new Result<>(null, parsedType.unwrapError());
        }
        return new Result<>(new TypeAbstraction(parsedType.unwrap().getLeft(), Collections.emptyList()));
    }

    private Result<Pair<Type, Integer>, ParseError> parseType(LambdaLexer lexer, int parenCount) {
        // TODO: remove misc. logging
        Result<Token, ParseError> token = lexer.nextToken();
        if (token.isError()) {
            return new Result<>(token);
        }
        Token t = token.unwrap();
        Type type;
        int removedParens = 0;
        switch (t.getType()) {
            case LEFT_PARENTHESIS:
                Result<Pair<Type, Integer>, ParseError> type2 = parseType(lexer, 1);
                if (type2.isError()) {
                    return type2;
                }
                type = type2.unwrap().getLeft();
                parenCount -= type2.unwrap().getRight() - 1;
                removedParens += type2.unwrap().getRight() - 1;
                if (parenCount < 0) {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                }
                break;
            case VARIABLE:
                type = new NamedType(t.getText());
                break;
            default:
                return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
        }
        while (true) {
            token = lexer.nextToken();
            if (token.isError()) {
                return new Result<>(token);
            }
            t = token.unwrap();
            if (t.getType() == TokenType.RIGHT_PARENTHESIS) {
                parenCount -= 1;
                removedParens += 1;
                if (parenCount <= 0) {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                }
                continue;
            }
            if (t.getType() == TokenType.EOF) {
                return new Result<>(new ImmutablePair<>(type, removedParens));
            }
            if (t.getType() != TokenType.ARROW) {
                return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
            }
            Result<Pair<Type, Integer>, ParseError> nextType = parseType(lexer, parenCount);
            if (nextType.isError()) {
                return nextType;
            }
            type = new FunctionType(type, nextType.unwrap().getLeft());
            removedParens += nextType.unwrap().getRight();
            parenCount -= nextType.unwrap().getRight();
            if (parenCount < 0) {
                return new Result<>(new ImmutablePair<>(type, removedParens));
            }
        }
    }
}
