package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString).getEverything();

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
        List<String> actual = new LatexCreatorConstraints(typeInferer, Enum::toString).getEverything();

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
}
