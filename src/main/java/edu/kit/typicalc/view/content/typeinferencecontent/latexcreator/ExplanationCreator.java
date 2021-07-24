package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

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

public class ExplanationCreator implements StepVisitor {
    private final TypicalcI18NProvider provider = new TypicalcI18NProvider();
    private final TermArgumentVisitor termArgumentVisitor = new TermArgumentVisitor();
    private final TypeArgumentVisitor typeArgumentVisitor = new TypeArgumentVisitor();
    
    private final Map<Locale, List<String>> explanationTexts = new HashMap<>();
    
    private static final LatexCreatorMode MODE = LatexCreatorMode.MATHJAX;

    public ExplanationCreator() {
        provider.getProvidedLocales().forEach(l -> explanationTexts.put(l, new ArrayList<String>()));
    }
    
    public Map<Locale, List<String>> getExplanationTexts() {
        return explanationTexts;
    }
    
    private String getDefaultTextLatex(String textKey, Locale locale) {
        return new StringBuilder(LatexCreatorConstants.TEXT + LatexCreatorConstants.CURLY_LEFT).
                append(provider.getTranslation(textKey, locale)).
                append(LatexCreatorConstants.CURLY_RIGHT)
                .toString();
    }
    
    private String toLatex(String latex) {
        return LatexCreatorConstants.DOLLAR_SIGN + latex + LatexCreatorConstants.DOLLAR_SIGN;
    }
    
    @Override
    public void visit(AbsStepDefault absD) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexAbsStep(absD, l));
            explanationTexts.put(l, texts);
        });
        absD.getPremise().accept(this);
    }
    
    @Override
    public void visit(AbsStepWithLet absL) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexAbsStep(absL, l));
            explanationTexts.put(l, texts);
        });
        absL.getPremise().accept(this);
    }
    
    private String createLatexAbsStep(AbsStep abs, Locale locale) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(abs.getConclusion().getLambdaTerm());
        Queue<Type> typeArguments = typeArgumentVisitor.getArguments(abs.getConstraint().getSecondType());
        
        return new StringBuilder(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(abs.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(abs.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(abs.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(abs.getConstraint(), MODE))).
                append(getDefaultTextLatex("key", locale)).
                toString();
    }

    @Override
    public void visit(AppStepDefault appD) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexAppStep(appD, l));
            explanationTexts.put(l, texts);
        });
        appD.getPremise1().accept(this);
        appD.getPremise2().accept(this);
    }
    
    private String createLatexAppStep(AppStep app, Locale locale) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(app.getConclusion().getLambdaTerm());
        Queue<Type> typeArguments = typeArgumentVisitor.getArguments(app.getConstraint().getSecondType());
        
        return new StringBuilder(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(app.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(app.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(typeArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(app.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(app.getConstraint(), MODE))).
                append(getDefaultTextLatex("key", locale)).
                toString();
    }
    

    @Override
    public void visit(ConstStepDefault constD) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexConstStep(constD, l));
            explanationTexts.put(l, texts);
        });
    }
    
    private String createLatexConstStep(ConstStep constS, Locale locale) {
        
        return new StringBuilder(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(constS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(constS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append("$" + constS.getConclusion().getType() + "$").
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(constS.getConstraint(), MODE))).
                append(getDefaultTextLatex("key", locale)).
                toString();
    }

    @Override
    public void visit(VarStepDefault varD) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexVarStep(varD, l));
            explanationTexts.put(l, texts);
        });
    }

    @Override
    public void visit(VarStepWithLet varL) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexVarStep(varL, l));
            explanationTexts.put(l, texts);
        });
        // TODO: maybe create slightly different text
    }
    
    private String createLatexVarStep(VarStep varS, Locale locale) {
        
        return new StringBuilder(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(varS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(varS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(varS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(varS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(varS.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(varS.getConstraint(), MODE))).
                append(getDefaultTextLatex("key", locale)).
                toString();
    }

    @Override
    public void visit(LetStepDefault letD) {
        provider.getProvidedLocales().forEach(l -> {
            List<String> texts = explanationTexts.get(l);
            texts.add(createLatexLetStep(letD, l));
            explanationTexts.put(l, texts);
        });
        
        letD.getPremise().accept(this);
    }
    
    private String createLatexLetStep(LetStep letS, Locale locale) {
        Queue<LambdaTerm> termArguments = termArgumentVisitor.getArguments(letS.getConclusion().getLambdaTerm());
        LambdaTerm variable = termArguments.poll();
        LambdaTerm innerTerm = termArguments.poll();
        
        return new StringBuilder(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(letS.getConclusion().getLambdaTerm(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(letS.getConclusion().getType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(variable, MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(termArguments.poll(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(innerTerm, MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(variable, MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorTerm(innerTerm, MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(new LatexCreatorType(letS.getConstraint().getSecondType(), MODE).getLatex())).
                append(getDefaultTextLatex("key", locale)).
                append(toLatex(LatexCreatorConstraints.createSingleConstraint(letS.getConstraint(), MODE))).
                append(getDefaultTextLatex("key", locale)).
                toString();
    }
    

    @Override
    public void visit(EmptyStep empty) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(OnlyConclusionStep onlyConc) {
        // TODO Auto-generated method stub
        
    }

}
