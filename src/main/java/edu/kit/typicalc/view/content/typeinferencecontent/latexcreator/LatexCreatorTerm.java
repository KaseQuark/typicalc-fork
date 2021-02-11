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
    // TODO: document

    private final StringBuilder latex = new StringBuilder();
    private boolean needsParentheses = false;

    /**
     * Initialize a new latex creator object with a lambda term.
     *
     * @param lambdaTerm the term to convert into LaTeX
     */
    protected LatexCreatorTerm(LambdaTerm lambdaTerm) {
        lambdaTerm.accept(this);
    }

    /**
     * @return the generated LaTeX code
     */
    public String getLatex() {
        // remove most outer parentheses if they exist
//        if (latex.indexOf(PAREN_LEFT) == 0 && latex.indexOf(PAREN_RIGHT) == latex.length() - 1) {
//            latex.deleteCharAt(latex.length() - 1);
//            latex.deleteCharAt(0);
//        }
        return latex.toString();
    }

    @Override
    public void visit(AppTerm appTerm) {
        appTerm.getFunction().accept(this);
        latex.append(LATEX_SPACE);
        latex.append(PAREN_LEFT);
        int index = latex.length() - 1;
        appTerm.getParameter().accept(this);
        if (needsParentheses) {
            latex.append(PAREN_RIGHT);
        } else {
            latex.deleteCharAt(index);
        }
        needsParentheses = true;
    }

    @Override
    public void visit(AbsTerm absTerm) {
        latex.append(PAREN_LEFT);
        latex.append(LAMBDA);
        latex.append(SPACE);
        absTerm.getVariable().accept(this);
        latex.append(DOT_SIGN);
        latex.append(LATEX_SPACE);
        absTerm.getInner().accept(this);
        latex.append(PAREN_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(VarTerm varTerm) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(varTerm.getName());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(IntegerTerm intTerm) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(intTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(BooleanTerm boolTerm) {
        latex.append(MONO_TEXT);
        latex.append(CURLY_LEFT);
        latex.append(boolTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
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
        needsParentheses = true;
    }
}
