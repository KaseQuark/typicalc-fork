package edu.kit.typicalc.model.step;

import edu.kit.typicalc.model.Conclusion;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.NamedType;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OnlyConclusionStepTest {

    @Test
    void getConstraintTest() {
        EmptyStep step = new EmptyStep();
        boolean thrown = false;
        try {
            step.getConstraint();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    void acceptTest() {
        TestStepVisitor testStepVisitor = new TestStepVisitor();
        OnlyConclusionStep step = new OnlyConclusionStep(
                new Conclusion(Collections.emptyMap(), new VarTerm(""), new NamedType("")));
        step.accept(testStepVisitor);
        assertEquals("onlyConclusion", testStepVisitor.visited);
    }
}