package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.*;
import edu.kit.typicalc.model.type.*;

import java.util.*;

/**
 * Models the proof tree formed when the type of a lambda term is inferred.
 * A Tree consists of a tree-like structure of inference steps and a list of constraints that
 * are part of these steps. It is guaranteed that these members can be accessed right after
 * instantiation (no additional initialization is required).
 */
public class Tree implements TermVisitorTree {

    private final TypeVariableFactory typeVarFactory;
    private final StepFactory stepFactory;

    private final TypeVariable firstTypeVariable;
    private final InferenceStep firstInferenceStep;
    private final List<Constraint> constraints;

    private boolean failedSubInference;

    /**
     * Initializes a new Tree representing the proof tree for the type inference of the given
     * lambda term considering the given type assumptions. The inference step structure
     * and constraint list are generated here.
     *
     * @param typeAssumptions  the type assumptions to consider when generating the tree
     * @param lambdaTerm the lambda term to generate the tree for
     */
    protected Tree(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm) {
        this(typeAssumptions, lambdaTerm, new TypeVariableFactory(TypeVariableKind.TREE), false);
    }

    /**
     * Initializes a new Tree representing the proof tree for the type inference of the given
     * lambda term considering the given type assumptions. This constructor is supposed
     * to be used if the lambda term is part of a let term. To generate unique type
     * variable names, a type variable factory is provided as a parameter. The inference
     * step structure and constraint list are generated here.
     *
     * @param typeAssumptions the type assumptions to consider when generating the tree
     * @param lambdaTerm the lambda term to generate the tree for
     * @param typeVariableFactory the type variable factory to use
     * @param partOfLetTerm indicates whether the tree is generated for a sub-inference that is part of a let term
     */
    protected Tree(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm,
                   TypeVariableFactory typeVariableFactory, boolean partOfLetTerm) {
        this.typeVarFactory = typeVariableFactory;
        this.constraints = new ArrayList<>();

        this.stepFactory = lambdaTerm.hasLet() || partOfLetTerm ? new StepFactoryWithLet() : new StepFactoryDefault();

        this.failedSubInference = false;

        this.firstTypeVariable = typeVarFactory.nextTypeVariable();
        this.firstInferenceStep = lambdaTerm.accept(this, typeAssumptions, firstTypeVariable);
    }

    /**
     * Returns the first (on the undermost level) inference step of the proof tree, containing
     * the original lambda term that is being type-inferred in this tree and presenting an
     * entry point for the tree-like inference step structure.
     *
     * @return the first inference step of the tree
     */
    protected InferenceStep getFirstInferenceStep() {
        return firstInferenceStep;
    }

    /**
     * Returns the first type variable the original lambda term was assigned in the first inference step.
     *
     * @return the first type variable
     */
    protected TypeVariable getFirstTypeVariable() {
        return firstTypeVariable;
    }

    /**
     * Returns the list of constraints that formed while generating the inference step tree
     * structure, needed for the subsequent unification.
     *
     * @return the constraint list of the tree
     */
    protected List<Constraint> getConstraints() {
        return constraints;
    }

    /**
     * Indicates whether the tree contains a sub-inference of a let step that failed
     *
     * @return true, if the tree contains a failed sub-inference, false otherwise
     */
    protected boolean hasFailedSubInference() {
        return failedSubInference;
    }

    @Override
    public InferenceStep visit(AppTerm appTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        Type leftType = typeVarFactory.nextTypeVariable();
        Type rightType = typeVarFactory.nextTypeVariable();

        Type function = new FunctionType(rightType, conclusionType);
        Constraint newConstraint = new Constraint(leftType, function);
        constraints.add(newConstraint);

        InferenceStep leftPremise = appTerm.getFunction().accept(this, typeAssumptions, leftType);
        InferenceStep rightPremise = appTerm.getParameter().accept(this, typeAssumptions, rightType);

        Conclusion conclusion = new Conclusion(typeAssumptions, appTerm, conclusionType);
        return stepFactory.createAppStep(leftPremise, rightPremise, conclusion, newConstraint);
    }

    @Override
    public InferenceStep visit(AbsTerm absTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        Map<VarTerm, TypeAbstraction> extendedTypeAssumptions = new LinkedHashMap<>(typeAssumptions);
        Type assType = typeVarFactory.nextTypeVariable();
        TypeAbstraction assAbs = new TypeAbstraction(assType);
        extendedTypeAssumptions.put(absTerm.getVariable(), assAbs);

        Type premiseType = typeVarFactory.nextTypeVariable();

        Type function = new FunctionType(assType, premiseType);
        Constraint newConstraint = new Constraint(conclusionType, function);
        constraints.add(newConstraint);

        InferenceStep premise = absTerm.getInner().accept(this, extendedTypeAssumptions, premiseType);

        Conclusion conclusion = new Conclusion(typeAssumptions, absTerm, conclusionType);
        return stepFactory.createAbsStep(premise, conclusion, newConstraint);
    }

    @Override
    public InferenceStep visit(LetTerm letTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        TypeInfererLet typeInfererLet = new TypeInfererLet(
                letTerm.getVariableDefinition(), typeAssumptions, typeVarFactory);

        Type premiseType = typeVarFactory.nextTypeVariable();
        Constraint newConstraint = new Constraint(conclusionType, premiseType);
        InferenceStep premise;

        if (typeInfererLet.getType().isPresent()) {
            Map<VarTerm, TypeAbstraction> extendedTypeAssumptions = new LinkedHashMap<>(typeAssumptions);
            extendedTypeAssumptions.replaceAll((key, value) -> {
                Type newType = value.getInnerType();
                for (Substitution substitution : typeInfererLet.getMGU().orElseThrow(IllegalStateException::new)) {
                    if (value.getQuantifiedVariables().contains(substitution.getVariable())) {
                        continue;
                    }
                    newType = newType.substitute(substitution);
                }
                return new TypeAbstraction(newType, value.getQuantifiedVariables());
            });

            TypeAbstraction newTypeAbstraction = new TypeAbstraction(
                    typeInfererLet.getType().orElseThrow(IllegalStateException::new), extendedTypeAssumptions);
            extendedTypeAssumptions.put(letTerm.getVariable(), newTypeAbstraction);

            constraints.add(newConstraint);
            constraints.addAll(typeInfererLet.getLetConstraints());

            premise = letTerm.getInner().accept(this, extendedTypeAssumptions, premiseType);
        } else {
            premise = new EmptyStep();
            failedSubInference = true;
        }

        Conclusion conclusion = new Conclusion(typeAssumptions, letTerm, conclusionType);
        return stepFactory.createLetStep(conclusion, newConstraint, premise, typeInfererLet);
    }

    @Override
    public InferenceStep visit(ConstTerm constant, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        Constraint newConstraint = new Constraint(conclusionType, constant.getType());
        constraints.add(newConstraint);

        Conclusion conclusion = new Conclusion(typeAssumptions, constant, conclusionType);
        return stepFactory.createConstStep(conclusion, newConstraint);
    }

    @Override
    public InferenceStep visit(VarTerm varTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        TypeAbstraction premiseAbs = typeAssumptions.get(varTerm);
        if (premiseAbs == null) {
            throw new IllegalStateException("Cannot create VarStep for VarTerm '"
                    + varTerm.getName() + "' without appropriate type assumption");
        }
        Type instantiation = premiseAbs.instantiate(typeVarFactory);

        Constraint newConstraint = new Constraint(conclusionType, instantiation);
        constraints.add(newConstraint);

        Conclusion conclusion = new Conclusion(typeAssumptions, varTerm, conclusionType);
        return stepFactory.createVarStep(premiseAbs, instantiation, conclusion, newConstraint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tree tree = (Tree) o;
        return failedSubInference == tree.failedSubInference && firstTypeVariable.equals(tree.firstTypeVariable)
                && firstInferenceStep.equals(tree.firstInferenceStep) && constraints.equals(tree.constraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstTypeVariable, firstInferenceStep, constraints, failedSubInference);
    }
}
