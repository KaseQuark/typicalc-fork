package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Component which contains information on the correct syntax of the term as well as the type assumptions.
 */
public class SyntaxContent extends VerticalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = 7193916603756938225L;

    private static final String GRAMMAR_CLASS = "input-syntax";

    private final Span termExplanation;
    private final Paragraph termSyntax;
    private final Span assExplanation;
    private final Span typeExplanation;
    private final Paragraph typeSyntax;

    /**
     * Creates a new SyntaxContent.
     */
    public SyntaxContent() {
        termExplanation = new Span();
        termSyntax = new Paragraph();
        assExplanation = new Span();
        typeExplanation = new Span();
        typeSyntax = new Paragraph();
        termSyntax.setClassName(GRAMMAR_CLASS);
        typeSyntax.setClassName(GRAMMAR_CLASS);
        add(termExplanation, termSyntax, assExplanation, typeExplanation, typeSyntax);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        termExplanation.setText(getTranslation("root.termExplanation"));
        termSyntax.getElement().setProperty("innerHTML", getTranslation("root.termGrammar"));
        assExplanation.getElement().setProperty("innerHTML", getTranslation("root.assExplanation"));
        typeExplanation.setText(getTranslation("root.typeExplanation"));
        typeSyntax.getElement().setProperty("innerHTML", getTranslation("root.typeGrammar"));
    }
}
