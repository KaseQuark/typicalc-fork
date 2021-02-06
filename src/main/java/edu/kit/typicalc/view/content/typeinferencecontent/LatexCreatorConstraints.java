package edu.kit.typicalc.view.content.typeinferencecontent;

import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.step.*;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.typicalc.view.content.typeinferencecontent.LatexCreatorConstants.*;

public class LatexCreatorConstraints implements StepVisitor {
    private final List<String> constraints;

    public LatexCreatorConstraints(TypeInfererInterface typeInferer) {
        constraints = new ArrayList<>();
        constraints.add(PHANTOM_X);
        typeInferer.getFirstInferenceStep().accept(this);
    }

    protected List<String> getConstraints() {
        List<String> temp = new ArrayList<>(constraints);
        temp.replaceAll(current -> DOLLAR_SIGN + current + DOLLAR_SIGN);
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
        letD.getPremise().accept(this);
    }

    @Override
    public void visit(EmptyStep empty) {
        // empty steps dont have constraints associated with them
    }
}
