package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.typeAssumptionsToLatex;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.APOSTROPHE;
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

/**
 * Generates LaTeX code for explanatory texts in a specific language for every step of the unification algorithm.
 */
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

   /**
    * Generates LaTeX code from the provided type inferer for the unification algorithm.
    * 
    * @param typeInferer        the TypeInfererInterface to create the LaTeX-code from      
    * @param locale             the language of the explanatory texts
    * @param provider           I18NProvider to get the templates from the resources bundle
    * @param mode               the used LaTeX generation method
    * @param letCounter         counter needed for nested let terms 
    * @param isLetUnification   variable to indicate if it is the final unification or a let unification
    * @param letVariable        optional containing the let variable in case of a let unification
    */
   protected ExplanationCreatorUnification(TypeInfererInterface typeInferer, Locale locale, I18NProvider provider,
           LatexCreatorMode mode, int letCounter, boolean isLetUnification, Optional<LambdaTerm> letVariable) {
       this.typeInferer = typeInferer;
       this.locale = locale;
       this.provider = provider;
       this.mode = mode;
       this.letCounter = letCounter;
       this.letVariable = letVariable;

       buildTexts(isLetUnification);
   }

   /**
    * Returns a pair of a list of strings containing the explanatory texts and a boolean value.
    * The boolean value is true if an error occurred during the let unification.
    * 
    * @return a pair of a list of strings and a boolean value
    */
   protected Pair<List<String>, Boolean> getUnificationsTexts() {
       return Pair.of(unificationTexts, errorOccurred);
   }

   private void buildTexts(boolean isLetUnification) {
       String initialPrefix = isLetUnification ? LET_KEY_PREFIX : KEY_PREFIX;
       String letVariable = isLetUnification 
               ? toLatex(new LatexCreatorTerm(this.letVariable.get(), mode).getLatex()) : "";
       String constraintSet = toLatex(letCounterToLatex(CONSTRAINT_SET));
       String finalText = provider.getTranslation(initialPrefix + "initial", locale, constraintSet, letVariable);
       unificationTexts.add(finalText);
       createUnficationTexts();

       if (!errorOccurred) {
           createMGU();
           createFinalType(isLetUnification);
           if (isLetUnification) {
               createLetUnficiationFinish();
           }
       } else if (isLetUnification) {
           unificationTexts.remove(unificationTexts.size() - 1);
       }
   }

   private void createLetUnficiationFinish() {
       String typeAssumptions = 
               typeAssumptionsToLatex(typeInferer.getFirstInferenceStep().getConclusion().getTypeAssumptions(), mode);
       String letVariableLatex = toLatex(new LatexCreatorTerm(this.letVariable.get(), mode).getLatex());
       String gamma = toLatex(GAMMA + APOSTROPHE);
       String sigma = toLatex(letCounterToLatex(SIGMA));
       String finalType = toLatex(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex());
       String newAssumptions = toLatex(letCounterToLatex(SIGMA) + PAREN_LEFT + typeAssumptions + "" + PAREN_RIGHT);
       String typeAbstraction = toLatex(createTypeAbstraction(typeAssumptions));
       String finalText1 = provider.getTranslation(LET_KEY_PREFIX + "typeAss", locale, gamma, sigma, letVariableLatex,
               typeAbstraction, finalType, newAssumptions);
       unificationTexts.add(finalText1);

       // add "second part" of let step here, so that the letCounterToLatex method can still be used
       String constraintSet = toLatex(letCounterToLatex(CONSTRAINT_SET));
       String finalText2 = provider.getTranslation(LET_KEY_PREFIX + "letStep", locale, constraintSet);
       unificationTexts.add(finalText2);
   }

   private String createTypeAbstraction(String typeAssumptions) {
       return new StringBuilder(TYPE_ABSTRACTION + PAREN_LEFT + letCounterToLatex(SIGMA) + PAREN_LEFT).
               append(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode)
                       .getLatex()).
               append("" + PAREN_RIGHT + COMMA + letCounterToLatex(SIGMA) + PAREN_LEFT).
               append(typeAssumptions).
               append("" + PAREN_RIGHT + PAREN_RIGHT).toString();
   }

   private void createFinalType(boolean isLetUnification) {
       String keyPrefix = isLetUnification ? LET_KEY_PREFIX : KEY_PREFIX;
       String sigma = toLatex(letCounterToLatex(SIGMA));
       String initialType = toLatex(
               new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode).getLatex());
       String finalType = toLatex(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex());
       String finalText = provider.getTranslation(keyPrefix + "finalType", locale, sigma, initialType, finalType);
       unificationTexts.add(finalText);
   }

   private void createMGU() {
       String sigma = toLatex(letCounterToLatex(SIGMA));
       String constraintSet = toLatex(letCounterToLatex(CONSTRAINT_SET));
       String finalText = provider.getTranslation(KEY_PREFIX + "mgu", locale, sigma, constraintSet);
       unificationTexts.add(finalText);
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
       return new StringBuilder()
               .append(BRACKET_LEFT)
               .append(new LatexCreatorType(sub.getVariable(), mode).getLatex())
               .append(SUBSTITUTION_SIGN)
               .append(new LatexCreatorType(sub.getType(), mode).getLatex())
               .append(BRACKET_RIGHT)
               .append(LATEX_NEW_LINE).toString();
   }

   private void createUnficationTexts() {
       List<UnificationStep> unificationSteps;
       if (typeInferer.getUnificationSteps().isPresent()) {
           unificationSteps = typeInferer.getUnificationSteps().get();
       } else {
           errorOccurred = true;
           return;
       }

       // skip first step since the substitutions list is still empty (unification introduction is shown)
       for (int stepNum = 1; stepNum < unificationSteps.size(); stepNum++) {
           UnificationStep step = unificationSteps.get(stepNum);
           // works because only step 0 has no constraint
           Constraint currentConstraint = step.getProcessedConstraint().get();
           Result<List<Substitution>, UnificationError> subs = step.getSubstitutions();

           if (subs.isError()) {
               createErrorText(subs.unwrapError());
               errorOccurred = true;
               break;
           }
           List<Substitution> substitutions = subs.unwrap();

           if (currentConstraint.getFirstType().equals(currentConstraint.getSecondType())) {
               // trivial constraint
               createTrivialConstraintText(currentConstraint);
           } else if (typeDeterminer.getArguments(currentConstraint.getFirstType()).isEmpty()) {
               // left side is a variable
               createVariableText(currentConstraint.getFirstType(), currentConstraint.getSecondType(),
                       currentConstraint, substitutions.get(substitutions.size() - 1));
           } else if (typeDeterminer.getArguments(currentConstraint.getSecondType()).isEmpty()) {
               // right side is a variable
               createVariableText(currentConstraint.getSecondType(), currentConstraint.getFirstType(),
                       currentConstraint, substitutions.get(substitutions.size() - 1));
           } else {
               // both sides are functions
               createFunctionText(currentConstraint, step.getConstraints().get(0), step.getConstraints().get(1));
           }
       }
   }

   private void createTrivialConstraintText(Constraint currentConstraint) {
       String constraintLatex = toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode));
       String finalText = provider.getTranslation(KEY_PREFIX + "trivial", locale, constraintLatex);
       unificationTexts.add(finalText);
   }

   private void createFunctionText(Constraint currentConstraint, Constraint newConstraint1, Constraint newConstraint2) {
       String constraintLatex = toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode));
       String firstType = toLatex(new LatexCreatorType(currentConstraint.getFirstType(), mode).getLatex());
       String secondType = toLatex(new LatexCreatorType(currentConstraint.getSecondType(), mode).getLatex());
       String firstNewConstraint = toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint1, mode));
       String secondNewConstraint = toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint2, mode));
       String finalText = provider.getTranslation(KEY_PREFIX + "function", locale, constraintLatex, firstType,
               secondType, firstNewConstraint, secondNewConstraint);
       unificationTexts.add(finalText);
   }

   private void createVariableText(Type variable, Type anyType, Constraint currentConstraint,
           Substitution newSubstitution) {
       String constraintLatex = toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode));
       String variableType = toLatex(new LatexCreatorType(variable, mode).getLatex());
       String otherType = toLatex(new LatexCreatorType(anyType, mode).getLatex());
       String substitutionLatex = toLatex(getSubstitutionLatex(newSubstitution));
       String finalText = provider.getTranslation(KEY_PREFIX + "variable", locale,
               constraintLatex, variableType, otherType, substitutionLatex);
       unificationTexts.add(finalText);
   }

   private void createErrorText(UnificationError errorType) {
       if (errorType == UnificationError.INFINITE_TYPE) {
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "infiniteType"));
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "infiniteType"));
       } else if (errorType == UnificationError.DIFFERENT_TYPES) {
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "differentTypes"));
           unificationTexts.add(getDefaultTextLatex(KEY_PREFIX + "differentTypes"));
       }
   }

}
