package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for type assumptions.
 */
public class TypeAssumptionParser {

    public static final Pattern TYPE_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern TYPE_VARIABLE_PATTERN = Pattern.compile("t(\\d+)");

    /**
     * Parse the given type assumptions.
     *
     * @param assumptions the type assumptions
     * @return if successful, a map of the type assumptions, otherwise an error
     */
    public Result<Map<VarTerm, TypeAbstraction>, ParseError> parse(Map<String, String> assumptions) {
        Map<VarTerm, TypeAbstraction> typeAssumptions = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : assumptions.entrySet()) {
            String typeName = entry.getKey();
            if (!TYPE_NAME_PATTERN.matcher(typeName).matches()) {
                return new Result<>(null, ParseError.UNEXPECTED_CHARACTER);
            }
            VarTerm var = new VarTerm(typeName);
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
        return new Result<>(new TypeAbstraction(parsedType.unwrap().getLeft()));
    }

    private Result<Pair<Type, Integer>, ParseError> parseType(LambdaLexer lexer, int parenCount) {
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
                removedParens += type2.unwrap().getRight() - 1;
                if (parenCount - removedParens < 0) {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                }
                break;
            case VARIABLE:
                Matcher typeVariableMatcher = TYPE_VARIABLE_PATTERN.matcher(t.getText());
                if (typeVariableMatcher.matches()) {
                    int typeVariableIndex = Integer.parseInt(typeVariableMatcher.group(1));
                    type = new TypeVariable(TypeVariableKind.USER_INPUT, typeVariableIndex);
                } else {
                    type = new NamedType(t.getText());
                }
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
                removedParens += 1;
                if (parenCount - removedParens < 0) {
                    return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
                } else if (parenCount - removedParens == 0) {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                }
                continue;
            }
            if (t.getType() == TokenType.EOF) {
                if (parenCount - removedParens > 0) {
                    return new Result<>(null, ParseError.TOO_FEW_TOKENS);
                } else {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                }
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
            if (parenCount - removedParens < 0) {
                return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
            } else if (parenCount - removedParens == 0) {
                return new Result<>(new ImmutablePair<>(type, removedParens));
            }
        }
    }
}
