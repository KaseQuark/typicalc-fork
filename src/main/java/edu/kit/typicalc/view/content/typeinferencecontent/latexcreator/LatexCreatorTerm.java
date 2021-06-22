package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.term.*;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;

/**
 * Generates LaTeX code for lambda terms.
 *
 * @see LatexCreator
 * @see LambdaTerm
 */
public class LatexCreatorTerm implements TermVisitor {
    private final StringBuilder latex = new StringBuilder();
    private final LatexCreatorMode mode;

    private enum ParenthesesNeeded {
        NEVER,
        SOMETIMES,
        ALWAYS
    }

    private ParenthesesNeeded needsParentheses;

    /**
     * Initialize a new latex creator object with a lambda term.
     *
     * @param lambdaTerm the term to convert into LaTeX
     */
    protected LatexCreatorTerm(LambdaTerm lambdaTerm, LatexCreatorMode mode) {
        this.mode = mode;
        lambdaTerm.accept(this);
    }

    /**
     * Returns the generated LaTeX code
     *
     * @return the generated LaTeX code
     */
    public String getLatex() {
        return latex.toString();
    }

    @Override
    public void visit(AppTerm appTerm) {
        int index;
        latex.append(PAREN_LEFT);
        index = latex.length() - 1;
        appTerm.getFunction().accept(this);
        if (needsParentheses == ParenthesesNeeded.ALWAYS) {
            latex.append(PAREN_RIGHT);
        } else {
            latex.deleteCharAt(index);
        }

        latex.append(LATEX_SPACE);

        latex.append(PAREN_LEFT);
        index = latex.length() - 1;
        appTerm.getParameter().accept(this);
        if (needsParentheses == ParenthesesNeeded.SOMETIMES || needsParentheses == ParenthesesNeeded.ALWAYS) {
            latex.append(PAREN_RIGHT);
        } else {
            latex.deleteCharAt(index);
        }
        needsParentheses = ParenthesesNeeded.SOMETIMES;
    }

    @Override
    public void visit(AbsTerm absTerm) {
        latex.append(LAMBDA);
        latex.append(SPACE);
        absTerm.getVariable().accept(this);
        latex.append(DOT_SIGN);
        latex.append(LATEX_SPACE);
        absTerm.getInner().accept(this);
        needsParentheses = ParenthesesNeeded.ALWAYS;
    }

    @Override
    public void visit(VarTerm varTerm) {
        if (mode == LatexCreatorMode.MATHJAX) {
            latex.append("\\class{typicalc-type typicalc-type-v-");
            latex.append(varTerm.hashCode());
            latex.append("}{");
        }
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(varTerm.getName());
        latex.append(CURLY_RIGHT);
        if (mode == LatexCreatorMode.MATHJAX) {
            latex.append("}");
        }
        needsParentheses = ParenthesesNeeded.NEVER;
    }

    @Override
    public void visit(IntegerTerm intTerm) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(intTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = ParenthesesNeeded.NEVER;
    }

    @Override
    public void visit(BooleanTerm boolTerm) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(boolTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = ParenthesesNeeded.NEVER;
    }

    @Override
    public void visit(LetTerm letTerm) {
        latex.append(MONO_TEXT)
                .append(CURLY_LEFT)
                .append(BOLD_TEXT)
                .append(CURLY_LEFT)
                .append(LET)
                .append(CURLY_RIGHT)
                .append(CURLY_RIGHT)
                .append(LATEX_SPACE);
        letTerm.getVariable().accept(this);
        latex.append(EQUALS);
        letTerm.getVariableDefinition().accept(this);
        latex.append(LATEX_SPACE)
                .append(MONO_TEXT)
                .append(CURLY_LEFT)
                .append(BOLD_TEXT)
                .append(CURLY_LEFT)
                .append(IN)
                .append(CURLY_RIGHT)
                .append(CURLY_RIGHT)
                .append(LATEX_SPACE);
        letTerm.getInner().accept(this);
        needsParentheses = ParenthesesNeeded.ALWAYS;
    }
}
