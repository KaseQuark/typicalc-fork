package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for type assumptions.
 */
public class TypeAssumptionParser {

    public static final Pattern TYPE_NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern TYPE_VARIABLE_PATTERN = Pattern.compile("t(\\d+)");

    private static final Set<TokenType> END_TOKENS
            = EnumSet.of(TokenType.ARROW, TokenType.RIGHT_PARENTHESIS, TokenType.EOF);

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

    public Result<TypeAbstraction, ParseError> parseType(String type) {
        LambdaLexer lexer = new LambdaLexer(type);
        Result<Pair<Type, Integer>, ParseError> parsedType = parseType(lexer, 0);
        if (parsedType.isError()) {
            return new Result<>(null, parsedType.unwrapError());
        }
        return new Result<>(new TypeAbstraction(parsedType.unwrap().getLeft()));
    }

    private Result<Pair<Type, Integer>, ParseError> parseType(LambdaLexer lexer, int parenCount) {
        Type type = null;
        int removedParens = 0;
        while (true) {
            Result<Token, ParseError> token = lexer.nextToken();
            if (token.isError()) {
                return new Result<>(token);
            }
            Token t = token.unwrap();
            Result<Type, ParseError> typeResult = null;
            switch (t.getType()) {
                case LEFT_PARENTHESIS:
                    Result<Pair<Type, Integer>, ParseError> type2 = parseType(lexer, 1);
                    typeResult = type2.map(Pair::getLeft);
                    removedParens += type2.map(Pair::getRight).unwrapOr(1) - 1;
                    break;
                case VARIABLE:
                    type = parseLiteral(t.getText());
                    break;
                case RIGHT_PARENTHESIS:
                    removedParens += 1;
                    break;
                case ARROW:
                    if (type == null) {
                        return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
                    }
                    Result<Pair<Type, Integer>, ParseError> nextType = parseType(lexer, parenCount);
                    final Type left = type;
                    typeResult = nextType.map(Pair::getLeft).map(right -> new FunctionType(left, right));
                    removedParens += nextType.map(Pair::getRight).unwrapOr(0);
                    break;
                case EOF:
                    break;
                default:
                    return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
            }
            if (typeResult != null && typeResult.isError()) {
                return new Result<>(typeResult);
            }
            type = typeResult != null ? typeResult.unwrap() : type;
            if (type == null) {
                return new Result<>(null, ParseError.TOO_FEW_TOKENS);
            }
            if (parenCount - removedParens < 0) {
                return new Result<>(null, ParseError.UNEXPECTED_TOKEN.withToken(t));
            } else if (END_TOKENS.contains(t.getType())) {
                if (parenCount - removedParens == 0) {
                    return new Result<>(new ImmutablePair<>(type, removedParens));
                } else {
                    return new Result<>(null, ParseError.TOO_FEW_TOKENS);
                }
            }
        }
    }

    private Type parseLiteral(String text) {
        Matcher typeVariableMatcher = TYPE_VARIABLE_PATTERN.matcher(text);
        if (typeVariableMatcher.matches()) {
            int typeVariableIndex = Integer.parseInt(typeVariableMatcher.group(1));
            return new TypeVariable(TypeVariableKind.USER_INPUT, typeVariableIndex);
        } else {
            return new NamedType(text);
        }
    }
}
