package edu.kit.typicalc.view.content.errorcontent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.model.parser.Token;
import edu.kit.typicalc.view.main.InfoContent;

import java.util.Collection;
import java.util.Optional;

@CssImport("./styles/view/error-view.css")
public class ErrorView extends VerticalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = 239587L;

    private static final String ERROR_CONTENT_ID = "errorViewContent";
    private static final String ADDITIONAL_INFO_ID = "errorAdditionalInfo";
    private static final String ERROR_SUMMARY_ID = "errorSummary";

    private final H3 heading;
    private final Div errorMessage;
    private final ParseError error;
    private final Paragraph hint;

    public ErrorView(ParseError error) {
        this.error = error;
        VerticalLayout container = new VerticalLayout();
        container.setId(ERROR_CONTENT_ID);
        heading = new H3();
        heading.getStyle().set("color", "white");
        errorMessage = new Div();
        container.add(heading, errorMessage);

        InfoContent infoContent = new InfoContent();
        hint = new Paragraph();
        infoContent.addComponentAsFirst(hint);

        add(container, infoContent);
    }

    private Component buildErrorMessage(ParseError error) {
        VerticalLayout additionalInformation = new VerticalLayout();
        additionalInformation.setId(ADDITIONAL_INFO_ID);
        additionalInformation.setSpacing(false);
        Paragraph summary = new Paragraph(getTranslation("root." + error.toString()));
        summary.setId(ERROR_SUMMARY_ID);
        String term = error.getTerm();

        switch (error) {
            case TOO_FEW_TOKENS:
                additionalInformation.add(new Span(getTranslation("root.tooFewTokensHelp")));
                break;
            case UNEXPECTED_TOKEN:
                Optional<Token> cause = error.getCause();
                if (cause.isPresent()) {
                    additionalInformation.add(new Span(new Pre(getTranslation("root.termForError") + term)));
                    additionalInformation.add(
                            new Span(new Pre(" ".repeat(Math.max(getTranslation("root.termForError").length(),
                                    cause.get().getPos() + getTranslation("root.termForError").length()))
                            + "^ " + getTranslation("root.wrongCharacter") + cause.get().getText())));
                }
                break;
            case UNEXPECTED_CHARACTER:
                char c = error.getWrongCharacter();
                if (c != '\0') {
                    additionalInformation.add(new Span(new Pre(getTranslation("root.termForError") + term)));
                    additionalInformation.add(new Span(
                            new Pre(" ".repeat(Math.max(getTranslation("root.termForError").length(),
                            error.getPosition() + getTranslation("root.termForError").length()))
                            + "^ " + getTranslation("root.wrongCharacter") + c)));
                } else {
                    return summary;
                }
                break;
            default:
                throw new IllegalStateException(); // delete when updating to Java 12+
        }

        // add expected tokens, if available
        Optional<Collection<Token.TokenType>> expected = error.getExpected();
        if (expected.isPresent()) {
            Collection<Token.TokenType> possible = expected.get();
            StringBuilder sb = new StringBuilder();
            for (Token.TokenType t : possible) {
                if (sb.length() > 0) {
                    sb.append(' ');
                    sb.append(getTranslation("root.or"));
                    sb.append(' ');
                }
                sb.append(getTranslation("tokentype." + t));
            }
            additionalInformation.add(new Span(new Pre(
                    getTranslation("error.expectedToken", sb.toString()))));
        }
        // add expected character, if possible
        char correct = error.getExpectedCharacter();
        if (correct != '\0') {
            additionalInformation.add(new Span(new Pre(
                    getTranslation("error.expectedToken", correct))));
        }

        return new Div(summary, additionalInformation);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        heading.setText(getTranslation("error.heading"));
        errorMessage.removeAll();
        errorMessage.add(buildErrorMessage(error));
        hint.setText(getTranslation("error.hint"));
    }
}
