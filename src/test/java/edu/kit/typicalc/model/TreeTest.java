package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    private static final Map<VarTerm, TypeAbstraction> TYPE_ASSUMPTIONS = new LinkedHashMap<>();
    private static final ConstTerm CONST = new IntegerTerm(10);
    private static final VarTerm VAR = new VarTerm("var");
    private static final AbsTerm ABS = new AbsTerm(VAR, VAR);
    private static final AppTerm APP = new AppTerm(VAR, VAR);
    private static final NamedType TYPE = new NamedType("type");
    private static final TypeAbstraction TYPE_ABS = new TypeAbstraction(TYPE);

    @BeforeAll
    static void setUp() {
        TYPE_ASSUMPTIONS.put(VAR, TYPE_ABS);
    }

    @Test
    void firstTypeVariableNewFactory() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR);
        assertEquals(tree.getFirstTypeVariable(), new TypeVariableFactory(TypeVariableKind.TREE).nextTypeVariable());
    }

    @Test
    void firstTypeVariableGivenFactory() {
        TypeVariableFactory factory = new TypeVariableFactory(TypeVariableKind.TREE);
        TypeVariableFactory factoryRef = new TypeVariableFactory(TypeVariableKind.TREE);
        for (int i = 0; i < 10; i++) {
            factory.nextTypeVariable();
            factoryRef.nextTypeVariable();
        }
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR, factory, false);
        assertEquals(tree.getFirstTypeVariable(), factoryRef.nextTypeVariable());
    }

    @Test
    void missingTypeAssumptionForVar() {
        Map<VarTerm, TypeAbstraction> ass = new LinkedHashMap<>();
        assertThrows(IllegalStateException.class, () -> {
                new Tree(ass, VAR);
        });
    }

    @Test
    void visitApp() {
        TypeVariable variable2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable variable3 = new TypeVariable(TypeVariableKind.TREE, 3);

        Tree tree = new Tree(TYPE_ASSUMPTIONS, APP);

        Conclusion varLeftConclusion = new Conclusion(TYPE_ASSUMPTIONS, VAR, variable2);
        Constraint varLeftConstraint = new Constraint(variable2, TYPE);
        InferenceStep varLeftStep = new VarStepDefault(TYPE_ABS, TYPE, varLeftConclusion, varLeftConstraint);

        Conclusion varRightConclusion = new Conclusion(TYPE_ASSUMPTIONS, VAR, variable3);
        Constraint varRightConstraint = new Constraint(variable3, TYPE);
        InferenceStep varRightStep = new VarStepDefault(TYPE_ABS, TYPE, varRightConclusion, varRightConstraint);

        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, APP, tree.getFirstTypeVariable());
        Constraint appConstraint = new Constraint(variable2, new FunctionType(variable3, tree.getFirstTypeVariable()));
        InferenceStep expectedStep = new AppStepDefault(varLeftStep, varRightStep, conclusion, appConstraint);

        assertEquals(expectedStep, tree.getFirstInferenceStep());

        List<Constraint> constraints = new ArrayList<>();
        constraints.add(appConstraint);
        constraints.add(varLeftConstraint);
        constraints.add(varRightConstraint);
        assertEquals(constraints, tree.getConstraints());
    }

    @Test
    void visitAbs() {
        TypeVariable variable2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable variable3 = new TypeVariable(TypeVariableKind.TREE, 3);

        Tree tree = new Tree(TYPE_ASSUMPTIONS, ABS);

        Map<VarTerm, TypeAbstraction> varTypeAss = new LinkedHashMap<>(TYPE_ASSUMPTIONS);
        varTypeAss.put(VAR, new TypeAbstraction(variable2));
        Conclusion varConclusion = new Conclusion(varTypeAss, VAR, variable3);
        Constraint varConstraint = new Constraint(variable2, variable3);
        InferenceStep varStep = new VarStepDefault(new TypeAbstraction(variable2), variable2, varConclusion,
                varConstraint);

        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, ABS, tree.getFirstTypeVariable());
        Constraint absConstraint = new Constraint(tree.getFirstTypeVariable(), new FunctionType(variable2, variable3));
        InferenceStep expectedStep = new AbsStepDefault(varStep, conclusion, absConstraint);

        assertEquals(expectedStep, tree.getFirstInferenceStep());

        List<Constraint> constraints = new ArrayList<>();
        constraints.add(absConstraint);
        constraints.add(varConstraint);
        assertEquals(constraints, tree.getConstraints());
    }

    @Test
    void visitConst() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, CONST);
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, CONST, tree.getFirstTypeVariable());
        Constraint constraint = new Constraint(NamedType.INT, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new ConstStepDefault(conclusion, constraint);
        assertEquals(expectedStep, tree.getFirstInferenceStep());
        assertEquals(Collections.singletonList(constraint), tree.getConstraints());
    }

    @Test
    void visitVar() {
        Tree tree = new Tree(TYPE_ASSUMPTIONS, VAR);
        Conclusion conclusion = new Conclusion(TYPE_ASSUMPTIONS, VAR, tree.getFirstTypeVariable());
        Constraint constraint = new Constraint(TYPE, tree.getFirstTypeVariable());
        InferenceStep expectedStep = new VarStepDefault(TYPE_ABS, TYPE, conclusion, constraint);
        assertEquals(expectedStep, tree.getFirstInferenceStep());
        assertEquals(Collections.singletonList(constraint), tree.getConstraints());
    }

    @Test
    void visitLet() {
        VarTerm x = new VarTerm("x");
        VarTerm f = new VarTerm("f");
        TypeVariable generated1 = new TypeVariable(TypeVariableKind.GENERATED_TYPE_ASSUMPTION, 1);
        TypeVariable variable2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable variable3 = new TypeVariable(TypeVariableKind.TREE, 3);
        TypeAbstraction generated1Abs = new TypeAbstraction(generated1);

        Map<VarTerm, TypeAbstraction> typeAssumptions = new LinkedHashMap<>();
        typeAssumptions.put(x, generated1Abs);

        LetTerm letTerm = new LetTerm(f, x, f);
        Tree tree = new Tree(typeAssumptions, letTerm);

        TypeVariableFactory refFac = new TypeVariableFactory(TypeVariableKind.TREE);
        refFac.nextTypeVariable();
        TypeInfererLet typeInfererLet = new TypeInfererLet(x, typeAssumptions, refFac);


        Map<VarTerm, TypeAbstraction> varRightTypeAss = new LinkedHashMap<>(typeAssumptions);
        varRightTypeAss.put(f, generated1Abs);
        Conclusion varRightConclusion = new Conclusion(varRightTypeAss, f, variable3);
        Constraint varRightConstraint = new Constraint(variable3, generated1);
        InferenceStep varRightStep = new VarStepWithLet(generated1Abs, generated1,
                varRightConclusion, varRightConstraint);

        Conclusion conclusion = new Conclusion(typeAssumptions, letTerm, tree.getFirstTypeVariable());
        Constraint letConstraint = new Constraint(tree.getFirstTypeVariable(), variable3);
        InferenceStep expectedStep = new LetStepDefault(conclusion, letConstraint, varRightStep, typeInfererLet);

        assertEquals(expectedStep, tree.getFirstInferenceStep());

        List<Constraint> constraints = new ArrayList<>();
        constraints.add(letConstraint);
        constraints.add(new Constraint(variable2, generated1));
        constraints.add(varRightConstraint);
        assertEquals(constraints, tree.getConstraints());
    }
}
