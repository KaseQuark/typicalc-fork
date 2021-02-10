package edu.kit.typicalc.view.content.typeinferencecontent;

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
