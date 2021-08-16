package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class ExplanationCreatorTest {
    @Test
    void itWorks() {
        TypeInfererInterface model = new ModelImpl().getTypeInferer("λx.x", "").unwrap();
        new ExplanationCreator(model, Locale.GERMAN).getExplanationTexts();
    }
}
