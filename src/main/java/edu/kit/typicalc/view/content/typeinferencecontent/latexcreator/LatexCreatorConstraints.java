package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.*;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private String prevStep;

    /**
     * Initializes the LatexCreatorConstraints with the right values calculates the strings
     * that will be returned in getEverything().
     *
     * @param typeInferer the source for the generation of the LaTeX code
     */
    protected LatexCreatorConstraints(TypeInfererInterface typeInferer) {
        this(typeInferer, new ConstraintSetIndexFactory(), new TreeNumberGenerator(), FIRST_PREFIX);
    }

    private LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                      ConstraintSetIndexFactory constraintSetIndexFactory,
                                      TreeNumberGenerator numberGenerator,
                                      String prefix) {
        this.prefix = prefix;
        this.prevStep = "";
        this.constraintSetIndexFactory = constraintSetIndexFactory;
        this.numberGenerator = numberGenerator;
        this.constraintSetIndex = constraintSetIndexFactory.nextConstraintSetIndex();
        this.typeInferer = typeInferer;
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
            // todo add some helpful text for the user
        }
        if (FIRST_PREFIX.equals(prefix)) {
            result.replaceAll(content -> ALIGN_BEGIN + content + ALIGN_END);
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
        LatexCreatorConstraints subCreator = new LatexCreatorConstraints(letD.getTypeInferer(),
                constraintSetIndexFactory, numberGenerator,
                constraints.get(constraints.size() - 1) + LATEX_NEW_LINE + NEW_LINE);
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
        for (UnificationStep step : unificationSteps) {
            Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();
            Optional<UnificationError> error = Optional.empty();
            if (subs.isError()) {
                error = Optional.of(subs.unwrapError());
                step = unificationSteps.get(unificationSteps.size() - 2);
                subs = step.getSubstitutions(); // TODO: what if first step fails?
            }
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
                if (markError && i == 0) {
                    latex.append(COLOR_RED);
                    latex.append(CURLY_LEFT);
                }
                latex.append(new LatexCreatorType(unificationConstraints.get(i).getFirstType()).getLatex());
                latex.append(EQUALS);
                latex.append(new LatexCreatorType(unificationConstraints.get(i).getSecondType()).getLatex());
                if (markError && i == 0) {
                    latex.append(CURLY_RIGHT);
                }
                if (i > 0) {
                    latex.append(COMMA);
                    latex.append(LATEX_NEW_LINE);
                }
            }
            if (!unificationConstraints.isEmpty()) {
                // todo somehow this gets colored red too when an error occurs
                latex.append(PAREN_RIGHT + LATEX_CURLY_RIGHT + LATEX_NEW_LINE);
            }

            List<Substitution> substitutions = subs.unwrap();
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
            latex.append(SPLIT_END);
            if (error.isPresent()) {
                latex.append(LATEX_NEW_LINE + AMPERSAND);
            }
            error.ifPresent(latex::append); // TODO: translation
            steps.add(latex.toString());
        }
        return steps;
    }


    private String generateMGU(String constraintSets) {
        StringBuilder latex = new StringBuilder();
        latex.append(constraintSets);
        latex.append(AMPERSAND + SPLIT_BEGIN);
        latex.append(generateUnificationName());
        latex.append(SPACE);
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
}
