package edu.kit.typicalc.view.content.errorcontent;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.view.main.InfoContent;

@CssImport("./styles/view/error-view.css")
public class ErrorView extends VerticalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = 239587L;

    private static final String ERROR_CONTENT_ID = "errorViewContent";
    private static final String ADDITIONAL_INFO_ID = "errorAdditionalInfo";
    private static final String ERROR_SUMMARY_ID = "errorSummary";

    private static final int NO_ADDITIONAL_INFO = -1;

    private final H3 heading;
    private Div errorMessage;
    private final ParseError error;

    public ErrorView(ParseError error) {
        this.error = error;
        VerticalLayout container = new VerticalLayout();
        heading = new H3();
        heading.getStyle().set("color", "white");
        errorMessage = new Div();
        container.add(heading, errorMessage);
        add(container, new InfoContent());
        container.setId(ERROR_CONTENT_ID);
    }

    private Component buildErrorMessage(ParseError error) {
        VerticalLayout additionalInformation = new VerticalLayout();
        additionalInformation.setId(ADDITIONAL_INFO_ID);
        Paragraph summary = new Paragraph(getTranslation("root." + error.toString()));
        summary.setId(ERROR_SUMMARY_ID);

        if (error == ParseError.TOO_FEW_TOKENS) {
            additionalInformation.add(new Span(getTranslation("root.tooFewTokensHelp")));
        } else if (error == ParseError.UNEXPECTED_CHARACTER) {
            char c = error.getWrongCharacter();
            if (c != '\0') {
                additionalInformation.add(new Span(getTranslation("root.wrongCharacter") + c));
                additionalInformation.add(new Span(getTranslation("root.position") + error.getPosition()));
            } else {
                return summary;
            }
        } else {
            if (error.getCause().getPos() == NO_ADDITIONAL_INFO) {
                return summary;
            } else {
                additionalInformation.add(new Span(getTranslation("root.wrongCharacter") + error.getCause().getText()));
                additionalInformation.add(new Span(getTranslation("root.position") + error.getCause().getPos()));
            }
        }

        return new Details(summary, additionalInformation);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        heading.setText(getTranslation("error.heading"));
        errorMessage.removeAll();
        errorMessage.add(buildErrorMessage(error));
    }
}
