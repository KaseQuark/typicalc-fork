package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LatexCreatorTermTest {

    private final Model model = new ModelImpl();
    private TypeInfererInterface typeInferer;

    @Test
    void absTest() {
        typeInferer = model.getTypeInferer("λx.y", "").unwrap();
        assertEquals(LAMBDA + SPACE + MONO_TEXT + "{x}" + DOT_SIGN
                        + LATEX_SPACE + MONO_TEXT + "{y}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void appTest() {
        typeInferer = model.getTypeInferer("x y", "").unwrap();
        assertEquals(MONO_TEXT + "{x}" + LATEX_SPACE + MONO_TEXT + "{y}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void varTest() {
        typeInferer = model.getTypeInferer("x", "").unwrap();
        assertEquals(MONO_TEXT + "{x}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void integerTest() {
        typeInferer = model.getTypeInferer("5", "").unwrap();
        assertEquals(MONO_TEXT + "{5}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void booleanTest() {
        typeInferer = model.getTypeInferer("true", "").unwrap();
        assertEquals(MONO_TEXT + "{true}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void letTest() {
        typeInferer = model.getTypeInferer("let f = x in f", "").unwrap();
        assertEquals(MONO_TEXT + CURLY_LEFT + BOLD_TEXT + "{let}" + CURLY_RIGHT + LATEX_SPACE + MONO_TEXT + "{f}"
                        + EQUALS + MONO_TEXT + "{x}" + LATEX_SPACE + MONO_TEXT + CURLY_LEFT + BOLD_TEXT + "{in}"
                        + CURLY_RIGHT + LATEX_SPACE + MONO_TEXT + "{f}",
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

    @Test
    void appWithParenthesesTest() {
        typeInferer = model.getTypeInferer("x (y z)", "").unwrap();
        assertEquals(MONO_TEXT + "{x}" + LATEX_SPACE + PAREN_LEFT + MONO_TEXT + "{y}"
                        + LATEX_SPACE + MONO_TEXT + "{z}" + PAREN_RIGHT,
                new LatexCreatorTerm(typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(),
                        LatexCreatorMode.NORMAL).getLatex());
    }

}
