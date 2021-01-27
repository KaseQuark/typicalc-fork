package edu.kit.typicalc.model;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.term.AbsTerm;
import edu.kit.typicalc.model.term.AppTerm;
import edu.kit.typicalc.model.term.ConstTerm;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.TermVisitorTree;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.model.type.TypeAssumption;

import java.util.List;
import java.util.Map;

/**
 * Models the proof tree formed when the type of a lambda term is inferred.
 * A Tree consists of a tree-like structure of inference steps and a list of constraints that
 * are part of these steps. It is guaranteed that these members can be accessed right after
 * instantiation (no additional initialization is required).
 */
public class Tree implements TermVisitorTree {

    /**
     * Initializes a new Tree representing the proof tree for the type inference of the given
     * lambda term considering the given type assumptions. The inference step structure
     * and constraint list are generated here.
     *
     * @param typeAssumptions  the type assumptions to consider when generating the tree
     * @param lambdaTerm the lambda term to generate the tree for
     */
    protected Tree(Map<VarTerm, TypeAbstraction> typeAssumptions, LambdaTerm lambdaTerm) {
        // TODO
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
        // TODO
    }

    /**
     * Returns the first (on the undermost level) inference step of the proof tree, containing
     * the original lambda term that is being type-inferred in this tree and presenting an
     * entry point for the tree-like inference step structure.
     *
     * @return the first inference step of the tree
     */
    protected InferenceStep getFirstInferenceStep() {
        return null;
        // TODO;
    }

    /**
     * Returns the list of constraints that formed while generating the inference step tree
     * structure, needed for the subsequent unification.
     *
     * @return the constraint list of the tree
     */
    protected List<Constraint> getConstraints() {
        return null;
        // TODO
    }

    @Override
    public InferenceStep visit(AppTerm appTerm, List<TypeAssumption> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(AbsTerm absTerm, List<TypeAssumption> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(LetTerm letTerm, List<TypeAssumption> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(ConstTerm constTerm, List<TypeAssumption> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }

    @Override
    public InferenceStep visit(VarTerm varTerm, List<TypeAssumption> typeAssumptions, Type conclusionType) {
        return null; // TODO
    }
}
