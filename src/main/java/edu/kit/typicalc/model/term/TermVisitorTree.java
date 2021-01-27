package edu.kit.typicalc.model.term;

import edu.kit.typicalc.model.step.InferenceStep;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeAssumption;

import java.util.List;

public interface TermVisitorTree {
	/**
	 * Returns an {@link edu.kit.typicalc.model.step.AppStep} suiting the given application (lambda term)
	 * to type-infer and the type assumptions to consider.
	 * Simultaneously assembles the tree's constraint list.
	 * @param appTerm the application (lambda term) to build the inference step structure for,
	 * 					i.e. the lambda term in the conclusion of the returned inference step
	 * @param typeAssumptions the type assumptions to consider,
	 * 					i.e. the type assumptions in the conclusion of the returned inference step
	 * @param conclusionType the type that the lambda term in the conclusion
	 * 					of the returned inference step will be assigned
	 * @return an {@link edu.kit.typicalc.model.step.AppStep}
	 */
	InferenceStep visit(AppTerm appTerm, List<TypeAssumption> typeAssumptions, Type conclusionType);

	/**
	 * Returns an {@link edu.kit.typicalc.model.step.AbsStep} suiting the given abstraction (lambda term)
	 * to type-infer and the type assumptions to consider.
	 * Simultaneously assembles the tree's constraint list.
	 * @param absTerm the abstraction (lambda term) to build the inference step structure for,
	 * 					i.e. the lambda term in the conclusion of the returned inference step
	 * @param typeAssumptions the type assumptions to consider,
	 * 					i.e. the type assumptions in the conclusion of the returned inference step
	 * @param conclusionType the type that the lambda term in the conclusion
	 * 					of the returned inference step will be assigned
	 * @return an {@link edu.kit.typicalc.model.step.AbsStep}
	 */
	InferenceStep visit(AbsTerm absTerm, List<TypeAssumption> typeAssumptions, Type conclusionType);

	/**
	 * Returns an {@link edu.kit.typicalc.model.step.LetStep} suiting the given let expression (lambda term)
	 * to type-infer and the type assumptions to consider.
	 * Simultaneously assembles the tree's constraint list.
	 * @param letTerm the let expression (lambda term) to build the inference step structure for,
	 * 					i.e. the lambda term in the conclusion of the returned inference step
	 * @param typeAssumptions the type assumptions to consider,
	 * 					i.e. the type assumptions in the conclusion of the returned inference step
	 * @param conclusionType the type that the lambda term in the conclusion
	 * 					of the returned inference step will be assigned
	 * @return an {@link edu.kit.typicalc.model.step.LetStep}
	 */
	InferenceStep visit(LetTerm letTerm, List<TypeAssumption> typeAssumptions, Type conclusionType);

	/**
	 * Returns an {@link edu.kit.typicalc.model.step.ConstStep} suiting the given constant
	 * to type-infer and the type assumptions to consider.
	 * Simultaneously assembles the tree's constraint list.
	 * @param constTerm the constant to build the inference step structure for,
	 * 					i.e. the lambda term in the conclusion of the returned inference step
	 * @param typeAssumptions the type assumptions to consider,
	 * 					i.e. the type assumptions in the conclusion of the returned inference step
	 * @param conclusionType the type that the lambda term in the conclusion
	 * 					of the returned inference step will be assigned
	 * @return an {@link edu.kit.typicalc.model.step.ConstStep}
	 */
	InferenceStep visit(ConstTerm constTerm, List<TypeAssumption> typeAssumptions, Type conclusionType);

	/**
	 * Returns an {@link edu.kit.typicalc.model.step.VarStep} suiting the given variable (lambda term)
	 * to type-infer and the type assumptions to consider.
	 * Simultaneously assembles the tree's constraint list.
	 * @param varTerm the variable (lambda term) to build the inference step structure for,
	 * 					i.e. the lambda term in the conclusion of the returned inference step
	 * @param typeAssumptions the type assumptions to consider,
	 * 					i.e. the type assumptions in the conclusion of the returned inference step
	 * @param conclusionType the type that the lambda term in the conclusion
	 * 					of the returned inference step will be assigned
	 * @return an {@link edu.kit.typicalc.model.step.VarStep}
	 */
	InferenceStep visit(VarTerm varTerm, List<TypeAssumption> typeAssumptions, Type conclusionType);
}
