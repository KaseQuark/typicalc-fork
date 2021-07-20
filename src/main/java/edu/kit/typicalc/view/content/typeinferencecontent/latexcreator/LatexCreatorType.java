package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVisitor;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;

/**
 * Generates LaTeX code for types.
 *
 * @see LatexCreator
 * @see Type
 */
public class LatexCreatorType implements TypeVisitor {
    private final Type type;
    private static final int MAX_LENGTH = 100000; // 100 KB
    private final LatexCreatorMode mode;

    private final StringBuilder latex = new StringBuilder();
    private boolean needsParentheses = false;

    /**
     * Initialize a new latex creator with a type.
     *
     * @param type the type
     */
    public LatexCreatorType(Type type, LatexCreatorMode mode) {
        this.type = type;
        this.mode = mode;
        type.accept(this);
    }

    /**
     * @return the generated LaTeX code
     */
    public String getLatex() {
        return latex.toString();
    }

    @Override
    public void visit(NamedType named) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        if (mode == LatexCreatorMode.MATHJAX) {
            // this class is used in frontend/src/mathjax-setup.js
            latex.append("\\class{typicalc-type typicalc-type-")
                    .append(named.hashCode())
                    .append("}{");
        }
        latex.append(named.getName());
        if (mode == LatexCreatorMode.MATHJAX) {
            latex.append(CURLY_RIGHT);
        }
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
        if (mode == LatexCreatorMode.MATHJAX) {
            // this class is used in frontend/src/mathjax-setup.js
            latex.append("\\class{typicalc-type typicalc-type-")
                    .append(variable.hashCode())
                    .append("-")
                    .append(variable.getUniqueIndex())
                    .append("}{");
        }
        latex.append(name);
        latex.append(UNDERSCORE);
        latex.append(CURLY_LEFT);
        latex.append(variable.getIndex());
        if (mode == LatexCreatorMode.MATHJAX) {
            latex.append(CURLY_RIGHT);
        }
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
        checkMemoryUsage();
        needsParentheses = true;
    }

    private void checkMemoryUsage() {
        if (latex.length() > MAX_LENGTH) {
            throw new IllegalStateException("type too large!");
        }
    }
}
