package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.term.VarTerm;
import edu.kit.typicalc.model.type.TypeAbstraction;

import java.util.Map;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.DOT_SIGN;

public final class AssumptionGeneratorUtil {
    private AssumptionGeneratorUtil() { }

    protected static String typeAssumptionsToLatex(Map<VarTerm, TypeAbstraction> typeAssumptions) {
        if (typeAssumptions.isEmpty()) {
            return "";
        } else {
            StringBuilder assumptions = new StringBuilder();
            typeAssumptions.forEach(((varTerm, typeAbstraction) -> {
                String termLatex = new LatexCreatorTerm(varTerm).getLatex();
                String abstraction = generateTypeAbstraction(typeAbstraction);
                assumptions.append(termLatex)
                        .append(COLON)
                        .append(abstraction)
                        .append(COMMA);
            }));
            assumptions.deleteCharAt(assumptions.length() - 1);
            return assumptions.toString();
        }
    }

    protected static String generateTypeAbstraction(TypeAbstraction abs) {
        StringBuilder abstraction = new StringBuilder();
        if (abs.hasQuantifiedVariables()) {
            abstraction.append(FOR_ALL);
            abs.getQuantifiedVariables().forEach(typeVariable -> {
                String variableTex = new LatexCreatorType(typeVariable).getLatex();
                abstraction.append(variableTex).append(COMMA);
            });
            abstraction.deleteCharAt(abstraction.length() - 1);
            abstraction.append(DOT_SIGN);
        }
        abstraction.append(new LatexCreatorType(abs.getInnerType()).getLatex());
        return abstraction.toString();
    }
}
