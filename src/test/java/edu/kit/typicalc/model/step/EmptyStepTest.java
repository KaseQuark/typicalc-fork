package edu.kit.typicalc.model.step;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptyStepTest {

    @Test
    void getConclusionTest() {
        EmptyStep step = new EmptyStep();
        boolean thrown = false;
        try {
            step.getConclusion();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

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
        EmptyStep step = new EmptyStep();
        step.accept(testStepVisitor);
        assertEquals("empty", testStepVisitor.visited);
    }
}