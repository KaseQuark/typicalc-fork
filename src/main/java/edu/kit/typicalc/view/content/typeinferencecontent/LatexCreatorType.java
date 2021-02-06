package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVisitor;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

/**
 * Generates LaTeX code for types.
 *
 * @see LatexCreator
 * @see Type
 */
public class LatexCreatorType implements TypeVisitor {
    private final StringBuilder latex = new StringBuilder();
    private boolean needsParentheses = false;

    /**
     * Initialize a new latex creator with a type.
     *
     * @param type the type
     */
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
        int index = latex.length() - 1;
        function.getParameter().accept(this);
        if (needsParentheses) {
            latex.append(PAREN_RIGHT);
        } else {
            latex.deleteCharAt(index);
        }

        latex.append(SPACE);
        latex.append(RIGHT_ARROW);
        latex.append(SPACE);

        function.getOutput().accept(this);
        needsParentheses = true;
    }
}
