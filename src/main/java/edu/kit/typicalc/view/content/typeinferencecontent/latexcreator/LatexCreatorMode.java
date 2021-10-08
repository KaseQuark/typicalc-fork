package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

/**
 * Used to switch between different LaTeX generation methods.
 */
public enum LatexCreatorMode {
    /**
     * This mode adds a few HTML classes and ids to the generated LaTeX to facilitate highlighting etc.
     * See OVERVIEW.md in the view package for more details.
     */
    MATHJAX,
    /**
     * Regular LaTeX mode. Doesn't use any MathJax extensions.
     */
    NORMAL
}
