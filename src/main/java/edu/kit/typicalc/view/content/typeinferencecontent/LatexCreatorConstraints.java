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
    private final String constraintSetIndex;
    private final String prefix;
    private String prevStep;

    protected LatexCreatorConstraints(TypeInfererInterface typeInferer) {
        this(typeInferer, new ConstraintSetIndexFactory(), FIRST_PREFIX);
    }

    protected LatexCreatorConstraints(TypeInfererInterface typeInferer,
                                      ConstraintSetIndexFactory constraintSetIndexFactory,
                                      String prefix) {
        this.prefix = prefix;
        this.prevStep = "";
        this.constraintSetIndexFactory = constraintSetIndexFactory;
        this.constraintSetIndex = constraintSetIndexFactory.nextConstraintSetIndex();
        this.typeInferer = typeInferer;
        constraints = new ArrayList<>();
        if (FIRST_PREFIX.equals(prefix)) {
            constraints.add(DOLLAR_SIGN + CONSTRAINT_SET + EQUALS + LATEX_CURLY_LEFT + LATEX_CURLY_RIGHT + DOLLAR_SIGN);
        }

        typeInferer.getFirstInferenceStep().accept(this);
    }

    protected List<String> getEverything() {
        List<String> result = new ArrayList<>(constraints);
        result.addAll(generateUnification());
        typeInferer.getMGU().ifPresent(mgu -> result.add(generateMGU()));
        // todo return final type
        return result;
    }

    protected void addConstraint(InferenceStep step) {
        String firstType = new LatexCreatorType(step.getConstraint().getFirstType()).getLatex();
        String secondType = new LatexCreatorType(step.getConstraint().getSecondType()).getLatex();
        String currentConstraint = firstType + SPACE + EQUALS + SPACE + secondType;
        prevStep = prevStep.equals("") ? currentConstraint : prevStep + COMMA + currentConstraint;
        currentConstraint = prefix + DOLLAR_SIGN + CONSTRAINT_SET + constraintSetIndex + EQUALS + LATEX_CURLY_LEFT
                + prevStep + LATEX_CURLY_RIGHT + DOLLAR_SIGN;
        constraints.add(currentConstraint);
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
                constraintSetIndexFactory, constraints.get(constraints.size() - 1));
        constraints.addAll(subCreator.getEverything());
        letD.getPremise().accept(this);
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
            latex.append(DOLLAR_SIGN);
            latex.append(ALIGN_BEGIN);
            List<Substitution> substitutions = subs.unwrap();
            for (Substitution s : substitutions) {
                latex.append(new LatexCreatorType(s.getVariable()).getLatex());
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
                latex.append(EQUALS);
                latex.append(new LatexCreatorType(c.getSecondType()).getLatex());
                latex.append(LATEX_NEW_LINE);
            }
            latex.append(ALIGN_END);
            latex.append(DOLLAR_SIGN);
            steps.add(latex.toString());
        }
        return steps;
    }


    private String generateMGU() {
        StringBuilder mguLatex = new StringBuilder();
        mguLatex.append(DOLLAR_SIGN);
        mguLatex.append(ALIGN_BEGIN);
        mguLatex.append(BRACKET_LEFT);
        typeInferer.getMGU().ifPresent(mgu -> mgu.forEach(substitution -> {
            mguLatex.append(new LatexCreatorType(substitution.getVariable()).getLatex());
            mguLatex.append(SUBSTITUTION_SIGN);
            mguLatex.append(new LatexCreatorType(substitution.getType()).getLatex());
            mguLatex.append(COMMA);
            mguLatex.append(LATEX_NEW_LINE);
            mguLatex.append(NEW_LINE);
        }));
        mguLatex.delete(mguLatex.length() - 3, mguLatex.length());
        mguLatex.append(BRACKET_RIGHT);
        mguLatex.append(ALIGN_END);
        mguLatex.append(DOLLAR_SIGN);
        return mguLatex.toString();
    }
}
