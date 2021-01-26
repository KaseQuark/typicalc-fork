package edu.kit.typicalc.view.content;

public interface ControlPanelView {
    /**
     * Provides user with a way to share contents shown in the view, e.g. the URL orLaTeX-code.
     */
    void shareButton();

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
