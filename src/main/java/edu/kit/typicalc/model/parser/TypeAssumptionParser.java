package edu.kit.typicalc.model.parser;

import edu.kit.typicalc.model.parser.Token.TokenType;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;
import edu.kit.typicalc.util.Result;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for type assumptions.
 */
public class TypeAssumptionParser {

    private static final Pattern TYPE_VARIABLE_PATTERN = Pattern.compile("t(\\d+)");

    /**
     * Parse the given type assumptions.
     *
     * @param assumptions the type assumptions
     * @return if successful, a map of the type assumptions, otherwise an error
     */
    public Result<Map<VarTerm, TypeAbstraction>, ParseError> parse(String assumptions) {
        ParserState<Map<VarTerm, TypeAbstraction>> state = new InitialState(new LinkedHashMap<>());
        LambdaLexer lexer = new LambdaLexer(cleanAssumptionText(assumptions));
        Optional<Token> extraToken = Optional.empty();
        while (true) {
            Token token1;
            if (extraToken.isPresent()) {
                token1 = extraToken.get();
            } else {
                Result<Token, ParseError> token = lexer.nextToken();
                if (token.isError()) {
                    return token.castError();
                }
                token1 = token.unwrap();
            }
            ParserResult<Map<VarTerm, TypeAbstraction>> result = state.handle(token1);
            if (result.isResult()) {
                return new Result<>(result.getResult());
            } else if (result.isError()) {
                return new Result<>(null, result.getError());
            } else {
                state = result.getState();
                extraToken = result.extraToken();
            }
        }
    }

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

    private static class ParserResult<T> {
        private Optional<ParserState<T>> newState = Optional.empty();
        private Optional<ParseError> error = Optional.empty();
        private Optional<T> result = Optional.empty();
        private Optional<Token> extraToken = Optional.empty();

        ParserResult(ParseError e) {
            this.error = Optional.of(e);
        }

        ParserResult(ParserState<T> state) {
            this.newState = Optional.of(state);
        }

        ParserResult(T result) {
            this.result = Optional.of(result);
        }

        boolean isTransition() {
            return newState.isPresent();
        }

        ParserState<T> getState() {
            return newState.get();
        }

        boolean isError() {
            return error.isPresent();
        }

        ParseError getError() {
            return error.get();
        }

        <U> ParserResult<U> castError() {
            return new ParserResult<>(error.get());
        }

        boolean isResult() {
            return result.isPresent();
        }

        T getResult() {
            return result.get();
        }

        ParserResult<T> attachToken(Token t) {
            this.extraToken = Optional.of(t);
            return this;
        }

        ParserResult<T> copyToken(ParserResult<?> other) {
            this.extraToken = other.extraToken;
            return this;
        }

        Optional<Token> extraToken() {
            return this.extraToken;
        }
    }

    private interface ParserState<T> {
        ParserResult<T> handle(Token t);
    }

    private static class InitialState implements ParserState<Map<VarTerm, TypeAbstraction>> {
        private Map<VarTerm, TypeAbstraction> alreadyParsed = new LinkedHashMap<>();

        InitialState(Map<VarTerm, TypeAbstraction> alreadyParsed) {
            this.alreadyParsed = alreadyParsed;
        }

        @Override
        public ParserResult<Map<VarTerm, TypeAbstraction>> handle(Token t) {
            switch (t.getType()) {
                case VARIABLE:
                    return new ParserResult<>(new ExpectingColon(alreadyParsed, new VarTerm(t.getText())));
                case EOF:
                    return new ParserResult<>(alreadyParsed);
                default:
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
            }
        }
    }

    private static class ExpectingColon implements ParserState<Map<VarTerm, TypeAbstraction>> {
        private Map<VarTerm, TypeAbstraction> alreadyParsed;
        private final VarTerm var;
        ExpectingColon(Map<VarTerm, TypeAbstraction> alreadyParsed, VarTerm var) {
            this.alreadyParsed = alreadyParsed;
            this.var = var;
        }

        @Override
        public ParserResult<Map<VarTerm, TypeAbstraction>> handle(Token t) {
            switch (t.getType()) {
                case COLON:
                    return new ParserResult<>(new ExpectingTypeDef(alreadyParsed, var));
                default:
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
            }
        }
    }

    private static class ExpectingTypeDef implements ParserState<Map<VarTerm, TypeAbstraction>> {
        private final Map<VarTerm, TypeAbstraction> alreadyParsed;
        private final Set<TypeVariable> typeVariables;
        private final VarTerm var;
        private Optional<ParserState<Type>> state = Optional.empty();
        ExpectingTypeDef(Map<VarTerm, TypeAbstraction> alreadyParsed, VarTerm var) {
            this.alreadyParsed = alreadyParsed;
            this.typeVariables = new TreeSet<>();
            this.var = var;
        }

        ExpectingTypeDef(Map<VarTerm, TypeAbstraction> alreadyParsed, Set<TypeVariable> typeVariables, VarTerm var) {
            this.alreadyParsed = alreadyParsed;
            this.typeVariables = typeVariables;
            this.var = var;
        }

        @Override
        public ParserResult<Map<VarTerm, TypeAbstraction>> handle(Token t) {
            switch (t.getType()) {
                case UNIVERSAL_QUANTIFIER:
                    if (typeVariables.isEmpty()) {
                        return new ParserResult<>(new ExpectingTypeVariables(alreadyParsed, var));
                    } else {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
                default:
                    if (state.isPresent()) {
                        ParserState<Type> status = state.get();
                        // already parsing type
                        ParserResult<Type> result = status.handle(t);
                        if (result.isResult()) {
                            alreadyParsed.put(var, new TypeAbstraction(result.getResult(), typeVariables));
                            return new ParserResult<>(new ExpectingCommaBeforeNextType(alreadyParsed))
                                    .copyToken(result);
                        } else if (result.isError()) {
                            return result.castError();
                        } else {
                            state = Optional.of(result.getState());
                            return new ParserResult<>(this);
                        }
                    }
                    // attempt to parse as type
                    ParserResult<Type> result = new ParseTypeState1(alreadyParsed.size()).handle(t);
                    if (result.isError()) {
                        return result.castError();
                    }
                    state = Optional.of(result.getState());
                    return new ParserResult<>(this);
            }
        }
    }

    private static class ExpectingCommaBeforeNextType implements ParserState<Map<VarTerm, TypeAbstraction>> {
        private final Map<VarTerm, TypeAbstraction> alreadyParsed;

        ExpectingCommaBeforeNextType(Map<VarTerm, TypeAbstraction> alreadyParsed) {
            this.alreadyParsed = alreadyParsed;
        }

        @Override
        public ParserResult<Map<VarTerm, TypeAbstraction>> handle(Token t) {
            switch (t.getType()) {
                case EOF:
                    return new ParserResult<>(alreadyParsed);
                case COMMA:
                    return new ParserResult<>(new InitialState(alreadyParsed));
                default:
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
            }
        }
    }

    /**
     * Main type parsing state.
     */
    private static class ParseTypeState1 implements ParserState<Type> {
        private Optional<Type> parsedType = Optional.empty();
        private Optional<ParserState<Type>> state = Optional.empty();
        private Optional<ParserState<Type>> stateParenthesis = Optional.empty();
        private int parenthesisInitial = 0;
        private int openParens = 0;

        /**
         * Attached to any parsed type variables.
         */
        private final int typeVariableUniqueIndex;

        ParseTypeState1(int typeVariableUniqueIndex) {
            this.typeVariableUniqueIndex = typeVariableUniqueIndex;
        }

        @Override
        public ParserResult<Type> handle(Token t) {
            switch (t.getType()) {
                case VARIABLE:
                    if (state.isPresent()) {
                        return handleInner(t);
                    }
                    if (stateParenthesis.isPresent()) {
                        return handleInnerParenthesis(t);
                    }
                    if (parsedType.isPresent()) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
                    Type type = parseLiteral(t.getText());
                    // try parsing function type (see below)
                    this.parsedType = Optional.of(type);
                    return new ParserResult<>(this);
                case ARROW:
                    if (state.isPresent()) {
                        return handleInner(t);
                    }
                    if (stateParenthesis.isPresent()) {
                        return handleInnerParenthesis(t);
                    }
                    if (parsedType.isEmpty()) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
                    // parse function type
                    state = Optional.of(new ParseTypeStateExpectArrow(typeVariableUniqueIndex).handle(t).getState());
                    return new ParserResult<>(this);
                case LEFT_PARENTHESIS:
                    openParens += 1;
                    if (state.isPresent()) {
                        return handleInner(t);
                    }
                    if (stateParenthesis.isPresent()) {
                        return handleInnerParenthesis(t);
                    }
                    stateParenthesis = Optional.of(new ParseTypeState1(typeVariableUniqueIndex));
                    parenthesisInitial = openParens - 1;
                    return new ParserResult<>(this);
                case RIGHT_PARENTHESIS:
                    openParens -= 1;
                    if (state.isPresent()) {
                        return handleInner(t);
                    }
                    if (stateParenthesis.isPresent()) {
                        if (openParens == parenthesisInitial) {
                            // inner state is done parsing
                            ParserResult<Type> result = handleInnerParenthesis(
                                    new Token(TokenType.EOF, "", "", -1));
                            if (result.isError()) {
                                return result.castError();
                            } else {
                                parsedType = Optional.of(result.getResult());
                                stateParenthesis = Optional.empty();
                            }
                        } else {
                            return handleInnerParenthesis(t);
                        }
                    }
                    if (parsedType.isPresent()) {
                        return new ParserResult<>(this); // parenthesized part may be start of function
                    }
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                case COMMA:
                case EOF:
                    if (state.isPresent()) {
                        return handleInner(t).attachToken(t);
                    }
                    if (stateParenthesis.isPresent() && openParens == parenthesisInitial) {
                        return handleInnerParenthesis(t).attachToken(t);
                    }
                    if (parsedType.isPresent() && openParens == parenthesisInitial) {
                        return new ParserResult<>(parsedType.get()).attachToken(t);
                    }
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                default:
                    if (state.isPresent()) {
                        return handleInner(t);
                    }
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
            }
        }

        private ParserResult<Type> handleInner(Token t) {
            ParserState<Type> status = state.get();
            // already parsing right type of function
            ParserResult<Type> result = status.handle(t);
            if (result.isResult()) {
                return new ParserResult<Type>(new FunctionType(parsedType.get(), result.getResult())).copyToken(result);
            } else if (result.isError()) {
                return result.castError();
            } else {
                state = Optional.of(result.getState());
                return new ParserResult<>(this);
            }
        }

        private ParserResult<Type> handleInnerParenthesis(Token t) {
            ParserState<Type> status = stateParenthesis.get();
            // already parsing right type of function
            ParserResult<Type> result = status.handle(t);
            if (result.isResult()) {
                return new ParserResult<>(result.getResult()).copyToken(result);
            } else if (result.isError()) {
                return result.castError();
            } else {
                stateParenthesis = Optional.of(result.getState());
                return new ParserResult<>(this);
            }
        }

        private Type parseLiteral(String text) {
            Matcher typeVariableMatcher = TYPE_VARIABLE_PATTERN.matcher(text);
            if (typeVariableMatcher.matches()) {
                int typeVariableIndex = Integer.parseInt(typeVariableMatcher.group(1));
                TypeVariable var = new TypeVariable(TypeVariableKind.USER_INPUT, typeVariableIndex);
                var.setUniqueIndex(typeVariableUniqueIndex);
                return var;
            } else {
                return new NamedType(text);
            }
        }
    }

    private static class ParseTypeStateExpectArrow implements ParserState<Type> {
        private Optional<ParserState<Type>> state = Optional.empty();

        /**
         * Attached to any parsed type variables.
         */
        private final int typeVariableUniqueIndex;

        ParseTypeStateExpectArrow(int typeVariableUniqueIndex) {
            this.typeVariableUniqueIndex = typeVariableUniqueIndex;
        }

        @Override
        public ParserResult<Type> handle(Token t) {
            switch (t.getType()) {
                case ARROW:
                    if (state.isEmpty()) {
                        // try parsing remainder as type
                        state = Optional.of(new ParseTypeState1(typeVariableUniqueIndex));
                        return new ParserResult<>(this);
                    }
                default:
                    if (state.isPresent()) {
                        ParserState<Type> status = state.get();
                        // already parsing type
                        ParserResult<Type> result = status.handle(t);
                        if (result.isResult()) {
                            return result;
                        } else if (result.isError()) {
                            return result.castError();
                        } else {
                            state = Optional.of(result.getState());
                            return new ParserResult<>(this);
                        }
                    } else {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
            }
        }
    }

    private static class ExpectingTypeVariables implements ParserState<Map<VarTerm, TypeAbstraction>> {
        private final Map<VarTerm, TypeAbstraction> alreadyParsed;
        private final VarTerm var;
        private final Set<TypeVariable> variables = new TreeSet<>();
        private boolean expectCommaOrDot = false;
        ExpectingTypeVariables(Map<VarTerm, TypeAbstraction> alreadyParsed, VarTerm var) {
            this.alreadyParsed = alreadyParsed;
            this.var = var;
        }

        @Override
        public ParserResult<Map<VarTerm, TypeAbstraction>> handle(Token t) {
            switch (t.getType()) {
                case VARIABLE:
                    if (expectCommaOrDot) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                                .expectedTypes(List.of(TokenType.COMMA, Token.TokenType.DOT)));
                    }
                    String input = t.getText();
                    if (!TYPE_VARIABLE_PATTERN.matcher(input).matches()) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
                    int i = Integer.parseInt(input.substring(1));
                    TypeVariable variable = new TypeVariable(TypeVariableKind.USER_INPUT, i);
                    if (variables.contains(variable)) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
                    }
                    variable.setUniqueIndex(alreadyParsed.size());
                    variables.add(variable);
                    expectCommaOrDot = true;
                    return new ParserResult<>(this);
                case COMMA:
                    if (expectCommaOrDot) {
                        expectCommaOrDot = false;
                        return new ParserResult<>(this);
                    } else {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                                .expectedType(TokenType.VARIABLE));
                    }
                case DOT:
                    if (expectCommaOrDot) {
                        // list of type variables is complete
                        // parse actual type
                        return new ParserResult<>(new ExpectingTypeDef(alreadyParsed, variables, var));
                    } else {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                                .expectedType(TokenType.VARIABLE));
                    }
                default:
                    if (expectCommaOrDot) {
                        return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                                .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR)
                                .expectedTypes(List.of(TokenType.COMMA, TokenType.DOT)));
                    }
                    return new ParserResult<>(ParseError.UNEXPECTED_TOKEN
                            .withToken(t, ParseError.ErrorType.TYPE_ASSUMPTION_ERROR));
            }
        }
    }
}
