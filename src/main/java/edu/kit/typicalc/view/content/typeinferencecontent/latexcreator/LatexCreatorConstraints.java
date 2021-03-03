package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.*;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.util.Result;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.typeAssumptionsToLatex;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;

/**
 * Generates the LaTeX code needed for the MathjaxUnification element, namely the constraints, the unification,
 * the MGU and the final type.
 */
public class LatexCreatorConstraints implements StepVisitor {

    private static final String FIRST_PREFIX = "";

    private final List<String> constraints;
    private final TypeInfererInterface typeInferer;
    private final ConstraintSetIndexFactory constraintSetIndexFactory;
    private final TreeNumberGenerator numberGenerator;
    private final String constraintSetIndex;
    private final String prefix;
    private final Optional<VarTerm> letVariable;
    private final Optional<Map<VarTerm, TypeAbstraction>> newTypeAssumption;
    private String prevStep;
    private final Function<UnificationError, String> translationProvider;

    /**
     * Initializes the LatexCreatorConstraints with the right values calculates the strings
     * that will be returned in getEverything().
     *
     * @param typeInferer         the source for the generation of the LaTeX code
     * @param translationProvider translation text provider for {@link UnificationError}
     */
    protected LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                      Function<UnificationError, String> translationProvider) {
        this(typeInferer,
                new ConstraintSetIndexFactory(), new TreeNumberGenerator(),
                FIRST_PREFIX, Optional.empty(), Optional.empty(), translationProvider);
    }

    private LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                    ConstraintSetIndexFactory constraintSetIndexFactory,
                                    TreeNumberGenerator numberGenerator,
                                    String prefix,
                                    Optional<VarTerm> letVariable,
                                    Optional<Map<VarTerm, TypeAbstraction>> newTypeAssumption,
                                    Function<UnificationError, String> translationProvider) {
        this.prefix = prefix;
        this.letVariable = letVariable;
        this.newTypeAssumption = newTypeAssumption;
        this.prevStep = "";
        this.constraintSetIndexFactory = constraintSetIndexFactory;
        this.numberGenerator = numberGenerator;
        this.constraintSetIndex = constraintSetIndexFactory.nextConstraintSetIndex();
        this.typeInferer = typeInferer;
        this.translationProvider = translationProvider;
        constraints = new ArrayList<>();
        if (FIRST_PREFIX.equals(prefix)) {
            constraints.add(AMPERSAND + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + LATEX_CURLY_RIGHT);
            numberGenerator.incrementPush();
        }

        typeInferer.getFirstInferenceStep().accept(this);
    }

    /**
     * Returns a list of strings, each of the strings represents one step in the collecting of constraints,
     * the unification, the mgu and the final type.
     *
     * @return steps for the MathjaxUnification element to display
     */
    protected List<String> getEverything() {
        List<String> result = new ArrayList<>(constraints);

        String constraintSets = constraints.get(constraints.size() - 1) + LATEX_NEW_LINE;
        if (typeInferer.getUnificationSteps().isPresent()) {
            generateUnification(constraintSets).forEach(step -> {
                result.add(step);
                numberGenerator.push();
            });
            typeInferer.getMGU().ifPresent(mgu -> {
                result.add(generateMGU(constraintSets));
                numberGenerator.push();
                result.add(generateMGU(constraintSets) + LATEX_NEW_LINE + generateFinalType());
                numberGenerator.push();
            });
        }
        if (FIRST_PREFIX.equals(prefix)) {
            result.replaceAll(content -> ALIGN_BEGIN + content + ALIGN_END);
        } else {
            // add the new type assumptions only during a let sub inference where
            // the unification was successful
            typeInferer.getMGU().ifPresent(mgu -> {
                result.add(generateNewTypeAssumptions(result.get(result.size() - 1)));
                numberGenerator.push();
            });
        }
        return result;
    }

    /**
     * Returns a list of numbers that describe in which step the tree should be, fitting to the current step of
     * the unification. The list is always the same length as the list provided by getEverything().
     * The index stands for the step the unification is in, the Integer at that index stands for the step that the
     * tree should be in (at that point).
     *
     * @return a list describing in which step the tree should be
     */
    protected List<Integer> getTreeNumbers() {
        return numberGenerator.getNumbers();
    }

    private String createSingleConstraint(Constraint constraint) {
        String firstType = new LatexCreatorType(constraint.getFirstType()).getLatex();
        String secondType = new LatexCreatorType(constraint.getSecondType()).getLatex();
        return firstType + EQUALS + secondType;
    }

    private void addConstraint(InferenceStep step) {
        String currentConstraint = createSingleConstraint(step.getConstraint());
        prevStep = prevStep.equals("") ? currentConstraint : prevStep + COMMA + currentConstraint;
        currentConstraint = prefix + AMPERSAND + CONSTRAINT_SET + constraintSetIndex + EQUALS + LATEX_CURLY_LEFT
                + prevStep + LATEX_CURLY_RIGHT;
        constraints.add(currentConstraint);
        numberGenerator.incrementPush();
    }

    @Override
    public void visit(AbsStepDefault absD) {
        addConstraint(absD);
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        addConstraint(absL);
        absL.getPremise().accept(this);
    }

    @Override
    public void visit(AppStepDefault appD) {
        addConstraint(appD);
        appD.getPremise1().accept(this);
        appD.getPremise2().accept(this);
    }

    @Override
    public void visit(ConstStepDefault constD) {
        addConstraint(constD);
    }

    @Override
    public void visit(VarStepDefault varD) {
        addConstraint(varD);
    }

    @Override
    public void visit(VarStepWithLet varL) {
        addConstraint(varL);
    }

    @Override
    public void visit(LetStepDefault letD) {
        addConstraint(letD);
        LetTerm term = (LetTerm) letD.getConclusion().getLambdaTerm();
        Optional<Map<VarTerm, TypeAbstraction>> newTypeAss = Optional.empty();
        if (letD.getTypeInferer().getMGU().isPresent()) {
            newTypeAss = Optional.of(letD.getPremise().getConclusion().getTypeAssumptions());
        }
        LatexCreatorConstraints subCreator = new LatexCreatorConstraints(letD.getTypeInferer(),
                constraintSetIndexFactory, numberGenerator,
                constraints.get(constraints.size() - 1) + LATEX_NEW_LINE + NEW_LINE, Optional.of(term.getVariable()),
                newTypeAss, translationProvider);
        constraints.addAll(subCreator.getEverything());

        // cancels constraint creation if sub inference failed
        if (letD.getTypeInferer().getMGU().isEmpty()) {
            return;
        }
        // adds one step in which all let constraints are added to 'outer' constraint set
        String letConstraints = createLetConstraints(letD.getTypeInferer().getLetConstraints());
        prevStep = prevStep.equals("") ? letConstraints : prevStep + COMMA + letConstraints;
        letConstraints = prefix + AMPERSAND + CONSTRAINT_SET + constraintSetIndex + EQUALS + LATEX_CURLY_LEFT
                + prevStep + LATEX_CURLY_RIGHT;
        constraints.add(letConstraints);
        numberGenerator.push();

        letD.getPremise().accept(this);
    }

    private String createLetConstraints(List<Constraint> letConstraints) {
        StringBuilder result = new StringBuilder();
        letConstraints.forEach(constraint -> result.append(createSingleConstraint(constraint)).append(COMMA));
        if (!letConstraints.isEmpty()) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    @Override
    public void visit(EmptyStep empty) {
        // empty steps don't have constraints associated with them
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        // steps containing only a conclusion don't have constraints associated with them
    }

    private StringBuilder generateUnificationName() {
        StringBuilder latex = new StringBuilder();
        latex.append(SIGMA);
        latex.append(constraintSetIndex);
        latex.append("" + COLON + EQUALS + MGU + PAREN_LEFT + CONSTRAINT_SET);
        latex.append(constraintSetIndex);
        latex.append("" + PAREN_RIGHT + EQUALS);
        return latex;
    }

    private List<String> generateUnification(String constraintSets) {
        List<String> steps = new ArrayList<>();

        List<UnificationStep> unificationSteps = typeInferer.getUnificationSteps()
                .orElseThrow(IllegalStateException::new);
        // store constraints of previous step to highlight changes
        String[] previousConstraints = new String[0];
        for (UnificationStep step : unificationSteps) {
            List<String> constraints2 = new ArrayList<>();
            Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();
            Optional<UnificationError> error = Optional.empty();
            if (subs.isError()) {
                error = Optional.of(subs.unwrapError());
                step = unificationSteps.get(unificationSteps.size() - 2);
                subs = step.getSubstitutions(); // TODO: what if first step fails?
            }
            List<Substitution> substitutions = subs.unwrap();
            StringBuilder latex = new StringBuilder();
            latex.append(constraintSets);
            latex.append(AMPERSAND + SPLIT_BEGIN);
            latex.append(generateUnificationName());

            boolean markError = error.isPresent();
            List<Constraint> unificationConstraints = step.getConstraints();
            if (!unificationConstraints.isEmpty()) {
                latex.append(UNIFY + PAREN_LEFT + LATEX_CURLY_LEFT);
            }
            for (int i = unificationConstraints.size() - 1; i >= 0; i--) {
                latex.append(AMPERSAND);
                StringBuilder sb = new StringBuilder();
                if (markError && i == 0) {
                    sb.append(CURLY_LEFT);
                    sb.append(COLOR_RED);
                }
                sb.append(new LatexCreatorType(unificationConstraints.get(i).getFirstType()).getLatex());
                sb.append(EQUALS);
                sb.append(new LatexCreatorType(unificationConstraints.get(i).getSecondType()).getLatex());
                if (markError && i == 0) {
                    sb.append(CURLY_RIGHT);
                }
                constraints2.add(sb.toString());
                int invIdx = unificationConstraints.size() - 1 - i;
                // if this constraint was modified by the substitution
                if (invIdx < previousConstraints.length) {
                    // perform the substitution "manually" and color the new type
                    Substitution s = substitutions.get(substitutions.size() - 1);
                    String original = previousConstraints[invIdx];
                    StringBuilder highlightedChange2 = new StringBuilder();
                    highlightedChange2.append(CURLY_LEFT);
                    highlightedChange2.append(COLOR_HIGHLIGHT);
                    highlightedChange2.append(new LatexCreatorType(s.getType()).getLatex());
                    highlightedChange2.append(CURLY_RIGHT);
                    latex.append(original.replace(new LatexCreatorType(s.getVariable()).getLatex(),
                            highlightedChange2.toString()));
                } else {
                    latex.append(sb.toString());
                }
                if (i > 0) {
                    latex.append(COMMA);
                    latex.append(LATEX_NEW_LINE);
                }
            }
            if (!unificationConstraints.isEmpty()) {
                constraints2.remove(constraints2.size() - 1);
                latex.append(LATEX_CURLY_RIGHT + PAREN_RIGHT + LATEX_NEW_LINE);
            }
            previousConstraints = constraints2.toArray(new String[0]);

            for (int i = substitutions.size() - 1; i >= 0; i--) {
                if (!unificationConstraints.isEmpty() || i != substitutions.size() - 1) {
                    latex.append(CIRC);
                }
                latex.append(BRACKET_LEFT);
                latex.append(AMPERSAND);
                latex.append(new LatexCreatorType(substitutions.get(i).getVariable()).getLatex());
                latex.append(SUBSTITUTION_SIGN);
                latex.append(new LatexCreatorType(substitutions.get(i).getType()).getLatex());
                latex.append(BRACKET_RIGHT);
                latex.append(LATEX_NEW_LINE);
            }
            latex.delete(latex.length() - LATEX_NEW_LINE.length(), latex.length());
            latex.append(SPLIT_END);
            if (error.isPresent()) {
                latex.append(LATEX_NEW_LINE + LATEX_NEW_LINE + AMPERSAND + TEXT + CURLY_LEFT);
                latex.append(translationProvider.apply(error.get()));
                latex.append(CURLY_RIGHT);
            }
            steps.add(latex.toString());
        }
        return steps;
    }


    private String generateMGU(String constraintSets) {
        StringBuilder latex = new StringBuilder();
        latex.append(constraintSets);
        latex.append(AMPERSAND + SPLIT_BEGIN);
        latex.append(generateUnificationName());
        latex.append(BRACKET_LEFT);
        typeInferer.getMGU().ifPresent(mgu -> mgu.forEach(substitution -> {
            latex.append(AMPERSAND);
            latex.append(new LatexCreatorType(substitution.getVariable()).getLatex());
            latex.append(SUBSTITUTION_SIGN);
            latex.append(new LatexCreatorType(substitution.getType()).getLatex());
            latex.append(COMMA);
            latex.append(LATEX_NEW_LINE);
        }));
        latex.delete(latex.length() - (COMMA + LATEX_NEW_LINE).length(), latex.length());
        latex.append(BRACKET_RIGHT);
        latex.append(SPLIT_END);
        return latex.toString();
    }

    private String generateFinalType() {
        StringBuilder latex = new StringBuilder();
        latex.append(AMPERSAND);
        latex.append(SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType()).getLatex());
        latex.append("" + PAREN_RIGHT + EQUALS);
        latex.append(new LatexCreatorType(typeInferer.getType().get()).getLatex());
        return latex.toString();
    }

    // generates the TypeAssumptions for the right sub tree of a let step
    private String generateNewTypeAssumptions(String subPrefix) {
        InferenceStep step = typeInferer.getFirstInferenceStep();
        String typeAssumptions = typeAssumptionsToLatex(step.getConclusion().getTypeAssumptions());
        if ("".equals(typeAssumptions)) {
            typeAssumptions = EMPTY_SET;
        }

        StringBuilder latex = new StringBuilder();
        latex.append(subPrefix);
        latex.append(LATEX_NEW_LINE + AMPERSAND + GAMMA + APOSTROPHE + EQUALS + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(typeAssumptions);
        latex.append("" + PAREN_RIGHT + COMMA + LATEX_SPACE);
        latex.append(new LatexCreatorTerm(letVariable.orElseThrow(IllegalStateException::new)).getLatex());
        latex.append("" + COLON + TYPE_ABSTRACTION + PAREN_LEFT + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType()).getLatex());
        latex.append("" + PAREN_RIGHT + COMMA + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(typeAssumptions);
        latex.append("" + PAREN_RIGHT + PAREN_RIGHT + EQUALS);
        latex.append(typeAssumptionsToLatex(newTypeAssumption.orElseThrow(IllegalSelectorException::new)));
        return latex.toString();
    }
}
