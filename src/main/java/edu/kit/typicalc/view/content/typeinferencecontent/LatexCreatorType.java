package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVisitor;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

public class LatexCreatorType implements TypeVisitor {
    private final StringBuilder latex = new StringBuilder();
    private boolean needsParentheses = false;

    protected LatexCreatorType(Type type) {
        type.accept(this);
    }

    protected String getLatex() {
        return latex.toString();
    }

    @Override
    public void visit(NamedType named) {
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        latex.append(named.getName());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(TypeVariable variable) {
        String name;
        switch (variable.getKind()) {
            case TREE:
                name = TREE_VARIABLE;
                break;
            case GENERATED_TYPE_ASSUMPTION:
                name = GENERATED_ASSUMPTION_VARIABLE;
                break;
            case USER_INPUT:
                name = USER_VARIABLE;
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
        latex.append(name);
        latex.append(UNDERSCORE);
        latex.append(CURLY_LEFT);
        latex.append(variable.getIndex());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(FunctionType function) {
        latex.append(PAREN_LEFT);
        function.getParameter().accept(this);
        latex.append(PAREN_RIGHT); // TODO: reduce parentheses
        latex.append(SPACE);
        latex.append(RIGHT_ARROW);
        latex.append(SPACE);
        latex.append(PAREN_LEFT);
        function.getOutput().accept(this);
        latex.append(PAREN_RIGHT); // TODO: reduce parentheses
        needsParentheses = true;
    }
}
