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
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Dialog which contains information on the correct syntax for the users input.
 */
@CssImport("./styles/view/main/info-dialog.css")
public class InfoDialog extends Dialog implements LocaleChangeObserver {
    private static final long serialVersionUID = 2914411566361539614L;

    /*
     * IDs for the imported .css-file
     */
    private static final String INFO_HEADER_ID = "infoHeader";
    private static final String INFO_CONTENT_ID = "infoContent";
    private static final String INPUT_SYNATX_ID = "inputSyntax";
    private static final String CLOSE_ICON_ID = "closeIcon";

    private final H4 heading;

    /**
     * Creates new InfoDialog.
     */
    protected InfoDialog() {
        heading = new H4(getTranslation("root.inputSyntax"));
        HorizontalLayout infoHeader = new HorizontalLayout(heading);
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);
        infoHeader.setId(INFO_HEADER_ID);
        infoHeader.add(closeIcon);
        
        //TODO fill with content
        VerticalLayout infoContent = new VerticalLayout();
        infoContent.setId(INFO_CONTENT_ID);
        Span explanation = new Span(getTranslation("root.infoExplanation"));
        Paragraph inputSyntax = new Paragraph();
        String paragraphContent = getTranslation("root.inputGrammar");
        inputSyntax.getElement().setProperty("innerHTML", paragraphContent);
        inputSyntax.setId(INPUT_SYNATX_ID);
        infoContent.add(explanation, inputSyntax);
        add(infoHeader, infoContent);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.inputSyntax"));
    }
}
