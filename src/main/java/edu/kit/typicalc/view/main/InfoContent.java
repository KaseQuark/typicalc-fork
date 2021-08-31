package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class InfoContent extends VerticalLayout implements LocaleChangeObserver {
    private static final String GRAMMAR_ID = "input-syntax";

    private final Span termExplanation;
    private final Paragraph termSyntax;
    private final Span assExplanation;
    private final Span typeExplanation;
    private final Paragraph typeSyntax;

    public InfoContent() {
        termExplanation = new Span();
        termSyntax = new Paragraph();
        assExplanation = new Span();
        typeExplanation = new Span();
        typeSyntax = new Paragraph();
        termSyntax.setId(GRAMMAR_ID);
        typeSyntax.setId(GRAMMAR_ID);
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
