package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LatexCreatorConstraintsTest {
    private static final String EMPTY_CONSTRAINT_SET =
            ALIGN_BEGIN + AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + LATEX_CURLY_RIGHT + ALIGN_END;

    private static final String MGU_START = LATEX_NEW_LINE + AMPERSAND + SPLIT_BEGIN + SIGMA + COLON + EQUALS + MGU
            + PAREN_LEFT + CONSTRAINT_SET + PAREN_RIGHT;

    private final Model model = new ModelImpl();
    private TypeInfererInterface typeInferer;

    @Test
    void singleVarDefaultConstraintTest() {
        typeInferer = model.getTypeInferer("x", new HashMap<>()).unwrap();
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();

        String constraintSet = AMPERSAND + SPLIT_BEGIN + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + AMPERSAND
                + TREE_VARIABLE + "_{1}" + EQUALS + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + LATEX_CURLY_RIGHT
                + SPLIT_END;


        String mgu = "" + EQUALS + BRACKET_LEFT + AMPERSAND + TREE_VARIABLE + "_{1}"
                + SUBSTITUTION_SIGN + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + BRACKET_RIGHT + SPLIT_END;

        List<String> expected = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + constraintSet + ALIGN_END,
                ALIGN_BEGIN + constraintSet + MGU_START + EQUALS + UNIFY + PAREN_LEFT + LATEX_CURLY_LEFT + AMPERSAND
                        + TREE_VARIABLE + "_{1}" + EQUALS + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + LATEX_CURLY_RIGHT
                        + PAREN_RIGHT + SPLIT_END + ALIGN_END,
                ALIGN_BEGIN + constraintSet + MGU_START + mgu + ALIGN_END,
                ALIGN_BEGIN + constraintSet + MGU_START + mgu + LATEX_NEW_LINE + AMPERSAND + SIGMA + PAREN_LEFT
                        + TREE_VARIABLE + "_{1}" + PAREN_RIGHT + EQUALS + GENERATED_ASSUMPTION_VARIABLE
                        + "_{1}" + ALIGN_END);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }


    @Test
    void singleAbsDefaultConstraintTest() {
        typeInferer = model.getTypeInferer("λx.y", new HashMap<>()).unwrap();
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();

        String constraintSet1 = AMPERSAND + SPLIT_BEGIN + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + AMPERSAND
                + TREE_VARIABLE + "_{1}" + EQUALS + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE
                + TREE_VARIABLE + "_{3}" + LATEX_CURLY_RIGHT + SPLIT_END;

        String constraintSet2 = AMPERSAND + SPLIT_BEGIN + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + AMPERSAND
                + TREE_VARIABLE + "_{1}" + EQUALS + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE
                + TREE_VARIABLE + "_{3}" + COMMA + TREE_VARIABLE + "_{3}" + EQUALS + GENERATED_ASSUMPTION_VARIABLE
                + "_{1}" + LATEX_CURLY_RIGHT + SPLIT_END;

        String unify1 = MGU_START + EQUALS + UNIFY + PAREN_LEFT + LATEX_CURLY_LEFT + AMPERSAND
                + TREE_VARIABLE + "_{3}" + EQUALS + GENERATED_ASSUMPTION_VARIABLE + "_{1}";

        String subs2 = LATEX_NEW_LINE + CIRC
                + BRACKET_LEFT + AMPERSAND + TREE_VARIABLE + "_{1}" + SUBSTITUTION_SIGN + TREE_VARIABLE
                + "_{2}" + SPACE + RIGHT_ARROW + SPACE + TREE_VARIABLE + "_{3}" + BRACKET_RIGHT;

        String mgu = constraintSet2 + MGU_START + EQUALS + BRACKET_LEFT + AMPERSAND + TREE_VARIABLE + "_{1}"
                + SUBSTITUTION_SIGN + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE
                + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + COMMA + LATEX_NEW_LINE + AMPERSAND + TREE_VARIABLE
                + "_{3}" + SUBSTITUTION_SIGN + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + BRACKET_RIGHT
                + SPLIT_END;

        List<String> expected = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + constraintSet1 + ALIGN_END,
                ALIGN_BEGIN + constraintSet2 + ALIGN_END,
                ALIGN_BEGIN + constraintSet2 + unify1 + COMMA + LATEX_NEW_LINE + AMPERSAND + TREE_VARIABLE + "_{1}"
                        + EQUALS + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE + TREE_VARIABLE + "_{3}"
                        + LATEX_CURLY_RIGHT + PAREN_RIGHT + SPLIT_END + ALIGN_END,
                ALIGN_BEGIN + constraintSet2 + unify1 + LATEX_CURLY_RIGHT + PAREN_RIGHT + subs2 + SPLIT_END + ALIGN_END,
                ALIGN_BEGIN + constraintSet2 + MGU_START + EQUALS + BRACKET_LEFT + AMPERSAND + TREE_VARIABLE + "_{3}"
                        + SUBSTITUTION_SIGN + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + BRACKET_RIGHT + subs2 + SPLIT_END
                        + ALIGN_END,
                ALIGN_BEGIN + mgu + ALIGN_END,
                ALIGN_BEGIN + mgu + LATEX_NEW_LINE + AMPERSAND + SIGMA + PAREN_LEFT + TREE_VARIABLE + "_{1}"
                        + PAREN_RIGHT + EQUALS + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE
                        + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + ALIGN_END);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void lineBreak() {
        typeInferer = model.getTypeInferer("a b c d e f", new HashMap<>()).unwrap();
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();

        assertEquals("\\begin{aligned}&\\begin{split}C=\\{&\\alpha_{2}=\\alpha_{3} \\rightarrow \\alpha_{1},\\alpha_{4}=\\alpha_{5} \\rightarrow \\alpha_{2},\\alpha_{6}=\\alpha_{7} \\rightarrow \\alpha_{4},\\alpha_{8}=\\alpha_{9} \\rightarrow \\alpha_{6},\\alpha_{10}=\\alpha_{11} \\rightarrow \\alpha_{8},\\alpha_{10}=\\beta_{1},\\alpha_{11}=\\beta_{2},\\alpha_{9}=\\beta_{3},\\alpha_{7}=\\beta_{4},\\alpha_{5}=\\beta_{5},\\\\&\\alpha_{3}=\\beta_{6}\\}\\end{split}\\end{aligned}", actual.get(11));

        typeInferer = model.getTypeInferer("let g = a b c d e f in g", new HashMap<>()).unwrap();
        actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();

        assertEquals("\\begin{aligned}&\\begin{split}C=\\{&\\alpha_{1}=\\alpha_{13}\\}\\end{split}\\\\\n" +
                "&\\begin{split}C_{let}=\\{&\\alpha_{3}=\\alpha_{4} \\rightarrow \\alpha_{2},\\alpha_{5}=\\alpha_{6} \\rightarrow \\alpha_{3},\\alpha_{7}=\\alpha_{8} \\rightarrow \\alpha_{5},\\alpha_{9}=\\alpha_{10} \\rightarrow \\alpha_{7},\\alpha_{11}=\\alpha_{12} \\rightarrow \\alpha_{9},\\alpha_{11}=\\beta_{1},\\alpha_{12}=\\beta_{2},\\alpha_{10}=\\beta_{3},\\alpha_{8}=\\beta_{4},\\alpha_{6}=\\beta_{5},\\\\&\\alpha_{4}=\\beta_{6}\\}\\end{split}\\end{aligned}", actual.get(12));
    }

    @Test
    void emptyLetTypeAssumptions() {
        typeInferer = model.getTypeInferer("let g = 5 in g", new HashMap<>()).unwrap();
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();
        assertEquals("\\begin{aligned}&\\begin{split}C=\\{&\\alpha_{1}=\\alpha_{3}\\}\\end{split}\\\\\n" +
                "&\\begin{split}C_{let}=\\{&\\alpha_{2}=\\texttt{int}\\}\\end{split}\\\\&\\begin{split}\\sigma_{let}:=\\textit{mgu}(C_{let})=[&\\alpha_{2}\\mathrel{\\unicode{x21E8}}\\texttt{int}]\\end{split}\\\\&\\sigma_{let}(\\alpha_{2})=\\texttt{int}\\\\&\\Gamma'=\\sigma_{let}(\\emptyset),\\ \\texttt{g}:ta(\\sigma_{let}(\\alpha_{2}),\\sigma_{let}(\\emptyset))=\\texttt{g}:\\texttt{int}\\end{aligned}", actual.get(6));
    }

    @Test
    void excessiveMemoryUsageAvoided() {
        try {
            typeInferer = model.getTypeInferer("(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)(λx.x)", new HashMap<>()).unwrap();
            List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString, LatexCreatorMode.NORMAL).getEverything();
            // should have thrown IllegalStateException by now
            fail("did not throw exception");
        } catch (IllegalStateException e) {
            assertEquals("type too large!", e.getMessage());
        }
    }
}
