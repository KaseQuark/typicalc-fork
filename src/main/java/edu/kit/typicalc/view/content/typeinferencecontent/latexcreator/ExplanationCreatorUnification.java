package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.AssumptionGeneratorUtil.typeAssumptionsToLatex;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.AMPERSAND;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Queue;

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
       String initialStepKey = isLetUnification ? "key1" : "key2";
       unificationTexts.add(getDefaultTextLatex(initialStepKey));
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

       StringBuilder latex = new StringBuilder(getDefaultTextLatex("key")).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex("key")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex("key")).
               append(toLatex(letVariableLatex)).
               append(getDefaultTextLatex("key")).
               append(toLatex(createTypeAbstraction(typeAssumptions))).
               append(getDefaultTextLatex("key")).
               append(toLatex(letVariableLatex)).
               append(getDefaultTextLatex("key")).
               append(toLatex(GAMMA)).
               append(getDefaultTextLatex("key")).
               append(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex()).
               append(getDefaultTextLatex("key")).
               append(toLatex(letCounterToLatex(SIGMA) + PAREN_LEFT + typeAssumptions + "" + PAREN_RIGHT)).
               append(getDefaultTextLatex("key"));
       unificationTexts.add(latex.toString());

       // add "second part" of let step here, so that the letCounterToLatex method can still be used
       latex = new StringBuilder(getDefaultTextLatex("key")).
               append(toLatex(letCounterToLatex(CONSTRAINT_SET))).
               append(getDefaultTextLatex("key"));
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
       String keyPrefix = isLetUnification ? "let" : "";

       StringBuilder latex = new StringBuilder(getDefaultTextLatex(keyPrefix + "key")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(getDefaultTextLatex(keyPrefix + "key")).
               append(toLatex(new LatexCreatorType(typeInferer.getFirstInferenceStep().getConclusion().getType(), mode)
                       .getLatex())).
               append(getDefaultTextLatex(keyPrefix + "key")).
               append(toLatex(new LatexCreatorType(typeInferer.getType().get(), mode).getLatex())).
               append(getDefaultTextLatex(keyPrefix + "key"));
       unificationTexts.add(latex.toString());
   }

   private void createMGU() {
       StringBuilder latex =  new StringBuilder(getDefaultTextLatex("key")).
               append(toLatex(letCounterToLatex(SIGMA))).
               append(getDefaultTextLatex("key")).
               append(toLatex(letCounterToLatex(CONSTRAINT_SET))).
               append(getDefaultTextLatex("key"));
       unificationTexts.add(latex.toString());
   }

   private String getDefaultTextLatex(String textKey) {
       return new StringBuilder(LatexCreatorConstants.TEXT + LatexCreatorConstants.CURLY_LEFT).
               append(provider.getTranslation(textKey, locale)).
               append(LatexCreatorConstants.CURLY_RIGHT)
               .toString();
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
       return LatexCreatorConstants.DOLLAR_SIGN + latex + LatexCreatorConstants.DOLLAR_SIGN;
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
           Queue<Type> leftTypeArgs = typeDeterminer.getArguments(currentConstraint.getFirstType());
           Queue<Type> rightTypeArgs = typeDeterminer.getArguments(currentConstraint.getSecondType());
           if (leftTypeArgs.isEmpty()) {
               // left side is a variable
               createVariableText(currentConstraint.getFirstType(), currentConstraint.getSecondType(),
                       currentConstraint, subs.unwrap().get(0));
           } else if (rightTypeArgs.isEmpty()) {
               // right side is a variable
               createVariableText(currentConstraint.getSecondType(), currentConstraint.getFirstType(),
                       currentConstraint, subs.unwrap().get(0));
           } else {
               // both sides are functions
               createFunctionText(leftTypeArgs, rightTypeArgs, currentConstraint,
                       constraints.get(constraints.size() - 1), constraints.get(constraints.size() - 2));
           }

           currentConstraint = constraints.get(constraints.size() - 1);
       }
   }

   private void createFunctionText(Queue<Type> leftTypeArgs, Queue<Type> rightTypeArgs, Constraint currentConstraint,
           Constraint newConstraint1, Constraint newConstraint2) {
       StringBuilder latex = new StringBuilder();
       latex.append(getDefaultTextLatex("key")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(currentConstraint.getFirstType(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(currentConstraint.getSecondType(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(leftTypeArgs.poll(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(rightTypeArgs.poll(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(leftTypeArgs.poll(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(rightTypeArgs.poll(), mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex("key")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint1, mode))).
       append(getDefaultTextLatex("key")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(newConstraint2, mode))).
       append(getDefaultTextLatex("key")).toString();
       unificationTexts.add(latex.toString());
   }

   private void createVariableText(Type variable, Type anyType, Constraint currentConstraint,
           Substitution newSubstitution) {
       StringBuilder latex = new StringBuilder();
       latex.append(getDefaultTextLatex("key")).
       append(toLatex(LatexCreatorConstraints.createSingleConstraint(currentConstraint, mode))).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(variable, mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(variable, mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(new LatexCreatorType(anyType, mode).getLatex())).
       append(getDefaultTextLatex("key")).
       append(toLatex(getSubstitutionLatex(newSubstitution))).
       append(getDefaultTextLatex("key"));
       unificationTexts.add(latex.toString());
   }

   private void createErrorText(UnificationError errorType) {
       if (errorType == UnificationError.DIFFERENT_TYPES) {
           unificationTexts.add(getDefaultTextLatex("key"));
       } else if (errorType == UnificationError.INFINITE_TYPE) {
           unificationTexts.add(getDefaultTextLatex("key"));
       }
   }

}
