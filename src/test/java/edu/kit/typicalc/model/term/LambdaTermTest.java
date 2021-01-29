package edu.kit.typicalc.model.term;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaTermTest {

    private static VarTerm x1;
    private static VarTerm x2;
    private static VarTerm x3;
    private static VarTerm x4;
    private static ConstTerm c;


    @BeforeAll
    static void setUp() {
        x1 = new VarTerm("x1");
        x2 = new VarTerm("x2");
        x3 = new VarTerm("x3");
        x4 = new VarTerm("x4");

        c = new BooleanTerm(true);
    }

    @Test
    void freeVariablesVarTerm() {
        assertEquals(new HashSet<>(Collections.singletonList(x1)), x1.getFreeVariables());
    }

    @Test
    void freeVariablesConstTerm() {
        assertEquals(new HashSet<>(), c.getFreeVariables());
    }

    @Test
    void freeVariablesAbsTerm() {
        AbsTerm abs1 = new AbsTerm(x1, x2);
        assertEquals(new HashSet<>(Collections.singletonList(x2)), abs1.getFreeVariables());

        AbsTerm abs2 = new AbsTerm(x1, x1);
        assertEquals(new HashSet<>(), abs2.getFreeVariables());

        AbsTerm abs3 = new AbsTerm(x1, new AbsTerm(x2, new AppTerm(x1, x2)));
        assertEquals(new HashSet<>(), abs3.getFreeVariables());

        AbsTerm abs4 = new AbsTerm(x1, new AppTerm(x1, x2));
        assertEquals(new HashSet<>(Collections.singletonList(x2)), abs4.getFreeVariables());

        AbsTerm abs5 = new AbsTerm(x1, c);
        assertEquals(new HashSet<>(), abs5.getFreeVariables());

        AbsTerm abs6 = new AbsTerm(x1, new AppTerm(x1, new AppTerm(x2, x3)));
        assertEquals(new HashSet<>(Arrays.asList(x2, x3)), abs6.getFreeVariables());
    }

    @Test
    void freeVariablesAppTerm() {
        AppTerm app1 = new AppTerm(x1, x1);
        assertEquals(new HashSet<>(Collections.singletonList(x1)), app1.getFreeVariables());

        AppTerm app2 = new AppTerm(x1, x2);
        assertEquals(new HashSet<>(Arrays.asList(x1, x2)), app2.getFreeVariables());

        AppTerm app3 = new AppTerm(x1, new AbsTerm(x2, new AppTerm(x1, x2)));
        assertEquals(new HashSet<>(Collections.singletonList(x1)), app3.getFreeVariables());

        AppTerm app4 = new AppTerm(x1, c);
        assertEquals(new HashSet<>(Collections.singletonList(x1)), app4.getFreeVariables());

        AppTerm app5 = new AppTerm(x1, new AbsTerm(x1, new AppTerm(x1, x2)));
        assertEquals(new HashSet<>(Arrays.asList(x1, x2)), app5.getFreeVariables());
    }

    @Test
    void freeVariablesLetTerm() {
        LetTerm let1 = new LetTerm(x1, x2, x1);
        assertEquals(new HashSet<>(Collections.singletonList(x2)), let1.getFreeVariables());

        LetTerm let2 = new LetTerm(x1, new AbsTerm(x2, x2), x1);
        assertEquals(new HashSet<>(), let2.getFreeVariables());

        LetTerm let3 = new LetTerm(x1, new AbsTerm(x3, x3), new AbsTerm(x2, new AppTerm(x1, x2)));
        assertEquals(new HashSet<>(), let3.getFreeVariables());

        LetTerm let4 = new LetTerm(x1, new AppTerm(x2, c), new AbsTerm(x3, new AppTerm(x3, new AppTerm(x1, x4))));
        assertEquals(new HashSet<>(Arrays.asList(x2, x4)), let4.getFreeVariables());
    }

    @Test
    void freeVariablesComplexTerm() {
        // let x1 = (x2 let x1 = \x2. x2 in x1) in x3 (\x3. x1 x4 \x4. x4)
        LetTerm let = new LetTerm(x1,
                new AppTerm(x2, new LetTerm(x1, new AbsTerm(x2, x2), x1)),
                new AppTerm(x3, new AppTerm(x3, new AppTerm(x1,
                        new AppTerm(x4, new AbsTerm(x4, x4))))));
        assertEquals(new HashSet<>(Arrays.asList(x2, x3, x4)), let.getFreeVariables());
    }
}
