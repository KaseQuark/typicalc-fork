package edu.kit.typicalc.model.parser;

import com.helger.commons.collection.map.MapEntry;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parser for type assumptions.
 * Parses according to the following grammar:
 *
 * TypeEnvironment --> ɛ | TypeAbstraction, TypeEnvironment | TypeAbstraction
 * TypeAbstraction --> Variable | Type
 * Type --> ∀ VarType.SingleType | SingleType
 * SingleType --> SimpleType Rest
 * SimpleType --> Variable | (SingleType)
 * Rest --> ɛ | -> SingleType
 */
public class TypeAssumptionParser {

    private static final Pattern TYPE_VARIABLE_PATTERN = Pattern.compile("t(\\d+)");
    private int typeVariableUniqueIndex = 0;
    private Token currentToken = new Token(Token.TokenType.EOF, "Init", "Init", -1);
    private LambdaLexer lexer;

    private String cleanAssumptionText(String text) {
        return text.replace('₀', '0')
                .replace('₁', '1')
                .replace('₂', '2')
                .replace('₃', '3')
                .replace('₄', '4')
                .replace('₅', '5')
                .replace('₆', '6')
                .replace('₇', '7')
                .replace('₈', '8')
                .replace('₉', '9')
                .replace('τ', 't');
    }

    private Type parseLiteral(String text) {
        Matcher typeVariableMatcher = TYPE_VARIABLE_PATTERN.matcher(text);
        if (typeVariableMatcher.matches()) {
            int typeVariableIndex = Integer.parseInt(typeVariableMatcher.group(1));
            TypeVariable variable = new TypeVariable(TypeVariableKind.USER_INPUT, typeVariableIndex);
            variable.setUniqueIndex(typeVariableUniqueIndex);
            return variable;
        } else {
            return new NamedType(text);
        }
    }

    public Result<Map<VarTerm, TypeAbstraction>, ParseError> parse(String assumptions) {
        lexer = new LambdaLexer(
                cleanAssumptionText(assumptions), ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR);
        return parseTE();
    }

    /**
     * parses Type Environment
     * @return if successful, a map of the type assumptions, otherwise an error
     */
    public Result<Map<VarTerm, TypeAbstraction>, ParseError> parseTE() {
        HashMap<VarTerm, TypeAbstraction> map = new HashMap<>();
        while (true) {
            Result<Token, ParseError> nextLexerToken = lexer.nextToken();
            if (nextLexerToken.isError()) {
                return nextLexerToken.castError();
            }
            currentToken = nextLexerToken.unwrap();

            if (currentToken.getType() == Token.TokenType.EOF) {
                return new Result<>(map, null);
            }
            Result<MapEntry<VarTerm, TypeAbstraction>, ParseError> result = parseTA();
            if (result.isError()) {
                return result.castError();
            }
            map.put(result.unwrap().getKey(), result.unwrap().getValue());

            if (currentToken.getType() == Token.TokenType.EOF) {
                return new Result<>(map, null);
            }
            typeVariableUniqueIndex++;
            if (currentToken.getType() != Token.TokenType.COMMA) {
                return new Result<>(null, ParseError.unexpectedToken(currentToken,
                        ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                        .expectedTypes(List.of(Token.TokenType.COMMA, Token.TokenType.EOF)));
            }
        }
    }

    /**
     * Parses single Type Assumption
     * @return if successful, a type assumption, otherwise an error
     */
    public Result<MapEntry<VarTerm, TypeAbstraction>, ParseError> parseTA() {
        VarTerm term;
        if (currentToken.getType() == Token.TokenType.VARIABLE) {
            term = new VarTerm(currentToken.getText());
        } else {
            return new Result<>(null, ParseError.unexpectedToken(currentToken,
                    ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR).expectedType(Token.TokenType.VARIABLE));
        }

        Result<Token, ParseError> nextLexerToken = lexer.nextToken();
        if (nextLexerToken.isError()) {
            return nextLexerToken.castError();
        }
        currentToken = nextLexerToken.unwrap();

        if (currentToken.getType() != Token.TokenType.COLON) {
            return new Result<>(null, ParseError.unexpectedToken(currentToken,
                    ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR).expectedType(Token.TokenType.COLON));
        }

        Result<TypeAbstraction, ParseError> result = parseType();
        if (result.isError()) {
            return result.castError();
        }
        return new Result<>(new MapEntry<>(term, result.unwrap()));
    }


    /**
     * Parses a Type
     * @return if successful a type abstraction, otherwise an error.
     */
    public Result<TypeAbstraction, ParseError> parseType() {
        Result<Token, ParseError> nextLexerToken = lexer.nextToken();
        if (nextLexerToken.isError()) {
            return nextLexerToken.castError();
        }
        currentToken = nextLexerToken.unwrap();

        TreeSet<TypeVariable> quantifiedVariables = new TreeSet<>();
        if (currentToken.getType() == Token.TokenType.UNIVERSAL_QUANTIFIER) {
            while (true) {
                nextLexerToken = lexer.nextToken();
                if (nextLexerToken.isError()) {
                    return nextLexerToken.castError();
                }
                currentToken = nextLexerToken.unwrap();

                if (currentToken.getType() != Token.TokenType.VARIABLE) {
                    return new Result<>(null,
                            ParseError.unexpectedToken(currentToken, ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                                    .expectedType(Token.TokenType.VARIABLE));
                }
                String input = currentToken.getText();
                if (!TYPE_VARIABLE_PATTERN.matcher(input).matches()) {
                    return new Result<>(null,
                            ParseError.unexpectedToken(currentToken, ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                                    .expectedInput(ExpectedInput.VARTYPE));
                }
                int i = Integer.parseInt(input.substring(1));
                TypeVariable v = new TypeVariable(TypeVariableKind.USER_INPUT, i);
                v.setUniqueIndex(typeVariableUniqueIndex);

                for (TypeVariable variable : quantifiedVariables) {
                    if (variable.equals(v)) {
                        return new Result<>(null,
                                ParseError.unexpectedToken(currentToken, ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                                        .additionalInformation(AdditionalInformation.DUPLICATETYPE));
                    }
                }
                quantifiedVariables.add(v);

                nextLexerToken = lexer.nextToken();
                if (nextLexerToken.isError()) {
                    return nextLexerToken.castError();
                }
                currentToken = nextLexerToken.unwrap();

                if (currentToken.getType() != Token.TokenType.COMMA) {
                    if (currentToken.getType() != Token.TokenType.DOT) {
                        return new Result<>(null,
                                ParseError.unexpectedToken(currentToken, ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                                        .expectedTypes(List.of(Token.TokenType.COMMA, Token.TokenType.DOT)));
                    }
                    nextLexerToken = lexer.nextToken();
                    if (nextLexerToken.isError()) {
                        return nextLexerToken.castError();
                    }
                    currentToken = nextLexerToken.unwrap();
                    break;
                }
            }
        }

        if (currentToken.getType() != Token.TokenType.VARIABLE && currentToken.getType()
                != Token.TokenType.LEFT_PARENTHESIS) {
            return new Result<>(null,
                    ParseError.unexpectedToken(currentToken, ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                            .expectedInput(ExpectedInput.TYPE)
                            .expectedType(Token.TokenType.UNIVERSAL_QUANTIFIER));
        }

        Result<Type, ParseError> result = parseSingleType();
        if (result.isError()) {
            return result.castError();
        }
        return new Result<>(new TypeAbstraction(result.unwrap(), quantifiedVariables));
    }

    /**
     * parses a single Type
     * @return if successful a type, otherwise an error
     */
    public Result<Type, ParseError> parseSingleType() {

        Result<Type, ParseError> result = parseSimpleType();
        if (result.isError()) {
            return result.castError();
        }
        Type type1 = result.unwrap();

        Result<Token, ParseError> nextLexerToken = lexer.nextToken();
        if (nextLexerToken.isError()) {
            return nextLexerToken.castError();
        }
        currentToken = nextLexerToken.unwrap();

        Result<Optional<Type>, ParseError> result2 = parseRest();
        if (result2.isError()) {
            return result2.castError();
        }
        Optional<Type> type2 = result2.unwrap();
        if (type2.isEmpty()) {
            return new Result<>(type1);
        } else {
            return new Result<>(new FunctionType(type1, type2.get()));
        }
    }

    /**
     * parses a simple type
     * @return if successful a type, otherwise an error
     */
    public Result<Type, ParseError> parseSimpleType() {
        if (currentToken.getType() == Token.TokenType.VARIABLE) {
            Type type = parseLiteral(currentToken.getText());
            return new Result<>(type);
        } else if (currentToken.getType() == Token.TokenType.LEFT_PARENTHESIS) {
            Result<Token, ParseError> nextLexerToken = lexer.nextToken();
            if (nextLexerToken.isError()) {
                return nextLexerToken.castError();
            }
            currentToken = nextLexerToken.unwrap();

            Result<Type, ParseError> result = parseSingleType();
            if (result.isError()) {
                return result.castError();
            }
            Type type = result.unwrap();

            if (currentToken.getType() != Token.TokenType.RIGHT_PARENTHESIS) {
                return new Result<>(null, ParseError.unexpectedToken(currentToken,
                        ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR)
                        .expectedTypes(List.of(Token.TokenType.ARROW, Token.TokenType.RIGHT_PARENTHESIS)));
            }
            return new Result<>(type);
        }
        return new Result<>(null, ParseError.unexpectedToken(currentToken,
                ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR).expectedInput(ExpectedInput.TYPE));
    }

    /**
     * parses the rest of a single type
     * @return if successful a type or an empty optional, otherwise an error
     */
    public Result<Optional<Type>, ParseError> parseRest() {
        switch (currentToken.getType()) {
            case ARROW:
                Result<Token, ParseError> nextLexerToken = lexer.nextToken();
                if (nextLexerToken.isError()) {
                    return nextLexerToken.castError();
                }
                currentToken = nextLexerToken.unwrap();

                Result<Type, ParseError> result = parseSingleType();
                if (result.isError()) {
                    return result.castError();
                }
                return new Result<>(Optional.of(result.unwrap()), null);
            case VARIABLE:
            case NUMBER:
                return new Result<>(null, ParseError.unexpectedToken(currentToken,
                        ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR).expectedType(Token.TokenType.ARROW));
                default:
            return new Result<>(Optional.empty(), null);
        }
    }
}
