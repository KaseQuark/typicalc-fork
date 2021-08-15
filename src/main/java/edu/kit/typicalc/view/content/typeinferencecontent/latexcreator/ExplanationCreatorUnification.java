package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.typeAssumptionsToLatex;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.BRACKET_LEFT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.BRACKET_RIGHT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.COMMA;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.LATEX_NEW_LINE;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.SUBSTITUTION_SIGN;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.TYPE_ABSTRACTION;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CURLY_LEFT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CURLY_RIGHT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.LET;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.PAREN_LEFT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.PAREN_RIGHT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.UNDERSCORE;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CONSTRAINT_SET;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.GAMMA;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.SIGMA;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.DOLLAR_SIGN;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.SPACE;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.flow.i18n.I18NProvider;

import edu.kit.typicalc.model.Constraint;
import edu.kit.typicalc.model.Substitution;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.UnificationError;
import edu.kit.typicalc.model.UnificationStep;
import edu.kit.typicalc.model.term.LambdaTerm;
import edu.kit.typicalc.model.type.Type;
import edu.kit.typicalc.model.type.TypeArgumentVisitor;
import edu.kit.typicalc.util.Result;

public class ExplanationCreatorUnification {
    private static final String KEY_PREFIX = "expUnification.";
    private static final String LET_KEY_PREFIX = "expLetUnification.";    
    
    private final TypeArgumentVisitor typeDeterminer = new TypeArgumentVisitor();
    private final TypeInfererInterface typeInferer;
    private final Locale locale;
    private final I18NProvider provider;
    private final LatexCreatorMode mode;
    private final int letCounter;
    private final Optional<LambdaTerm> letVariable;

    private final List<String> unificationTexts = new ArrayList<>();
    private boolean errorOccurred;

   public ExplanationCreatorUnification(TypeInfererInterface typeInferer, Locale locale, I18NProvider provider,
           LatexCreatorMode mode, int letCounter, boolean isLetUnification, Optional<LambdaTerm> letVariable) {
       this.typeInferer = typeInferer;
       this.locale = locale;
       this.provider = provider;
       this.mode = mode;
       this.letCounter = letCounter;
       this.letVariable = letVariable;

       buildTexts(isLetUnification);
   }

   public Pair<List<String>, Boolean> getUnificationsTexts() {
       return Pair.of(unificationTexts, errorOccurred);
   }

   private void buildTexts(boolean isLetUnification) {
       String initialPrefix = isLetUnification ? LET_KEY_PREFIX : KEY_PREFIX;
       unificationTexts.add(getDefaultTextLatex(initialPrefix + "initial1"));
       createUnficationTexts();

       if (!errorOccurred) {
           createMGU();
           createFinalType(isLetUnification);
           if (isLetUnification) {
               createLetUnficiationFinish();
           }
       }
   }

   private void createLetUnficiationFinish() {
       String typeAssumptions =
               typeAssumptionsToLatex(typeInferer.getFirstInferenceStep().getConclusion().getTypeAssumptions(), mode);
       String letVariableLatex = new LatexCreatorTerm(this.letVariable.get(), mode).getLatex();

       StringBuilder latex = new StringBuilder(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss1")).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss2")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss3")).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss4")).
               append(toLatex(letVariableLatex)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss5")).
               append(toLatex(createTypeAbstraction(typeAssumptions))).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss6")).
               append(toLatex(letVariableLatex)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss7")).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss8")).
               append(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex()).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss9")).
               append(toLatex(letCounterToLatex(SIGMA) + PAREN_LEFT + typeAssumptions + "" + PAREN_RIGHT)).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "typeAss10"));
       unificationTexts.add(latex.toString());

       // add "second part" of let step here, so that the letCounterToLatex method can still be used
       latex = new StringBuilder(getDefaultTextLatex(LET_KEY_PREFIX + "letStep1")).
               append(toLatex(letCounterToLatex(CONSTRAINT_SET))).
               append(getDefaultTextLatex(LET_KEY_PREFIX + "letStep2"));
       unificationTexts.add(latex.toString());
   }

   private String createTypeAbstraction(String typeAssumptions) {
       return new StringBuilder(TYPE_ABSTRACTION + PAREN_LEFT + letCounterToLatex(SIGMA)).
               append(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode)
                       .getLatex()).
               append("" + PAREN_RIGHT + COMMA + letCounterToLatex(SIGMA) + PAREN_LEFT).
               append(typeAssumptions).
               append("" + PAREN_RIGHT + PAREN_RIGHT).toString();
   }

   private void createFinalType(boolean isLetUnification) {
       String keyPrefix = isLetUnification ? LET_KEY_PREFIX : KEY_PREFIX;

       StringBuilder latex = new StringBuilder(getDefaultTextLatex(keyPrefix + "finalType1")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(getDefaultTextLatex(keyPrefix + "finalType2")).
               append(toLatex(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode)
                       .getLatex())).
               append(getDefaultTextLatex(keyPrefix + "finalType3")).
               append(toLatex(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex())).
               append(getDefaultTextLatex(keyPrefix + "finalType4"));
       unificationTexts.add(latex.toString());
   }

   private void createMGU() {
       StringBuilder latex =  new StringBuilder(getDefaultTextLatex(KEY_PREFIX + "mgu1")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(getDefaultTextLatex(KEY_PREFIX + "mgu2")).
               append(toLatex(letCounterToLatex(CONSTRAINT_SET))).
               append(getDefaultTextLatex(KEY_PREFIX + "mgu3"));
       unificationTexts.add(latex.toString());
   }

   private String getDefaultTextLatex(String textKey) {
       return provider.getTranslation(textKey, locale);
   }

   // WARNING: call toLatex() before to get proper latex code
   private String letCounterToLatex(String setName) {
       switch (letCounter) {
           case 0:
               return setName;
           case 1:
               return setName + UNDERSCORE + CURLY_LEFT + LET + CURLY_RIGHT;
           default:
               return setName + UNDERSCORE + CURLY_LEFT + LET + CURLY_LEFT + letCounter
                   + CURLY_RIGHT + CURLY_RIGHT;
       }
   }

   private String toLatex(String latex) {
       return SPACE + DOLLAR_SIGN + latex + DOLLAR_SIGN + SPACE;
   }

   private String getSubstitutionLatex(Substitution sub) {
       return new StringBuilder(BRACKET_LEFT)
               //.append(AMPERSAND)
               .append(new LatexCreatorType(sub.getVariable(), mode).getLatex())
               .append(SUBSTITUTION_SIGN)
               .append(new LatexCreatorType(sub.getType(), mode).getLatex())
               .append(BRACKET_RIGHT)
               .append(LATEX_NEW_LINE).toString();
   }

   private void createUnficationTexts() {
       List<UnificationStep> unificationSteps = typeInferer.getUnificationSteps()
               .orElseThrow(IllegalStateException::new);

       List<Constraint> initialConstraints = unificationSteps.get(0).getConstraints();
       // skip first step since the substitutions list is still empty (unification introduction is shown)
       Constraint currentConstraint = initialConstraints.get(initialConstraints.size() - 1);
       for (int stepNum = 1; stepNum < unificationSteps.size() - 1; stepNum++) {
           UnificationStep step = unificationSteps.get(stepNum);
           Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();

           if (subs.isError()) {
               createErrorText(subs.unwrapError());
               errorOccurred = true;
               break;
           }

           List<Constraint> constraints = step.getConstraints();
           if (currentConstraint.getFirstType().equals(currentConstraint.getSecondType())) {
               // trivial constraint
               createTrivialConstraintText(currentConstraint);
           } else if (typeDeterminer.getArguments(currentConstraint.getFirstType()).isEmpty()) {
               // left side is a variable
               createVariableText(currentConstraint.getFirstType(), currentConstraint.getSecondType(),
                       currentConstraint, subs.unwrap().get(0));
           } else if (typeDeterminer.getArguments(currentConstraint.getSecondType()).isEmpty()) {
               // right side is a variable
               createVariableText(currentConstraint.getSecondType(), currentConstraint.getFirstType(),
                       currentConstraint, subs.unwrap().get(0));
           } else {
               // both sides are functions
               createFunctionText(currentConstraint,
                       constraints.get(constraints.size() - 1), constraints.get(constraints.size() - 2));
           }

           currentConstraint = constraints.get(constraints.size() - 1);
       }
   }
   
   private void createTrivialConstraintText(Constraint currentConstraint) {
       StringBuilder latex = new StringBuilder();
       latex.append(getDefaultTextLatex(KEY_PREFIX + "trivial1")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "trivial2"));
       unificationTexts.add(latex.toString());
   }

   private void createFunctionText(Constraint currentConstraint, Constraint newConstraint1, Constraint newConstraint2) {
       StringBuilder latex = new StringBuilder();
       latex.append(getDefaultTextLatex(KEY_PREFIX + "function1")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "function2")).
       append(toLatex(new LatexCreatorType(currentConstraint.getFirstType(), mode).getLatex())).
       append(getDefaultTextLatex(KEY_PREFIX + "function3")).
       append(toLatex(new LatexCreatorType(currentConstraint.getSecondType(), mode).getLatex())).
       append(getDefaultTextLatex(KEY_PREFIX + "function4")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint1, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "function5")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint2, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "function6")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "function7"));
       unificationTexts.add(latex.toString());
   }

   private void createVariableText(Type variable, Type anyType, Constraint currentConstraint,
           Substitution newSubstitution) {
       StringBuilder latex = new StringBuilder();
       latex.append(getDefaultTextLatex(KEY_PREFIX + "variable1")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex(KEY_PREFIX + "variable2")).
       append(toLatex(new LatexCreatorType(variable, mode).getLatex())).
       append(getDefaultTextLatex(KEY_PREFIX + "variable3")).
       append(toLatex(new LatexCreatorType(variable, mode).getLatex())).
       append(getDefaultTextLatex(KEY_PREFIX + "variable4")).
       append(toLatex(new LatexCreatorType(anyType, mode).getLatex())).
       append(getDefaultTextLatex(KEY_PREFIX + "variable5")).
       append(toLatex(getSubstitutionLatex(newSubstitution))).
       append(getDefaultTextLatex(KEY_PREFIX + "variable6"));
       unificationTexts.add(latex.toString());
   }

   private void createErrorText(UnificationError errorType) {
       if (errorType == UnificationError.DIFFERENT_TYPES) {
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "infiniteType"));
       } else if (errorType == UnificationError.INFINITE_TYPE) {
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "differentTypes"));
       }
   }

}
