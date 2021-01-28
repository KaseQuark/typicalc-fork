package edu.kit.typicalc.view;

/**
 * Represents an HTML element that uses MathJax and custom JavaScript classes to render its contents.
 * Provides an interface between Java code and said JavaScript classes. Allows to reveal parts of the
 * rendered LaTeX step-by-step. Allows for scaling of the rendered LaTeX.
 */
public interface MathjaxAdapter {

    int getStepCount();

    void showStep(int n);

    void scale(double newScaling);
}
