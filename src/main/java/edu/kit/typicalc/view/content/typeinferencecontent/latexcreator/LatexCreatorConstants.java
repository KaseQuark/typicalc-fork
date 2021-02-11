package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

public final class LatexCreatorConstants {
    private LatexCreatorConstants() {
    }

    protected static final String CONST = "Const";
    protected static final String LET = "let";
    protected static final String IN = "in";
    protected static final String MGU = "mgu";
    protected static final String UNIFY = "unify";
    protected static final String CONSTRAINT_SET = "C";

    protected static final String DOLLAR_SIGN = "$";
    protected static final char NEW_LINE = '\n';
    protected static final char SPACE = ' ';
    protected static final char AMPERSAND = '&';
    protected static final char EQUALS = '=';
    protected static final char COMMA = ',';
    protected static final char DOT_SIGN = '.';
    protected static final char COLON = ':';
    protected static final char UNDERSCORE = '_';
    protected static final char CURLY_LEFT = '{';
    protected static final char CURLY_RIGHT = '}';
    protected static final char PAREN_LEFT = '(';
    protected static final char PAREN_RIGHT = ')';
    protected static final char BRACKET_LEFT = '[';
    protected static final char BRACKET_RIGHT = ']';

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
    protected static final String SIGMA = "\\sigma";

    protected static final String LATEX_NEW_LINE = "\\\\";
    protected static final String CIRC = "\\circ";
    protected static final String PHANTOM_X = "\\phantom{x}";
    protected static final String SUBSTITUTION_SIGN = "\\mathrel{\\unicode{x21E8}}";
    protected static final String COLOR_RED = "\\color{#f00}";
    protected static final String LAMBDA = "\\lambda";
    protected static final String LATEX_SPACE = "\\ ";
    protected static final String LATEX_CURLY_LEFT = "\\{";
    protected static final String LATEX_CURLY_RIGHT = "\\}";
    protected static final String FOR_ALL = "\\forall";
    protected static final String MONO_TEXT = "\\texttt";
    protected static final String BOLD_TEXT = "\\textbf";
    protected static final String RIGHT_ARROW = "\\rightarrow";
    protected static final String INSTANTIATE_SIGN = "\\succeq";
    protected static final String LATEX_IN = "\\in";
    protected static final String VDASH = "\\vdash";
    protected static final String TREE_BEGIN = "\\begin{prooftree}";
    protected static final String TREE_END = "\\end{prooftree}";
    protected static final String ALIGN_BEGIN = "\\begin{align*}";
    protected static final String ALIGN_END = "\\end{align*}";

    protected static final String BUSSPROOFS = "\\usepackage{bussproofs}";
}
