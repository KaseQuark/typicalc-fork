package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.term.*;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

public class LatexCreatorTerm implements TermVisitor {
    // TODO: document

    private final StringBuilder latex;
    private boolean needsParentheses = false;

    protected LatexCreatorTerm(LambdaTerm lambdaTerm) {
        this.latex = new StringBuilder();
        lambdaTerm.accept(this);
    }

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
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        latex.append(varTerm.getName());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(IntegerTerm intTerm) {
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        latex.append(intTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(BooleanTerm boolTerm) {
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        latex.append(boolTerm.getValue());
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(LetTerm letTerm) {
        latex.append(TEXTTT)
                .append(CURLY_LEFT)
                .append(TEXTBF)
                .append(CURLY_LEFT)
                .append(LET)
                .append(CURLY_RIGHT)
                .append(CURLY_RIGHT)
                .append(LATEX_SPACE);
        letTerm.getVariable().accept(this);
        latex.append(EQUALS);
        letTerm.getVariableDefinition().accept(this);
        latex.append(LATEX_SPACE)
                .append(TEXTTT)
                .append(CURLY_LEFT)
                .append(TEXTBF)
                .append(CURLY_LEFT)
                .append(IN)
                .append(CURLY_RIGHT)
                .append(CURLY_RIGHT)
                .append(LATEX_SPACE);
        letTerm.getInner().accept(this);
        needsParentheses = true;
    }
}
