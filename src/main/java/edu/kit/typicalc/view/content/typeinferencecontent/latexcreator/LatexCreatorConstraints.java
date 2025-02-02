package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.*;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.model.term.LetTerm;
import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;
import edu.kit.typicalc.util.Result;

import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
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
    private final LatexCreatorMode mode;

    /**
     * Initializes the LatexCreatorConstraints with the right values calculates the strings
     * that will be returned in getEverything().
     *
     * @param typeInferer         the source for the generation of the LaTeX code
     * @param translationProvider translation text provider for {@link UnificationError}
     */
    protected LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                      Function<UnificationError, String> translationProvider,
                                      LatexCreatorMode mode) {
        this(typeInferer,
                new ConstraintSetIndexFactory(), new TreeNumberGenerator(),
                FIRST_PREFIX, Optional.empty(), Optional.empty(), translationProvider, mode);
    }

    private LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                    ConstraintSetIndexFactory constraintSetIndexFactory,
                                    TreeNumberGenerator numberGenerator,
                                    String prefix,
                                    Optional<VarTerm> letVariable,
                                    Optional<Map<VarTerm, TypeAbstraction>> newTypeAssumption,
                                    Function<UnificationError, String> translationProvider,
                                    LatexCreatorMode mode) {
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
            constraints.add(DOLLAR_SIGN + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + LATEX_CURLY_RIGHT + DOLLAR_SIGN);
            numberGenerator.incrementPush();
        }
        this.mode = mode;

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

        String constraintSets = constraints.get(constraints.size() - 1);
        if (typeInferer.getUnificationSteps().isPresent()) {
            generateUnification(constraintSets).forEach(step -> {
                result.add(step);
                numberGenerator.push();
            });
            typeInferer.getMGU().ifPresent(mgu -> {
                // avoid superfluous step if unification only has one step (that equals the mgu)
                if (typeInferer.getUnificationSteps().orElseThrow(IllegalStateException::new).size() > 2) {
                    result.add(generateMGU(constraintSets));
                    numberGenerator.push();
                }
                result.add(generateMGU(constraintSets) + generateFinalType());
                numberGenerator.push();
            });
        }
        if (!FIRST_PREFIX.equals(prefix)) {
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

    /**
     * Returns the latex code to display a single constraint:
     * type1 = type2
     *
     * @param constraint the constraint
     * @param mode latex creator mode (if MathJax, adds the step index to the constraint)
     * @return the latex code
     */
    public static String createSingleConstraint(Constraint constraint, LatexCreatorMode mode) {
        String firstType = new LatexCreatorType(constraint.getFirstType(), mode).getLatex();
        String secondType = new LatexCreatorType(constraint.getSecondType(), mode).getLatex();
        String latex = firstType + EQUALS + secondType;
        if (mode == LatexCreatorMode.MATHJAX && constraint.getStepIndex() != -1) {
            return "\\class{typicalc-step-" + constraint.getStepIndex()  + "}{" + latex + "}";
        }
        return latex;
    }

    private void addConstraint(InferenceStep step) {
        String currentSingleConstraint = createSingleConstraint(step.getConstraint(), mode);
        if (!prevStep.equals("")) {
            prevStep += DOLLAR_SIGN + COMMA + DOLLAR_SIGN;
        }
        prevStep += currentSingleConstraint;

        String currentConstraints = prefix + DOLLAR_SIGN + CONSTRAINT_SET + constraintSetIndex
                + EQUALS + LATEX_CURLY_LEFT + prevStep + LATEX_CURLY_RIGHT + DOLLAR_SIGN;
        constraints.add(currentConstraints);
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
                constraints.get(constraints.size() - 1) + BREAK_ROW, Optional.of(term.getVariable()),
                newTypeAss, translationProvider, mode);
        constraints.addAll(subCreator.getEverything());

        // cancels constraint creation if sub inference failed
        if (letD.getTypeInferer().getMGU().isEmpty()) {
            return;
        }
        // adds one step in which all let constraints are added to 'outer' constraint set
        String letConstraints = createLetConstraints(letD.getTypeInferer().getLetConstraints());
        prevStep = prevStep.equals("") ? letConstraints : prevStep + DOLLAR_SIGN + COMMA + DOLLAR_SIGN + letConstraints;
        letConstraints = prefix + DOLLAR_SIGN + CONSTRAINT_SET + constraintSetIndex + EQUALS
                + LATEX_CURLY_LEFT + prevStep + LATEX_CURLY_RIGHT + DOLLAR_SIGN;
        constraints.add(letConstraints);
        numberGenerator.push();

        letD.getPremise().accept(this);
    }

    private String createLetConstraints(List<Constraint> letConstraints) {
        StringBuilder result = new StringBuilder();
        letConstraints.forEach(constraint ->
            result.append(createSingleConstraint(constraint, mode))
                    .append(DOLLAR_SIGN).append(COMMA).append(DOLLAR_SIGN)
        );
        if (!letConstraints.isEmpty()) {
            // remove last comma and dollar signs
            result.deleteCharAt(result.length() - 1);
            result.deleteCharAt(result.length() - 1);
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

    /**
     * Main method of this class, creates the latex code for each step of the unification.
     *
     * @param constraintSets current constraint set (added to the latex)
     * @return latex code for each step
     */
    private List<String> generateUnification(String constraintSets) {
        List<String> steps = new ArrayList<>();

        List<UnificationStep> unificationSteps = typeInferer.getUnificationSteps()
                .orElseThrow(IllegalStateException::new);
        // store constraints of previous step to highlight changes
        List<String> previousConstraints = new ArrayList<>();

        for (int stepNum = 0; stepNum < unificationSteps.size(); stepNum++) {
            UnificationStep step = unificationSteps.get(stepNum);
            List<String> constraints2 = new ArrayList<>();
            Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();
            Optional<UnificationError> error = Optional.empty();
            if (subs.isError()) {
                // display previous step again
                // and show the error message
                error = Optional.of(subs.unwrapError());
                step = unificationSteps.get(unificationSteps.size() - 2);
                subs = step.getSubstitutions();
            }
            List<Substitution> substitutions = subs.unwrap();
            StringBuilder latex = new StringBuilder();
            latex.append(constraintSets);
            latex.append(BREAK_ROW + MATH_START);
            latex.append(ALIGN_BEGIN + SPLIT_BEGIN);
            latex.append(generateUnificationName());

            boolean markError = error.isPresent();
            List<Constraint> unificationConstraints = step.getConstraints();
            // if new constraints were created in this step, mark them
            boolean markLastTwoConstraints = (stepNum != 0)
                    && unificationSteps.get(stepNum - 1).getConstraints().size() < unificationConstraints.size();

            // render each constraint present in this step
            if (!unificationConstraints.isEmpty()) {
                latex.append(UNIFY + PAREN_LEFT + LATEX_CURLY_LEFT);
            }
            for (int i = unificationConstraints.size() - 1; i >= 0; i--) {
                latex.append(AMPERSAND);
                StringBuilder sb = new StringBuilder();
                boolean highligthingConstraint = false;
                if (markError && i == 0) {
                    sb.append(CURLY_LEFT);
                    sb.append(COLOR_RED);
                    highligthingConstraint = true;
                } else if (markLastTwoConstraints && (i < 2)) {
                    sb.append(CURLY_LEFT);
                    sb.append(COLOR_HIGHLIGHTED);
                    highligthingConstraint = true;
                }
                sb.append(new LatexCreatorType(unificationConstraints.get(i).getFirstType(), mode).getLatex());
                sb.append(EQUALS);
                sb.append(new LatexCreatorType(unificationConstraints.get(i).getSecondType(), mode).getLatex());
                if (highligthingConstraint) {
                    sb.append(CURLY_RIGHT);
                }
                constraints2.add(sb.toString());
                int invIdx = unificationConstraints.size() - 1 - i;
                // if this constraint was modified by the substitution
                if (invIdx < previousConstraints.size()) {
                    // perform the substitution "manually" and color the new type
                    Substitution s = substitutions.get(substitutions.size() - 1);
                    String original = previousConstraints.get(invIdx);
                    String originalType = new LatexCreatorType(s.getVariable(), mode).getLatex();
                    if (original.contains(originalType)) {
                        StringBuilder highlightedChange2 = new StringBuilder();
                        highlightedChange2.append(CURLY_LEFT);
                        highlightedChange2.append(COLOR_HIGHLIGHTED);
                        highlightedChange2.append(new LatexCreatorType(s.getType(), mode).getLatex());
                        highlightedChange2.append(CURLY_RIGHT);
                        latex.append(original.replace(originalType, highlightedChange2.toString()));
                    } else {
                        latex.append(sb);
                    }
                } else {
                    latex.append(sb);
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
            previousConstraints = constraints2;

            // also render the substitutions
            for (int i = substitutions.size() - 1; i >= 0; i--) {
                if (!unificationConstraints.isEmpty() || i != substitutions.size() - 1) {
                    latex.append(CIRC);
                }
                latex.append(BRACKET_LEFT);
                latex.append(AMPERSAND);
                latex.append(new LatexCreatorType(substitutions.get(i).getVariable(), mode).getLatex());
                latex.append(SUBSTITUTION_SIGN);
                latex.append(new LatexCreatorType(substitutions.get(i).getType(), mode).getLatex());
                latex.append(BRACKET_RIGHT);
                latex.append(LATEX_NEW_LINE);
            }
            latex.delete(latex.length() - LATEX_NEW_LINE.length(), latex.length());
            latex.append(SPLIT_END);
            latex.append(ALIGN_END + MATH_END);
            // finally, display the error if there is one
            error.ifPresent(unificationError -> latex.append(translationProvider.apply(unificationError)));
            steps.add(latex.toString());
        }
        return steps;
    }


    private String generateMGU(String constraintSets) {
        StringBuilder latex = new StringBuilder();
        latex.append(constraintSets);
        latex.append(BREAK_ROW);
        latex.append(MATH_START);
        latex.append(ALIGN_BEGIN + AMPERSAND + SPLIT_BEGIN);
        latex.append(generateUnificationName());
        latex.append(BRACKET_LEFT);
        typeInferer.getMGU().ifPresent(mgu -> mgu.forEach(substitution -> {
            latex.append(AMPERSAND);
            latex.append(new LatexCreatorType(substitution.getVariable(), mode).getLatex());
            latex.append(SUBSTITUTION_SIGN);
            latex.append(new LatexCreatorType(substitution.getType(), mode).getLatex());
            latex.append(COMMA);
            latex.append(LATEX_NEW_LINE);
        }));
        latex.delete(latex.length() - (COMMA + LATEX_NEW_LINE).length(), latex.length());
        latex.append(BRACKET_RIGHT);
        latex.append(SPLIT_END + ALIGN_END + MATH_END);
        return latex.toString();
    }

    private String generateFinalType() {
        StringBuilder latex = new StringBuilder();
        latex.append(DOLLAR_SIGN);
        latex.append(SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(
                new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode).getLatex());
        latex.append("" + PAREN_RIGHT + EQUALS);
        latex.append(
                new LatexCreatorType(typeInferer.getType().orElseThrow(IllegalStateException::new), mode).getLatex());
        latex.append(DOLLAR_SIGN);
        return latex.toString();
    }

    // generates the TypeAssumptions for the right sub tree of a let step
    private String generateNewTypeAssumptions(String subPrefix) {
        InferenceStep step = typeInferer.getFirstInferenceStep();
        String typeAssumptions = typeAssumptionsToLatex(step.getConclusion().getTypeAssumptions(), mode);
        if ("".equals(typeAssumptions)) {
            typeAssumptions = EMPTY_SET;
        }

        StringBuilder latex = new StringBuilder();
        latex.append(subPrefix);
        latex.append(BREAK_ROW + DOLLAR_SIGN + GAMMA + APOSTROPHE + EQUALS + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(typeAssumptions);
        latex.append("" + PAREN_RIGHT + COMMA + LATEX_SPACE);
        latex.append(new LatexCreatorTerm(letVariable.orElseThrow(IllegalStateException::new), mode).getLatex());
        latex.append("" + COLON + TYPE_ABSTRACTION + PAREN_LEFT + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(
                new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode).getLatex()
        );
        latex.append("" + PAREN_RIGHT + COMMA + SIGMA);
        latex.append(constraintSetIndex);
        latex.append(PAREN_LEFT);
        latex.append(typeAssumptions);
        latex.append("" + PAREN_RIGHT + PAREN_RIGHT + EQUALS);
        latex.append(typeAssumptionsToLatex(newTypeAssumption.orElseThrow(IllegalSelectorException::new), mode));
        latex.append(DOLLAR_SIGN);
        return latex.toString();
    }
}
