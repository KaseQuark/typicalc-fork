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

/**
 * Generates LaTeX code for explanatory texts in a specific language for every step of the type inference algorithm.
 * To do this, text templates from the resource bundle are filled out with specific content for every input.
 */
public class ExplanationCreator implements StepVisitor {
    private static final String KEY_PREFIX = "explanationTree.";

    private final I18NProvider provider = new TypicalcI18NProvider();
    private final TermArgumentVisitor termArgumentVisitor = new TermArgumentVisitor();
    private final TypeArgumentVisitor typeArgumentVisitor = new TypeArgumentVisitor();
    private final Locale locale;

    private final List<String> explanationTexts = new ArrayList<>();

    private static final LatexCreatorMode MODE = LatexCreatorMode.NORMAL; // no highlighting here
    private boolean errorOccurred; // true if one unification was not successful
    private int letCounter = 0; // count number of lets for unification indices

    /**
     * Generate the LaTeX code from the type inferer for the language of the provided locale.
     * 
     * @param typeInferer the TypeInfererInterface to create the LaTeX-code from
     * @param locale      the language of the explanatory texts
     */
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

    /**
     * Returns a list of strings with an entry for every step of the algorithm.
     * 
     * @return list of strings containing the explanatory texts
     */
    public List<String> getExplanationTexts() {
        return explanationTexts;
    }

    private String toLatex(String latex) {
        return SPACE + DOLLAR_SIGN + latex + DOLLAR_SIGN + SPACE;
    }

    private String createInitialText(TypeInfererInterface typeInferer) {
        String typeLatex = new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), MODE).
                getLatex();

        var arg1 = toLatex(new LatexCreatorTerm(
                typeInferer.getFirstInferenceStep().getConclusion().getLambdaTerm(), MODE).getLatex());
        var arg2 = toLatex(typeLatex);

        return provider.getTranslation(KEY_PREFIX + "initial", locale, arg1, arg2);
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

        var arg1 = toLatex(new LatexCreatorTerm(abs.getConclusion().getLambdaTerm(), MODE).getLatex());
        var arg2 = toLatex(new LatexCreatorType(abs.getConclusion().getType(), MODE).getLatex());
        var arg3 = toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex());
        var arg4 = toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex());
        var arg5 = toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex());
        var arg6 = toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex());
        var arg7 = toLatex(new LatexCreatorType(abs.getConstraint().getSecondType(), MODE).getLatex());
        var arg8 = toLatex(LatexCreatorConstraints.createSingleConstraint(abs.getConstraint(), MODE));

        return provider.getTranslation(KEY_PREFIX + "absStep", locale,
                arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
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

        var arg1 = toLatex(new LatexCreatorTerm(app.getConclusion().getLambdaTerm(), MODE).getLatex());
        var arg2 = toLatex(new LatexCreatorType(app.getConclusion().getType(), MODE).getLatex());
        var arg3 = toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex());
        var arg4 = toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex());
        var arg5 = toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex());
        var arg6 = toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex());
        var arg7 = toLatex(new LatexCreatorType(app.getConstraint().getSecondType(), MODE).getLatex());
        var arg8 = toLatex(LatexCreatorConstraints.createSingleConstraint(app.getConstraint(), MODE));

        return provider.getTranslation(KEY_PREFIX + "appStep", locale,
                arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    @Override
    public void visit(ConstStepDefault constD) {
        explanationTexts.add(createLatexConstStep(constD));
    }

    private String createLatexConstStep(ConstStep constS) {

        var arg1 = toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex());
        var arg2 = toLatex(new LatexCreatorType(constS.getConclusion().getType(), MODE).getLatex());
        var arg3 = toLatex(new LatexCreatorType(constS.getConstraint().getSecondType(), MODE).getLatex());
        var arg4 = toLatex(LatexCreatorConstraints.createSingleConstraint(constS.getConstraint(), MODE));

        return provider.getTranslation(KEY_PREFIX + "constStep", locale, arg1, arg2, arg3, arg4);
    }

    @Override
    public void visit(VarStepDefault varD) {
        explanationTexts.add(createLatexVarStep(varD));
    }

    @Override
    public void visit(VarStepWithLet varL) {
        explanationTexts.add(createLatexVarStep(varL));
    }

    private String createLatexVarStep(VarStep varS) {
        String termLatex = new LatexCreatorTerm(varS.getConclusion().getLambdaTerm(), MODE).getLatex();

        var arg1 = toLatex(termLatex);
        var arg2 = toLatex(new LatexCreatorType(varS.getConclusion().getType(), MODE).getLatex());
        var arg3 = toLatex(new LatexCreatorType(varS.getConstraint().getSecondType(), MODE).getLatex());
        var arg4 = toLatex(LatexCreatorConstraints.createSingleConstraint(varS.getConstraint(), MODE));

        return provider.getTranslation(KEY_PREFIX + "varStep", locale, arg1, arg2, arg3, arg4);
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
        // skip creation of unification texts if nested let produced an error
        if (!errorOccurred) {
            ExplanationCreatorUnification unification =
                    new ExplanationCreatorUnification(letD.getTypeInferer(), locale, provider, MODE, letCounter, true,
                            Optional.of(variable));
            explanationTexts.addAll(unification.getUnificationsTexts().getLeft());
            errorOccurred = unification.getUnificationsTexts().getRight();
            letCounter--;
        }
        
        if (!errorOccurred) {
            letD.getPremise().accept(this);
        }
    }

    private String createLatexLetStep(LetStep letS, LambdaTerm variable, LambdaTerm innerTerm,
                                      LambdaTerm variableDefinition) {

        var arg1 = toLatex(new LatexCreatorTerm(letS.getConclusion().getLambdaTerm(), MODE).getLatex());
        var arg2 = toLatex(new LatexCreatorType(letS.getConclusion().getType(), MODE).getLatex());
        var arg3 = toLatex(new LatexCreatorTerm(variable, MODE).getLatex());
        var arg4 = toLatex(new LatexCreatorTerm(variableDefinition, MODE).getLatex());
        var arg5 = toLatex(new LatexCreatorTerm(innerTerm, MODE).getLatex());
        var arg6 = toLatex(LatexCreatorConstraints.createSingleConstraint(letS.getConstraint(), MODE));

        return provider.getTranslation(KEY_PREFIX + "letStep", locale,
                arg1, arg2, arg3, arg4, arg5, arg6);
    }


    @Override
    public void visit(EmptyStep empty) {
        // no implementation since EmptyStep is not visible for the user
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        // no implementation since OnlyConclusionStep is not visible for the user
    }

}
