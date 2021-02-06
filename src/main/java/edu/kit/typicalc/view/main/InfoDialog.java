package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Dialog which contains information on the correct syntax for the users input.
 */
@CssImport("./styles/view/main/info-dialog.css")
public class InfoDialog extends Dialog {
    private static final long serialVersionUID = 2914411566361539614L;

    /*
     * IDs for the imported .css-file
     */
    private static final String INFO_HEADER_ID = "infoHeader";
    private static final String INFO_CONTENT_ID = "infoContent";
    private static final String GRAMMAR_ID = "inputSyntax";
    private static final String CLOSE_ICON_ID = "closeIcon";

    /**
     * Creates new InfoDialog.
     */
    protected InfoDialog() {
        H4 heading = new H4(getTranslation("root.inputSyntax"));
        HorizontalLayout infoHeader = new HorizontalLayout(heading);
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);
        infoHeader.setId(INFO_HEADER_ID);
        infoHeader.add(closeIcon);

        VerticalLayout infoContent = createInfoContent();
        infoContent.setId(INFO_CONTENT_ID);

        add(infoHeader, infoContent);
    }

    private VerticalLayout createInfoContent() {
        Span termExplanation = new Span(getTranslation("root.termExplanation"));
        Paragraph termSyntax = new Paragraph();
        String termSyntaxContent = getTranslation("root.termGrammar");
        termSyntax.getElement().setProperty("innerHTML", termSyntaxContent);
        termSyntax.setId(GRAMMAR_ID);
        Span assExplanation = new Span(getTranslation("root.assExplanation"));
        Paragraph assSyntax = new Paragraph();
        String assSyntaxContent = getTranslation("root.assGrammar");
        assSyntax.getElement().setProperty("innerHTML", assSyntaxContent);
        assSyntax.setId(GRAMMAR_ID);
        return new VerticalLayout(termExplanation, termSyntax, assExplanation, assSyntax);
    }

    // local change observer is not needed, the dialog is created when it is opened
}
