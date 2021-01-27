package edu.kit.typicalc.view;

import com.vaadin.flow.component.littemplate.LitTemplate;

/**
 * Represents an HTML element that uses MathJax and custom JavaScript classes to render its contents.
 * Provides an interface between Java code and said JavaScript classes. Allows to reveal parts of the
 * rendered LaTeX step-by-step. Allows for scaling of the rendered LaTeX.
 */
public abstract class MathjaxAdapter extends LitTemplate {

    /**
     * Creates a new HTML element that renders the LaTeX string passed as parameter using MathJax. Because
     * MathJax is used to render the String, all LaTeX commands used must be MathJax compatible.
     * @param latex the LaTex string to render with MathJax
     */
    protected MathjaxAdapter(String latex) {

    }
}
