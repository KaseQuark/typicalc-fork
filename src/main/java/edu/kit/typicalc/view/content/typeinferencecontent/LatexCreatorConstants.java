package edu.kit.typicalc.view.content.typeinferencecontent;

public final class LatexCreatorConstants {
    private LatexCreatorConstants() {
    }

    protected static final String CONST = "Const";
    protected static final String LET = "let";
    protected static final String IN = "in";

    protected static final String NEW_LINE = System.lineSeparator();
    protected static final String SPACE = " ";
    protected static final String DOLLAR_SIGN = "$";
    protected static final String EQUALS = "=";
    protected static final String COMMA = ",";
    protected static final String DOT_SIGN = ".";
    protected static final String COLON = ":";
    protected static final String UNDERSCORE = "_";
    protected static final String CURLY_LEFT = "{";
    protected static final String CURLY_RIGHT = "}";
    protected static final String PAREN_LEFT = "(";
    protected static final String PAREN_RIGHT = ")";
    protected static final String BRACKET_LEFT = "[";
    protected static final String BRACKET_RIGHT = "]";

    protected static final String LABEL_ABS = "\\LeftLabel{\\rm A{\\small BS}}";
    protected static final String LABEL_APP = "\\LeftLabel{\\rm A{\\small PP}}";
    protected static final String LABEL_CONST = "\\LeftLabel{\\rm C{\\small ONST}}";
    protected static final String LABEL_VAR = "\\LeftLabel{\\rm V{\\small AR}}";
    protected static final String LABEL_LET = "\\LeftLabel{\\rm L{\\small ET}}";

    protected static final String UIC = "\\UnaryInfC";
    protected static final String BIC = "\\BinaryInfC";
    protected static final String AXC = "\\AxiomC";

    protected static final String TREE_VARIABLE = "\\alpha";
    protected static final String GENERATED_ASSUMPTION_VARIABLE = "\\beta";
    protected static final String USER_VARIABLE = "\\tau";
    protected static final String GAMMA = "\\Gamma";

    protected static final String SUBSTITUTION_SIGN = "\\mathrel{\\unicode{x21E8}}";
    protected static final String LAMBDA = "\\lambda";
    protected static final String LATEX_SPACE = "\\ ";
    protected static final String FOR_ALL = "\\forall";
    protected static final String TEXTTT = "\\texttt";
    protected static final String TEXTBF = "\\textbf";
    protected static final String RIGHT_ARROW = "\\rightarrow";
    protected static final String INSTANTIATE_SIGN = "\\succeq";
    protected static final String LATEX_IN = "\\in";
    protected static final String VDASH = "\\vdash";
    protected static final String TREE_BEGIN = "\\begin{prooftree}";
    protected static final String TREE_END = "\\end{prooftree}";

    protected static final String BUSSPROOFS = "\\usepackage{bussproofs}";
}
