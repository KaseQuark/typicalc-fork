package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

public class StepAnnotatorTest {
    @Test
    void canAnnotateFailedLetTerm() {
        Model model = new ModelImpl();
        TypeInfererInterface typer = model.getTypeInferer("let f = (λx.y x) (y 5) in λg.f 3", "y: int->(int->boolean)").unwrap();
        typer.getFirstInferenceStep().accept(new StepAnnotator());
    }
}
