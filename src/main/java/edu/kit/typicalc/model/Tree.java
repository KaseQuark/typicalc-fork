package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.step.StepFactory;
import edu.kit.typicalc.model.step.StepFactoryDefault;
import edu.kit.typicalc.model.step.StepFactoryWithLet;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.ConstTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.TermVisitorTree;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Initializes a new Tree representing the proof tree for the type inference of the given
     * lambda term considering the given type assumptions. The inference step structure
     * and constraint list are generated here.
     *
     * @param typeAssumptions  the type assumptions to consider when generating the tree
     * @param lambdaTerm the lambda term to generate the tree for
     */
    protected Tree(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm) {
        this(typeAssumptions, lambdaTerm, new TypeVariableFactory());
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
     */
    protected Tree(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm,
                   TypeVariableFactory typeVariableFactory) {
        // TODO: null checks
        this.typeVarFactory = typeVariableFactory;
        this.constraints = new ArrayList<>();

        this.stepFactory = lambdaTerm.hasLet() ? new StepFactoryWithLet() : new StepFactoryDefault();

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

    @Override
    public InferenceStep visit(AppTerm appTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(AbsTerm absTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(LetTerm letTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(ConstTerm constTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(VarTerm varTerm, Map<VarTerm, TypeAbstraction> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }
}
