package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.*;
import edu.kit.typicalc.model.step.*;
import edu.kit.typicalc.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo javadoc
import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

public class LatexCreatorConstraints implements StepVisitor {

    private static final String FIRST_PREFIX = "";

    private final List<String> constraints;
    private final TypeInfererInterface typeInferer;
    private final ConstraintSetIndexFactory constraintSetIndexFactory;
    private final TreeNumberGenerator numberGenerator;
    private final String constraintSetIndex;
    private final String prefix;
    private String prevStep;

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
            constraints.add(CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + LATEX_CURLY_RIGHT);
            numberGenerator.incrementPush();
        }

        typeInferer.getFirstInferenceStep().accept(this);
    }

    protected List<String> getEverything() {
        List<String> result = new ArrayList<>(constraints);
        if (typeInferer.getUnificationSteps().isPresent()) {
            generateUnification().forEach(step -> {
                result.add(step);
                numberGenerator.push();
            });
            typeInferer.getMGU().ifPresent(mgu -> {
                result.add(generateMGU());
                numberGenerator.push();
                result.add(generateMGU() + LATEX_NEW_LINE + new LatexCreatorType(typeInferer.getType().get()).getLatex());
                numberGenerator.push();
            });
            // todo add some helpful text for the user
        }
        if (FIRST_PREFIX.equals(prefix)) {
            result.replaceAll(content -> ALIGN_BEGIN + content + ALIGN_END);
        }
        return result;
    }

    protected List<Integer> getTreeNumbers() {
        return numberGenerator.getNumbers();
    }

    private String createSingleConstraint(Constraint constraint) {
        String firstType = new LatexCreatorType(constraint.getFirstType()).getLatex();
        String secondType = new LatexCreatorType(constraint.getSecondType()).getLatex();
        return firstType + SPACE + EQUALS + SPACE + secondType;
    }

    private void addConstraint(InferenceStep step) {
        String currentConstraint = createSingleConstraint(step.getConstraint());
        prevStep = prevStep.equals("") ? currentConstraint : prevStep + COMMA + currentConstraint;
        currentConstraint = prefix + CONSTRAINT_SET + constraintSetIndex + EQUALS + LATEX_CURLY_LEFT
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
        letConstraints = prefix + CONSTRAINT_SET + constraintSetIndex + EQUALS + LATEX_CURLY_LEFT
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
        // empty steps dont have constraints associated with them
    }

    private List<String> generateUnification() {
        List<String> steps = new ArrayList<>();
        // TODO: check if unification is present
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
            latex.append(prefix);
            List<Substitution> substitutions = subs.unwrap();
            for (Substitution s : substitutions) {
                latex.append(new LatexCreatorType(s.getVariable()).getLatex());
                latex.append(AMPERSAND);
                latex.append(SUBSTITUTION_SIGN);
                latex.append(new LatexCreatorType(s.getType()).getLatex());
                latex.append(LATEX_NEW_LINE);
            }
            error.ifPresent(latex::append); // TODO: translation
            if (error.isPresent()) {
                latex.append(LATEX_NEW_LINE);
            }
            List<Constraint> unificationConstraints = step.getConstraints();
            for (Constraint c : unificationConstraints) {
                latex.append(new LatexCreatorType(c.getFirstType()).getLatex());
                latex.append(AMPERSAND);
                latex.append(EQUALS);
                latex.append(new LatexCreatorType(c.getSecondType()).getLatex());
                latex.append(LATEX_NEW_LINE);
            }
            steps.add(latex.toString());
        }
        return steps;
    }


    private String generateMGU() {
        StringBuilder latex = new StringBuilder();
        latex.append(prefix);
        latex.append(BRACKET_LEFT);
        typeInferer.getMGU().ifPresent(mgu -> mgu.forEach(substitution -> {
            latex.append(new LatexCreatorType(substitution.getVariable()).getLatex());
            latex.append(AMPERSAND);
            latex.append(SUBSTITUTION_SIGN);
            latex.append(new LatexCreatorType(substitution.getType()).getLatex());
            latex.append(COMMA);
            latex.append(LATEX_NEW_LINE);
            latex.append(NEW_LINE);
        }));
        latex.delete(latex.length() - (COMMA + LATEX_NEW_LINE + NEW_LINE).length(), latex.length());
        latex.append(BRACKET_RIGHT);
        return latex.toString();
    }
}
