package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Queue;

import com.vaadin.flow.i18n.I18NProvider;

import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.AbsStep;
import edu.kit.typicalc.model.step.AbsStepDefault;
import edu.kit.typicalc.model.step.AbsStepWithLet;
import edu.kit.typicalc.model.step.AppStep;
import edu.kit.typicalc.model.step.AppStepDefault;
import edu.kit.typicalc.model.step.ConstStep;
import edu.kit.typicalc.model.step.ConstStepDefault;
import edu.kit.typicalc.model.step.EmptyStep;
import edu.kit.typicalc.model.step.LetStep;
import edu.kit.typicalc.model.step.LetStepDefault;
import edu.kit.typicalc.model.step.OnlyConclusionStep;
import edu.kit.typicalc.model.step.StepVisitor;
import edu.kit.typicalc.model.step.VarStep;
import edu.kit.typicalc.model.step.VarStepDefault;
import edu.kit.typicalc.model.step.VarStepWithLet;
import edu.kit.typicalc.model.term.TermArgumentVisitor;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeArgumentVisitor;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.view.TypicalcI18NProvider;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.DOLLAR_SIGN;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.SPACE;

public class ExplanationCreator implements StepVisitor {
    private static final String KEY_PREFIX = "explanationTree.";

    private final I18NProvider provider = new TypicalcI18NProvider();
    private final TermArgumentVisitor termArgumentVisitor = new TermArgumentVisitor();
    private final TypeArgumentVisitor typeArgumentVisitor = new TypeArgumentVisitor();
    private final Locale locale;

    private final List<String> explanationTexts = new ArrayList<>();

    private static final LatexCreatorMode MODE = LatexCreatorMode.MATHJAX;
    private boolean errorOccurred; // true if one unification was not successful
    private int letCounter = 0; // count number of lets for unification indices

    // creates explanation texts for a specific language
    public ExplanationCreator(TypeInfererInterface typeInferer, Locale locale) {
        this.locale = locale;
        errorOccurred = false;

        explanationTexts.add(createInitialText(typeInferer));
        typeInferer.getFirstInferenceStep().accept(this);
        if (!errorOccurred) {
            explanationTexts.addAll(new ExplanationCreatorUnification(typeInferer, locale, provider, MODE,
                    letCounter, false, Optional.empty()).getUnificationsTexts().getLeft());
        }
    }

    public List<String> getExplanationTexts() {
        return explanationTexts;
    }

    private String getDefaultTextLatex(String textKey) {
        return provider.getTranslation(textKey, locale);
    }

    private String toLatex(String latex) {
        return SPACE + DOLLAR_SIGN + latex + DOLLAR_SIGN + SPACE;
    }

    private String createInitialText(TypeInfererInterface typeInferer) {
        String typeLatex = new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), MODE).
                getLatex();

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "initial1")).
                append(toLatex(new LatexCreatorTerm(
                        typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "initial2")).
                append(toLatex(typeLatex)).
                append(getDefaultTextLatex(KEY_PREFIX + "initial3")).
                append(toLatex(typeLatex)).
                append(getDefaultTextLatex(KEY_PREFIX + "initial4")).
                toString();
    }

    @Override
    public void visit(AbsStepDefault absD) {
        explanationTexts.add(createLatexAbsStep(absD));
        absD.getPremise().accept(this);
    }

    @Override
    public void visit(AbsStepWithLet absL) {
        explanationTexts.add(createLatexAbsStep(absL));
        absL.getPremise().accept(this);
    }

    private String createLatexAbsStep(AbsStep abs) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(abs.getConclusion().getLambdaTerm());
        Queue<Type> typeArguments = typeArgumentVisitor.getArguments(abs.getConstraint().getSecondType());

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "absStep1")).
                append(toLatex(new LatexCreatorTerm(abs.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep2")).
                append(toLatex(new LatexCreatorType(abs.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep3")).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep4")).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep5")).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep6")).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep7")).
                append(toLatex(new LatexCreatorType(abs.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep8")).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(abs.getConstraint(), MODE))).
                append(getDefaultTextLatex(KEY_PREFIX + "absStep9")).
                toString();
    }

    @Override
    public void visit(AppStepDefault appD) {
        explanationTexts.add(createLatexAppStep(appD));
        appD.getPremise1().accept(this);
        appD.getPremise2().accept(this);
    }

    private String createLatexAppStep(AppStep app) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(app.getConclusion().getLambdaTerm());
        Queue<Type> typeArguments = typeArgumentVisitor.getArguments(app.getConstraint().getSecondType());

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "appStep1")).
                append(toLatex(new LatexCreatorTerm(app.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep2")).
                append(toLatex(new LatexCreatorType(app.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep3")).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep4")).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep5")).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep6")).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep7")).
                append(toLatex(new LatexCreatorType(app.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep8")).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(app.getConstraint(), MODE))).
                append(getDefaultTextLatex(KEY_PREFIX + "appStep9")).
                toString();
    }


    @Override
    public void visit(ConstStepDefault constD) {
        explanationTexts.add(createLatexConstStep(constD));
    }

    private String createLatexConstStep(ConstStep constS) {

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "constStep1")).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep2")).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep3")).
                append(toLatex(new LatexCreatorType(constS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep4")).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep5")).
                append(toLatex(new LatexCreatorType(constS.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep6")).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(constS.getConstraint(), MODE))).
                append(getDefaultTextLatex(KEY_PREFIX + "constStep7")).
                toString();
    }

    @Override
    public void visit(VarStepDefault varD) {
        explanationTexts.add(createLatexVarStep(varD));
    }

    @Override
    public void visit(VarStepWithLet varL) {
        explanationTexts.add(createLatexVarStep(varL));
        // TODO: maybe create slightly different text
    }

    private String createLatexVarStep(VarStep varS) {
        String termLatex = new LatexCreatorTerm(varS.getConclusion().getLambdaTerm(), MODE).getLatex();

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "varStep1")).
                append(toLatex(termLatex)).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep2")).
                append(toLatex(termLatex)).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep3")).
                append(toLatex(new LatexCreatorType(varS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep4")).
                append(toLatex(termLatex)).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep5")).
                append(toLatex(new LatexCreatorType(varS.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep6")).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(varS.getConstraint(), MODE))).
                append(getDefaultTextLatex(KEY_PREFIX + "varStep7")).
                toString();
    }

    @Override
    public void visit(LetStepDefault letD) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(letD.getConclusion().getLambdaTerm());
        LambdaTerm variable = termArguments.poll();
        LambdaTerm innerTerm = termArguments.poll();
        LambdaTerm variableDefinition = termArguments.poll();
        letCounter++;
        explanationTexts.add(createLatexLetStep(letD, variable, innerTerm, variableDefinition));

        letD.getTypeInferer().getFirstInferenceStep().accept(this);
        ExplanationCreatorUnification unification =
                new ExplanationCreatorUnification(letD.getTypeInferer(), locale, provider, MODE, letCounter, true,
                        Optional.of(variable));
        explanationTexts.addAll(unification.getUnificationsTexts().getLeft());
        errorOccurred = unification.getUnificationsTexts().getRight();
        letCounter--;

        if (!errorOccurred) {
            letD.getPremise().accept(this);
        }
    }

    private String createLatexLetStep(LetStep letS, LambdaTerm variable, LambdaTerm innerTerm,
            LambdaTerm variableDefinition) {

        return new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "letStep1")).
                append(toLatex(new LatexCreatorTerm(letS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep2")).
                append(toLatex(new LatexCreatorType(letS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep3")).
                append(toLatex(new LatexCreatorTerm(variable, MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep4")).
                append(toLatex(new LatexCreatorTerm(variableDefinition, MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep5")).
                append(toLatex(new LatexCreatorTerm(innerTerm, MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep6")).
                append(toLatex(new LatexCreatorTerm(variableDefinition, MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep7")).
                append(toLatex(new LatexCreatorTerm(variable, MODE).getLatex())).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep8")).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(letS.getConstraint(), MODE))).
                append(getDefaultTextLatex(KEY_PREFIX + "letStep9")).
                toString();
    }


    @Override
    public void visit(EmptyStep empty) {
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
    }

}
