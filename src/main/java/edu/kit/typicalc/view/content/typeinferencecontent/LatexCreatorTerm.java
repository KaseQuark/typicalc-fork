package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.ConstTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.TermVisitor;
import edu.kit.typicalc.model.term.VarTerm;

public class LatexCreatorTerm implements TermVisitor {
    // TODO: document
    private static final String PAREN_LEFT = "(";
    private static final String PAREN_RIGHT = ")";
    private static final String LATEX_SPACE = "\\ ";
    private static final String LAMBDA = "\\lambda";
    private static final String CURLY_LEFT = "{";
    private static final String CURLY_RIGHT = "}";
    private static final String TEXTTT = "\\texttt";

    private final StringBuilder latex;
    private boolean needsParentheses = false;

    protected LatexCreatorTerm(LambdaTerm lambdaTerm) {
        this.latex = new StringBuilder();
        lambdaTerm.accept(this);
    }

    public String getLatex() {
        return latex.toString();
    }

    @Override
    public void visit(AppTerm appTerm) {
        appTerm.getFunction().accept(this);
        latex.append(LATEX_SPACE);
        appTerm.getParameter().accept(this);
        if (needsParentheses) {
            latex.insert(0, PAREN_LEFT);
            latex.append(PAREN_RIGHT);
        }
        needsParentheses = true;
    }

    @Override
    public void visit(AbsTerm absTerm) {
        latex.append(LAMBDA);
        latex.append(' ');
        absTerm.getVariable().accept(this);
        latex.append('.');
        latex.append(LATEX_SPACE);
        absTerm.getInner().accept(this);
        needsParentheses = false;
    }

    @Override
    public void visit(VarTerm varTerm) {
        needsParentheses = false;
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        latex.append(varTerm.getName());
        latex.append(CURLY_RIGHT);
    }

    @Override
    public void visit(ConstTerm constTerm) {
        // todo implement correctly (with extended termVisitor)
        latex.append(TEXTTT);
        latex.append(CURLY_LEFT);
        constTerm.accept(this);
        latex.append(CURLY_RIGHT);
        needsParentheses = false;
    }

    @Override
    public void visit(LetTerm letTerm) {
        // todo implement
    }
}
