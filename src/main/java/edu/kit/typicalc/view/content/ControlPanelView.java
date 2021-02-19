package edu.kit.typicalc.view.content;

/**
 * Provides an interface for the ControlPanel to interact with its views. If a view doesn't
 * support a certain operation(s), it may provide a dummy implementation and/or disable
 * the corresponding button(s).
 */
public interface ControlPanelView {
    /**
     * Provides user with a way to share contents shown in the view (the URL and LaTeX code).
     */
    default void shareButton() {
    }

    /**
     * Go to the first step.
     */
    void firstStepButton();

    /**
     * Go to the last step.
     */
    void lastStepButton();

    /**
     * Go to the next step.
     */
    void nextStepButton();

    /**
     * Go to the previous step.
     */
    void previousStepButton();
}
