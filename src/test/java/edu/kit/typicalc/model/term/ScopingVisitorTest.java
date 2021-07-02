package edu.kit.typicalc.model.term;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ScopingVisitorTest {
    @Test
    void simple() {
        AppTerm x = new AppTerm(
                new AbsTerm(new VarTerm("x"), new VarTerm("x")),
                new VarTerm("x")
        ); // (λx.x)x

        ScopingVisitor visitor = new ScopingVisitor();
        x.accept(visitor);
        assertEquals(0, ((AbsTerm) x.getFunction()).getVariable().uniqueIndex());
        assertEquals(0, ((VarTerm) ((AbsTerm) x.getFunction()).getInner()).uniqueIndex());
        assertEquals(-1, ((VarTerm) x.getParameter()).uniqueIndex());
    }

    @Test
    void simpleNesting() {
        AppTerm x = new AppTerm(
                new AbsTerm(new VarTerm("x"),
                        new AppTerm(new AbsTerm(new VarTerm("x"), new VarTerm("x")),
                                new VarTerm("x"))),
                new VarTerm("x")
        ); // (λx.(λx.x)x)x

        ScopingVisitor visitor = new ScopingVisitor();
        x.accept(visitor);
        assertEquals(0,
                ((AbsTerm) x.getFunction()).getVariable().uniqueIndex());
        assertEquals(1,
                ((AbsTerm) ((AppTerm) ((AbsTerm) x.getFunction()).getInner()).getFunction()).getVariable().uniqueIndex());
        assertEquals(1,
                ((VarTerm) ((AbsTerm) ((AppTerm) ((AbsTerm) x.getFunction()).getInner()).getFunction()).getInner()).uniqueIndex());
        assertEquals(0,
                ((VarTerm) ((AppTerm) ((AbsTerm) x.getFunction()).getInner()).getParameter()).uniqueIndex());
        assertEquals(-1, ((VarTerm) x.getParameter()).uniqueIndex());
    }

    @Test
    void letTerm() {
        // let x = λx.x in x 5
        LetTerm x = new LetTerm(
                new VarTerm("x"),
                new AbsTerm(new VarTerm("x"), new VarTerm("x")),
                new AppTerm(new VarTerm("x"), new IntegerTerm(5)));

        ScopingVisitor visitor = new ScopingVisitor();
        x.accept(visitor);
        assertEquals(0,
                x.getVariable().uniqueIndex());
        assertEquals(1,
                ((AbsTerm) x.getVariableDefinition()).getVariable().uniqueIndex());
        assertEquals(1,
                ((VarTerm) ((AbsTerm) x.getVariableDefinition()).getInner()).uniqueIndex());
        assertEquals(0,
                ((VarTerm) ((AppTerm) x.getInner()).getFunction()).uniqueIndex());
    }
}
