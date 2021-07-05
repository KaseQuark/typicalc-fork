package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LatexCreatorTypeTest {

    private final Model model = new ModelImpl();
    private TypeInfererInterface typeInferer;

    @Test
    void identityTest() {
        typeInferer = model.getTypeInferer("λx.x", new HashMap<>()).unwrap();
        assertEquals(TREE_VARIABLE + "_{2} " + RIGHT_ARROW + SPACE + TREE_VARIABLE + "_{2}",
                new LatexCreatorType(typeInferer.getType().get(), LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void generatedTypeTest() {
        typeInferer = model.getTypeInferer("x", new HashMap<>()).unwrap();
        assertEquals(GENERATED_ASSUMPTION_VARIABLE + "_{1}",
                new LatexCreatorType(typeInferer.getType().get(), LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void namedTypeTest() {
        typeInferer = model.getTypeInferer("5", new HashMap<>()).unwrap();
        assertEquals(MONO_TEXT + "{int}",
                new LatexCreatorType(typeInferer.getType().get(), LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void userVariableTest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("x", "t1");
        typeInferer = model.getTypeInferer("x", map).unwrap();
        assertEquals(USER_VARIABLE + "_{1}",
                new LatexCreatorType(typeInferer.getType().get(), LatexCreatorMode.NORMAL).getLatex());
    }
}
