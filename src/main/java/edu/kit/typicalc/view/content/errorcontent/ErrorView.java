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
import edu.kit.typicalc.model.parser.AdditionalInformation;
import edu.kit.typicalc.model.parser.ExpectedInput;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.model.parser.Token;
import edu.kit.typicalc.view.main.SyntaxContent;

import java.util.Collection;
import java.util.Optional;

@CssImport("./styles/view/error-view.css")
public class ErrorView extends VerticalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = 239587L;

    private static final String ERROR_CONTENT_ID = "error-view-content";
    private static final String ADDITIONAL_INFO_ID = "error-additional-info";
    private static final String ERROR_SUMMARY_ID = "error-summary";

    private final H3 heading;
    private final Div errorMessage;
    private final transient ParseError error;
    private final Paragraph hint;

    public ErrorView(ParseError error) {
        this.error = error;
        VerticalLayout container = new VerticalLayout();
        container.setId(ERROR_CONTENT_ID);
        heading = new H3();
        heading.getStyle().set("color", "white");
        errorMessage = new Div();
        container.add(heading, errorMessage);

        SyntaxContent syntaxContent = new SyntaxContent();
        hint = new Paragraph();
        syntaxContent.addComponentAsFirst(hint);

        add(container, syntaxContent);
    }

    private String errorMessageForToken(Token token) {
        if (token.getType() != Token.TokenType.EOF) {
            return getTranslation("error.wrongCharacter") + token.getText();
        } else {
            return getTranslation("error.tooFewTokensHelp");
        }
    }

    private Component buildErrorMessage(ParseError error) {
        VerticalLayout additionalInformation = new VerticalLayout();
        additionalInformation.setId(ADDITIONAL_INFO_ID);
        additionalInformation.setSpacing(false);
        Paragraph summary = new Paragraph(getTranslation("root." + error.toString()));
        summary.setId(ERROR_SUMMARY_ID);
        String term = error.getTerm();
        String descriptionForError = null;
        Optional<ParseError.ErrorSource> errorType = error.getErrorType();
        if (errorType.isEmpty()) {
            throw new IllegalStateException();
        }
        if (errorType.get() == ParseError.ErrorSource.TERM_ERROR) {
            descriptionForError = "error.termForError";
        } else if (errorType.get() == ParseError.ErrorSource.TYPE_ASSUMPTION_ERROR) {
            descriptionForError = "error.typeAssumptionForError";
        }
        switch (error.getCauseEnum()) {
            case UNEXPECTED_TOKEN:
                Optional<Token> cause = error.getCause();
                if (cause.isPresent()) {
                    String errorText = errorMessageForToken(cause.get());
                    additionalInformation.add(new Span(new Pre(getTranslation(descriptionForError) + term
                            + "\n" + " ".repeat(Math.max(getTranslation(descriptionForError).length(),
                            cause.get().getPos() + getTranslation(descriptionForError).length()))
                            + "^ " + errorText)));
                }
                break;
            case UNEXPECTED_CHARACTER:
                char c = error.getWrongCharacter();
                if (c != '\0') {
                    additionalInformation.add(new Span(new Pre(getTranslation(descriptionForError) + term
                            + "\n" + " ".repeat(Math.max(getTranslation(descriptionForError).length(),
                            error.getPosition() + getTranslation(descriptionForError).length()))
                            + "^ " + getTranslation("error.wrongCharacter") + c)));
                } else {
                    return summary;
                }
                break;
            default:
                throw new IllegalStateException(); // delete when updating to Java 12+
        }

        // state expected input, if available
        Optional<ExpectedInput> expectedInput = error.getExpectedInput();
        if (expectedInput.isPresent()) {
            ExpectedInput e = expectedInput.get();
            additionalInformation.add(new Span(new Pre(
                    getTranslation("error.expectedToken",
                            getTranslation("expectedinput." + e)))));
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

        // state more additional information, if available
        Optional<AdditionalInformation> moreAdditionalInformation = error.getAdditionalInformation();
        if (moreAdditionalInformation.isPresent()) {
            AdditionalInformation e = moreAdditionalInformation.get();
            additionalInformation.add(new Span(new Pre(
                    getTranslation("error.additionalInformation",
                            getTranslation("additionalInformation." + e)))));
        }

        return new Div(additionalInformation);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        heading.setText(getTranslation("error.heading"));
        errorMessage.removeAll();
        errorMessage.add(buildErrorMessage(error));
        hint.setText(getTranslation("error.hint"));
    }
}
