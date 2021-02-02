package edu.kit.typicalc.model;

import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnificationTest {
    private static final TypeVariable a = new TypeVariable(TypeVariableKind.TREE, 1);
    private static final TypeVariable b = new TypeVariable(TypeVariableKind.TREE, 2);
    private static final TypeVariable c = new TypeVariable(TypeVariableKind.TREE, 3);
    private static final TypeVariable a4 = new TypeVariable(TypeVariableKind.TREE, 4);
    private static final TypeVariable a5 = new TypeVariable(TypeVariableKind.TREE, 5);
    private static final TypeVariable a6 = new TypeVariable(TypeVariableKind.TREE, 6);
    private static final TypeVariable a7 = new TypeVariable(TypeVariableKind.TREE, 7);

    @Test
    void simpleConstraints() {
        Deque<Constraint> constraints = new ArrayDeque<>();
        constraints.add(new Constraint(a, new FunctionType(b, c)));
        Deque<Constraint> initialConstraints = new ArrayDeque<>(constraints);
        Unification u = new Unification(constraints);
        List<UnificationStep> steps = u.getUnificationSteps();
        assertEquals(2, steps.size());
        assertEquals(new UnificationStep(new Result<>(new ArrayList<>()), new ArrayList<>(initialConstraints)), steps.get(0));
        List<Substitution> substitutions = new ArrayList<>(List.of(new Substitution(a, new FunctionType(b, c))));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>()), steps.get(1));
        assertEquals(substitutions, u.getSubstitutions().unwrap());
    }

    @Test
    void manyConstraints() {
        Deque<Constraint> constraints = new ArrayDeque<>();
        constraints.add(new Constraint(a, new FunctionType(b, c)));
        constraints.add(new Constraint(c, new FunctionType(a4, a5)));
        constraints.add(new Constraint(a6, new FunctionType(a7, a5)));
        constraints.add(new Constraint(a6, a4));
        constraints.add(new Constraint(a7, b));
        Deque<Constraint> initialConstraints = new ArrayDeque<>(constraints);

        Unification u = new Unification(constraints);
        List<UnificationStep> steps = u.getUnificationSteps();
        assertEquals(6, steps.size());
        assertEquals(new UnificationStep(new Result<>(new ArrayList<>()), new ArrayList<>(initialConstraints)), steps.get(0));

        initialConstraints.removeFirst();
        List<Substitution> substitutions = new ArrayList<>(List.of(new Substitution(a, new FunctionType(b, c))));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>(initialConstraints)), steps.get(1));

        initialConstraints.removeFirst();
        substitutions.add(new Substitution(c, new FunctionType(a4, a5)));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>(initialConstraints)), steps.get(2));

        initialConstraints.removeFirst();
        initialConstraints.removeFirst();
        initialConstraints.addFirst(new Constraint(new FunctionType(a7, a5), a4));
        substitutions.add(new Substitution(a6, new FunctionType(a7, a5)));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>(initialConstraints)), steps.get(3));

        initialConstraints.removeFirst();
        substitutions.add(new Substitution(a4, new FunctionType(a7, a5)));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>(initialConstraints)), steps.get(4));

        initialConstraints.removeFirst();
        substitutions.add(new Substitution(a7, b));
        assertEquals(new UnificationStep(new Result<>(
                substitutions
        ), new ArrayList<>(initialConstraints)), steps.get(5));

        assertEquals(substitutions, u.getSubstitutions().unwrap());
    }

    @Test
    void infiniteType() {
        Deque<Constraint> constraints = new ArrayDeque<>();
        constraints.add(new Constraint(a, new FunctionType(a, a)));
        Deque<Constraint> initialConstraints = new ArrayDeque<>(constraints);
        Unification u = new Unification(constraints);
        List<UnificationStep> steps = u.getUnificationSteps();
        assertEquals(2, steps.size());
        assertEquals(new UnificationStep(new Result<>(new ArrayList<>()), new ArrayList<>(initialConstraints)), steps.get(0));
        assertEquals(new UnificationStep(new Result<>(null,
                UnificationError.INFINITE_TYPE
        ), new ArrayList<>()), steps.get(1));
    }
}
