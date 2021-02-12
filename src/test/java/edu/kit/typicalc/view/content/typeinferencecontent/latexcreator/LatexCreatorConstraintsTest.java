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

    private final Model model = new ModelImpl();
    private TypeInfererInterface typeInferer;

    // todo tests should test all of getEverything, not only the first 2 parts
    @Test
    void singleVarDefaultConstraintTest() {
        typeInferer = model.getTypeInferer("x", new HashMap<>()).unwrap();
        List<String> expected = new LatexCreatorConstraints(typeInferer).getEverything();

        String constraintSet = AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + TREE_VARIABLE + "_{1}" + EQUALS
                + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + LATEX_CURLY_RIGHT;

        String mguStart = LATEX_NEW_LINE + AMPERSAND + SPLIT_BEGIN + SIGMA + COLON + EQUALS + MGU + PAREN_LEFT
                + CONSTRAINT_SET + PAREN_RIGHT;

        String mgu = "" + EQUALS + BRACKET_LEFT + AMPERSAND + TREE_VARIABLE + "_{1}"
                + SUBSTITUTION_SIGN + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + BRACKET_RIGHT + SPLIT_END;

        List<String> actual = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + constraintSet + ALIGN_END,
                ALIGN_BEGIN + constraintSet + mguStart + EQUALS + UNIFY + PAREN_LEFT + LATEX_CURLY_LEFT + AMPERSAND
                        + TREE_VARIABLE + "_{1}" + EQUALS + GENERATED_ASSUMPTION_VARIABLE + "_{1}" + PAREN_RIGHT
                        + LATEX_CURLY_RIGHT + SPLIT_END + ALIGN_END,
                ALIGN_BEGIN + constraintSet + mguStart + mgu + ALIGN_END,
                ALIGN_BEGIN + constraintSet + mguStart + mgu + ALIGN_END,
                ALIGN_BEGIN + constraintSet + mguStart + mgu + LATEX_NEW_LINE + AMPERSAND + SIGMA + PAREN_LEFT
                        + TREE_VARIABLE + "_{1}" + PAREN_RIGHT + EQUALS + GENERATED_ASSUMPTION_VARIABLE
                        + "_{1}" + ALIGN_END);

        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }


    @Test
    void singleAbsDefaultConstraintTest() {
        typeInferer = model.getTypeInferer("λx.y", new HashMap<>()).unwrap();
        List<String> expected = new LatexCreatorConstraints(typeInferer).getEverything();

        List<String> actual = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + TREE_VARIABLE + "_{1}" + EQUALS
                        + TREE_VARIABLE + "_{2}" + SPACE + RIGHT_ARROW + SPACE + TREE_VARIABLE + "_{3}"
                        + LATEX_CURLY_RIGHT + ALIGN_END);

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }


    @Test
    void singleAppConstraintTest() {
        typeInferer = model.getTypeInferer("x y", new HashMap<>()).unwrap();
        List<String> expected = new LatexCreatorConstraints(typeInferer).getEverything();

        List<String> actual = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + TREE_VARIABLE + "_{2}" + EQUALS
                        + TREE_VARIABLE + "_{3}" + SPACE + RIGHT_ARROW + SPACE + TREE_VARIABLE + "_{1}"
                        + LATEX_CURLY_RIGHT + ALIGN_END);

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }


    @Test
    void singleConstConstraintTest() {
        typeInferer = model.getTypeInferer("5", new HashMap<>()).unwrap();
        List<String> expected = new LatexCreatorConstraints(typeInferer).getEverything();

        List<String> actual = List.of(EMPTY_CONSTRAINT_SET,
                ALIGN_BEGIN + AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + TREE_VARIABLE + "_{1}" + EQUALS
                        + MONO_TEXT + "{int}" + LATEX_CURLY_RIGHT + ALIGN_END);

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }
}
