package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.step.InferenceStep;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

public class LatexCreatorConstraints {
    private final List<String> constraints;

    public LatexCreatorConstraints() {
        constraints = new ArrayList<>();
        constraints.add("");
    }

    protected List<String> getConstraints() {
        List<String> temp = new ArrayList<>(constraints);
        temp.replaceAll(current -> current.equals("")
                ? current
                : DOLLAR_SIGN + current + DOLLAR_SIGN);
        //todo vllt. noch was anderes drumrum schreiben
        return temp;
    }

    protected void addConstraint(InferenceStep step) {
        String firstType = new LatexCreatorType(step.getConstraint().getFirstType()).getLatex();
        String secondType = new LatexCreatorType(step.getConstraint().getSecondType()).getLatex();
        String currentConstraint = firstType + SPACE + EQUALS + SPACE + secondType;
        String previousConstraints = constraints.get(constraints.size() - 1);
        if (constraints.size() > 1) {
            constraints.add(previousConstraints + COMMA + currentConstraint);
        } else {
            constraints.add(currentConstraint);
        }
    }
}
