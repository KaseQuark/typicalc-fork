package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.COLON;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.COMMA;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.DOT_SIGN;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.FOR_ALL;

/**
 * Util class for {@link LatexCreator} and {@link LatexCreatorConstraints} to generate LaTeX code from
 * a Map representing type assumptions.
 */
public final class AssumptionGeneratorUtil {
    private AssumptionGeneratorUtil() {
    }

    protected static String typeAssumptionsToLatex(Map<VarTerm, TypeAbstraction> typeAssumptions,
                                                   LatexCreatorMode mode) {
        if (typeAssumptions.isEmpty()) {
            return "";
        } else {
            StringBuilder assumptions = new StringBuilder();
            typeAssumptions.forEach(((varTerm, typeAbstraction) -> {
                String termLatex = new LatexCreatorTerm(varTerm).getLatex();
                String abstraction = generateTypeAbstraction(typeAbstraction, mode);
                assumptions.append(termLatex)
                        .append(COLON)
                        .append(abstraction)
                        .append(COMMA);
            }));
            assumptions.deleteCharAt(assumptions.length() - 1);
            return assumptions.toString();
        }
    }

    protected static String generateTypeAbstraction(TypeAbstraction abs, LatexCreatorMode mode) {
        StringBuilder abstraction = new StringBuilder();
        if (abs.hasQuantifiedVariables()) {
            abstraction.append(FOR_ALL);
            abs.getQuantifiedVariables().forEach(typeVariable -> {
                String variableTex = new LatexCreatorType(typeVariable).getLatex(mode);
                abstraction.append(variableTex).append(COMMA);
            });
            abstraction.deleteCharAt(abstraction.length() - 1);
            abstraction.append(DOT_SIGN);
        }
        abstraction.append(new LatexCreatorType(abs.getInnerType()).getLatex(mode));
        return abstraction.toString();
    }
}
