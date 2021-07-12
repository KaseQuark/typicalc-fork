package edu.kit.typicalc.model;

import java.util.Objects;

/**
 * Provides the next step number on demand.
 */
public class StepNumberFactory {

    private static final int FIRST_STEP_NUMBER = 0;

    private int nextStepIndex;

    public StepNumberFactory() {
        this.nextStepIndex = FIRST_STEP_NUMBER;
    }

    public int nextStepIndex() {
        return nextStepIndex++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StepNumberFactory that = (StepNumberFactory) o;
        return nextStepIndex == that.nextStepIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextStepIndex);
    }
}
