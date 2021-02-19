package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * This class provides the layout for an AccordionPanel with either text or a button and text as its content.
 * These panels are used for the Accordion in the {@link edu.kit.typicalc.view.main.HelpDialog}.
 */
public class HelpContentField extends AccordionPanel implements LocaleChangeObserver {
    private static final String CLASSNAME = "help-field";

    private static final long serialVersionUID = -2793005857762897734L;

    private final String summaryKey;
    private final String contentKey;
    private final Paragraph content;

    /**
     * Create a HelpContentField with keys for the strings of the summary and the content of this AccordionPanel.
     *
     * @param summaryKey the key for the string of the summary
     * @param contentKey the key for the string of the content
     */
    protected HelpContentField(String summaryKey, String contentKey) {
        this.summaryKey = summaryKey;
        this.contentKey = contentKey;
        this.content = new Paragraph(getTranslation(contentKey));
        setSummaryText(getTranslation(summaryKey));
        setContent(content);
        addThemeVariants(DetailsVariant.FILLED);
    }

    /**
     * Create a HelpContentField with a button and keys for the string of the summary and the content of this
     * AccordionPanel.
     *
     * @param summaryKey the key for the string of the summary
     * @param button     the button
     * @param contentKey the key for the string of the content
     */
    protected HelpContentField(String summaryKey, Button button, String contentKey) {
        this(summaryKey, contentKey);
        HorizontalLayout layout = new HorizontalLayout(button, content);
        layout.setClassName(CLASSNAME);
        setContent(layout);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        setSummaryText(getTranslation(summaryKey));
        content.setText(getTranslation(contentKey));
    }

}
