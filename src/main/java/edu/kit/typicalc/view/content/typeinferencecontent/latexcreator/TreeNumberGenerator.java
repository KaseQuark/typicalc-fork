package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates the steps in which the tree should be during a certain step in unification.
 */
public class TreeNumberGenerator {
    private final List<Integer> numbers;
    private int current;

    /**
     * Initializes the TreeNumberGenerator().
     */
    protected TreeNumberGenerator() {
        numbers = new ArrayList<>();
        current = -1;
    }

    /**
     * Push the current index. Translates into the tree not doing anything in this step.
     */
    protected void push() {
        if (current < 0) {
            throw new IllegalStateException("The first step must add a step to the tree");
        }
        numbers.add(current);
    }

    /**
     * Push an incremented index. Translates into the tree going to the next step.
     */
    protected void incrementPush() {
        current++;
        numbers.add(current);
    }

    /**
     * Returns the resulting list.
     *
     * @return the list of step indices
     */
    protected List<Integer> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }
}
