package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class InfoContent extends VerticalLayout implements LocaleChangeObserver {
    private static final String GRAMMAR_ID = "inputSyntax";

    private final Span termExplanation;
    private final Paragraph termSyntax;
    private final Span assExplanation;
    private final Paragraph assSyntax;

    public InfoContent() {
        termExplanation = new Span();
        termSyntax = new Paragraph();
        assExplanation = new Span();
        assSyntax = new Paragraph();
        termSyntax.setId(GRAMMAR_ID);
        assSyntax.setId(GRAMMAR_ID);
        add(termExplanation, termSyntax, assExplanation, assSyntax);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        termExplanation.setText(getTranslation("root.termExplanation"));
        termSyntax.getElement().setProperty("innerHTML", getTranslation("root.termGrammar"));
        assExplanation.setText(getTranslation("root.assExplanation"));
        assSyntax.getElement().setProperty("innerHTML", getTranslation("root.assGrammar"));
    }
}
