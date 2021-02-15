package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.AppStepDefault;
import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.step.LetStepDefault;
import edu.kit.typicalc.model.step.VarStepWithLet;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeInfererTest {

    @Test
    void inferLetTerm() {
        VarTerm k = new VarTerm("k");
        VarTerm x = new VarTerm("x");
        VarTerm y = new VarTerm("y");
        VarTerm a = new VarTerm("a");
        VarTerm b = new VarTerm("b");
        VarTerm c = new VarTerm("c");
        LambdaTerm lyx = new AbsTerm(y, x);
        LambdaTerm lxlyx = new AbsTerm(x, lyx);
        LambdaTerm kb = new AppTerm(k, b);
        LambdaTerm kbc = new AppTerm(kb, c);
        LambdaTerm ka = new AppTerm(k, a);
        LambdaTerm kakbc = new AppTerm(ka, kbc);
        LambdaTerm let = new LetTerm(k, lxlyx, kakbc);

        Type intT = new NamedType("int");
        Type boolT = new NamedType("bool");
        Type charT = new NamedType("char");
        TypeAbstraction intAbs = new TypeAbstraction(intT);
        TypeAbstraction boolAbs = new TypeAbstraction(boolT);
        TypeAbstraction charAbs = new TypeAbstraction(charT);

        Map<VarTerm, TypeAbstraction> givenTypeAssumptions = new LinkedHashMap<>();
        givenTypeAssumptions.put(a, intAbs);
        givenTypeAssumptions.put(b, boolAbs);
        givenTypeAssumptions.put(c, charAbs);

        TypeVariable a1 = new TypeVariable(TypeVariableKind.TREE, 1);
        TypeVariable a2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable a3 = new TypeVariable(TypeVariableKind.TREE, 3);
        TypeVariable a4 = new TypeVariable(TypeVariableKind.TREE, 4);
        TypeVariable a5 = new TypeVariable(TypeVariableKind.TREE, 5);
        TypeVariable a6 = new TypeVariable(TypeVariableKind.TREE, 6);
        TypeVariable a7 = new TypeVariable(TypeVariableKind.TREE, 7);
        TypeVariable a8 = new TypeVariable(TypeVariableKind.TREE, 8);
        TypeVariable a9 = new TypeVariable(TypeVariableKind.TREE, 9);
        TypeVariable a10 = new TypeVariable(TypeVariableKind.TREE, 10);
        TypeVariable a11 = new TypeVariable(TypeVariableKind.TREE, 11);
        TypeVariable a12 = new TypeVariable(TypeVariableKind.TREE, 12);
        TypeVariable a13 = new TypeVariable(TypeVariableKind.TREE, 13);
        TypeVariable a14 = new TypeVariable(TypeVariableKind.TREE, 14);
        TypeVariable a15 = new TypeVariable(TypeVariableKind.TREE, 15);
        TypeVariable a16 = new TypeVariable(TypeVariableKind.TREE, 16);
        TypeVariable a17 = new TypeVariable(TypeVariableKind.TREE, 17);
        TypeVariable a18 = new TypeVariable(TypeVariableKind.TREE, 18);
        TypeVariable a19 = new TypeVariable(TypeVariableKind.TREE, 19);


        TypeInferer typeInferer = new TypeInferer(let, givenTypeAssumptions);

        TypeVariableFactory refFac = new TypeVariableFactory(TypeVariableKind.TREE);
        refFac.nextTypeVariable();
        TypeInfererLet expectedTypeInfererLet = new TypeInfererLet(lxlyx, givenTypeAssumptions, refFac);

        TypeAbstraction resultingAbs = new TypeAbstraction(new FunctionType(a3, new FunctionType(a5, a3)),
                new HashSet<>(Arrays.asList(a3, a5)));
        Map<VarTerm, TypeAbstraction> typeAssumptionsRight = new LinkedHashMap<>(givenTypeAssumptions);
        typeAssumptionsRight.put(k, resultingAbs);

        Type varStepKLeftInst = new FunctionType(a12, new FunctionType(a13, a12));
        Conclusion varStepKLeftConc = new Conclusion(typeAssumptionsRight, k, a10);
        Constraint varStepKLeftConst = new Constraint(a10, varStepKLeftInst);
        InferenceStep varStepKLeft = new VarStepWithLet(resultingAbs, varStepKLeftInst,
                varStepKLeftConc, varStepKLeftConst);

        Conclusion varStepAConc = new Conclusion(typeAssumptionsRight, a, a11);
        Constraint varStepAConst = new Constraint(a11, intT);
        InferenceStep varStepA = new VarStepWithLet(intAbs, intT, varStepAConc, varStepAConst);

        Conclusion appStepKAConc = new Conclusion(typeAssumptionsRight, ka, a8);
        Constraint appStepKAConst = new Constraint(a10, new FunctionType(a11, a8));
        InferenceStep appStepKA = new AppStepDefault(varStepKLeft, varStepA, appStepKAConc, appStepKAConst);

        Type varStepKRightInst = new FunctionType(a18, new FunctionType(a19, a18));
        Conclusion varStepKRightConc = new Conclusion(typeAssumptionsRight, k, a16);
        Constraint varStepKRightConst = new Constraint(a16, varStepKRightInst);
        InferenceStep varStepKRight = new VarStepWithLet(resultingAbs, varStepKRightInst,
                varStepKRightConc, varStepKRightConst);

        Conclusion varStepBConc = new Conclusion(typeAssumptionsRight, b, a17);
        Constraint varStepBConst = new Constraint(a17, boolT);
        InferenceStep varStepB = new VarStepWithLet(boolAbs, boolT, varStepBConc, varStepBConst);

        Conclusion appStepKBConc = new Conclusion(typeAssumptionsRight, kb, a14);
        Constraint appStepKBConst = new Constraint(a16, new FunctionType(a17, a14));
        InferenceStep appStepKB = new AppStepDefault(varStepKRight, varStepB, appStepKBConc, appStepKBConst);

        Conclusion varStepCConc = new Conclusion(typeAssumptionsRight, c, a15);
        Constraint varStepCConst = new Constraint(a15, charT);
        InferenceStep varStepC = new VarStepWithLet(charAbs, charT, varStepCConc, varStepCConst);

        Conclusion appStepKBCConc = new Conclusion(typeAssumptionsRight, kbc, a9);
        Constraint appStepKBCConst = new Constraint(a14, new FunctionType(a15, a9));
        InferenceStep appStepKBC = new AppStepDefault(appStepKB, varStepC, appStepKBCConc, appStepKBCConst);

        Conclusion appStepKAKBCConc = new Conclusion(typeAssumptionsRight, kakbc, a7);
        Constraint appStepKAKBCConst = new Constraint(a8, new FunctionType(a9, a7));
        InferenceStep appStepKAKBC = new AppStepDefault(appStepKA, appStepKBC, appStepKAKBCConc, appStepKAKBCConst);

        Conclusion letConc = new Conclusion(givenTypeAssumptions, let, a1);
        Constraint letConst = new Constraint(a1, a7);
        InferenceStep expectedFirstInferenceStep
                = new LetStepDefault(letConc, letConst, appStepKAKBC, expectedTypeInfererLet);

        assertEquals(expectedFirstInferenceStep, typeInferer.getFirstInferenceStep());

        List<Constraint> expectedConstraints = new ArrayList<>();
        expectedConstraints.add(letConst);
        expectedConstraints.addAll(expectedTypeInfererLet.getLetConstraints());
        expectedConstraints.add(appStepKAKBCConst);
        expectedConstraints.add(appStepKAConst);
        expectedConstraints.add(varStepKLeftConst);
        expectedConstraints.add(varStepAConst);
        expectedConstraints.add(appStepKBCConst);
        expectedConstraints.add(appStepKBConst);
        expectedConstraints.add(varStepKRightConst);
        expectedConstraints.add(varStepBConst);
        expectedConstraints.add(varStepCConst);

        assertTrue(typeInferer.getUnificationSteps().isPresent());
        assertEquals(expectedConstraints, typeInferer.getUnificationSteps().get().get(0).getConstraints());

        List<Substitution> expectedMGU = new ArrayList<>();
        expectedMGU.add(new Substitution(a1, intT));
        expectedMGU.add(new Substitution(a2, new FunctionType(a3, new FunctionType(a5, a3))));
        expectedMGU.add(new Substitution(a4, new FunctionType(a5, a3)));
        expectedMGU.add(new Substitution(a6, a3));
        expectedMGU.add(new Substitution(a7, intT));
        expectedMGU.add(new Substitution(a8, new FunctionType(boolT, intT)));
        expectedMGU.add(new Substitution(a9, boolT));
        expectedMGU.add(new Substitution(a10, new FunctionType(intT, new FunctionType(boolT, intT))));
        expectedMGU.add(new Substitution(a11, intT));
        expectedMGU.add(new Substitution(a12, intT));
        expectedMGU.add(new Substitution(a13, boolT));
        expectedMGU.add(new Substitution(a14, new FunctionType(charT, boolT)));
        expectedMGU.add(new Substitution(a15, charT));
        expectedMGU.add(new Substitution(a16, new FunctionType(boolT, new FunctionType(charT, boolT))));
        expectedMGU.add(new Substitution(a17, boolT));
        expectedMGU.add(new Substitution(a18, boolT));
        expectedMGU.add(new Substitution(a19, charT));

        assertTrue(typeInferer.getMGU().isPresent());
        assertEquals(expectedMGU, typeInferer.getMGU().get());

        assertTrue(typeInferer.getType().isPresent());
        assertEquals(intT, typeInferer.getType().get());
    }

    @Test
    void equalsTest() {
        EqualsVerifier.forClass(TypeInferer.class).usingGetClass().verify();
    }
}
