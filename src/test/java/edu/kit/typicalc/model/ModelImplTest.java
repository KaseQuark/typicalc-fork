package edu.kit.typicalc.model;

import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.FunctionType;
import edu.kit.typicalc.model.type.NamedType;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeVariable;
import edu.kit.typicalc.model.type.TypeVariableKind;
import edu.kit.typicalc.util.Result;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ModelImplTest {

    @Test
    void getTypeInferer() {
        ModelImpl model = new ModelImpl();
        assertTrue(model.getTypeInferer("test.x.x.test", "a.x: 3.int").isError());
        assertTrue(model.getTypeInferer("x", "a.x: 3.int").isError());

        Result<TypeInfererInterface, ParseError> result = model.getTypeInferer("λy.x", "x: int");
        VarTerm y = new VarTerm("y");
        VarTerm x = new VarTerm("x");
        LambdaTerm term = new AbsTerm(y, x);
        assertTrue(result.isOk());
        TypeInfererInterface typeInference = result.unwrap();
        assertTrue(typeInference.getType().isPresent());
        TypeVariable a1 = new TypeVariable(TypeVariableKind.TREE, 1);
        TypeVariable a2 = new TypeVariable(TypeVariableKind.TREE, 2);
        TypeVariable a3 = new TypeVariable(TypeVariableKind.TREE, 3);
        Type resultFunction = new FunctionType(a2, NamedType.INT);
        assertEquals(
                resultFunction,
                typeInference.getType().get());
        assertEquals(
                List.of(new Substitution(a1, resultFunction), new Substitution(a3, NamedType.INT)),
                typeInference.getMGU().get());
        assertEquals(
                new AbsStepDefault(
                        new VarStepDefault(
                                new TypeAbstraction(NamedType.INT), NamedType.INT,
                                new Conclusion(Map.of(
                                        x, new TypeAbstraction(NamedType.INT),
                                        y, new TypeAbstraction(a2)),
                                        x, a3),
                                new Constraint(a3, NamedType.INT),
                                1
                        ),
                        new Conclusion(Map.of(x, new TypeAbstraction(NamedType.INT)), term, a1),
                        new Constraint(a1, new FunctionType(a2, a3)),
                        0
                ), typeInference.getFirstInferenceStep()
        );
    }

    @Test
    void quantifiedTypeAssumption() {
        ModelImpl model = new ModelImpl();

        Result<TypeInfererInterface, ParseError> result = model.getTypeInferer("(id id) (id true)",
                "id: ∀ t1 . t1 -> t1");
        if (result.isError()) {
            System.out.println(result.unwrapError());
            fail();
        }
        assertTrue(result.isOk());
        TypeInfererInterface typeInference = result.unwrap();
        assertTrue(typeInference.getType().isPresent());
        assertEquals(
                new NamedType("boolean"),
                typeInference.getType().get());
        // spot check unification steps
        assertEquals(
                new Substitution(new TypeVariable(TypeVariableKind.TREE, 4), new FunctionType(
                        new FunctionType(new NamedType("boolean"), new NamedType("boolean")),
                        new FunctionType(new NamedType("boolean"), new NamedType("boolean"))
                )),
                typeInference.getMGU().get().get(3)
        );
    }

    @Test
    void letTermTypeAssumptions() {
        Model model = new ModelImpl();
        TypeInfererInterface typer = model.getTypeInferer("(let g = λx.x in g) g", "").unwrap();
        AppStep first = (AppStep) typer.getFirstInferenceStep();
        LetStep second = (LetStep) first.getPremise1();
        VarStep third = (VarStep) second.getPremise();
        VarTerm[] vars = third.getConclusion().getTypeAssumptions().keySet().toArray(new VarTerm[1]);
        assertEquals(0, vars[0].uniqueIndex());
    }
}
