package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNumberGenerator {
    private final List<Integer> numbers;
    private int current;

    protected TreeNumberGenerator() {
        numbers = new ArrayList<>();
        current = -1;
    }

    protected void push() {
        if (current < 0) {
            throw new IllegalStateException("The first step must add a step to the tree");
        }
        numbers.add(current);
    }

    protected void incrementPush() {
        current++;
        numbers.add(current);
    }

    protected List<Integer> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }
}
