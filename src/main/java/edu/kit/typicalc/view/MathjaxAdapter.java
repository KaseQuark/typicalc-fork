package edu.kit.typicalc.view;

import com.vaadin.flow.component.dependency.JsModule;

/**
 * Represents an HTML element that uses MathJax and custom TypeScript modules to render its contents.
 * Provides an interface between Java code and said TypeScript modules.
 * Multiple steps can be shown using the getStepCount and showStep methods.
 */
@JsModule("./src/mathjax-adapter.ts")
public interface MathjaxAdapter {

    /**
     * @return amount of steps available
     */
    int getStepCount();

    /**
     * Show a specific step to the user.
     *
     * @param n step index
     */
    void showStep(int n);
}
