package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class HelpContentField extends AccordionPanel implements LocaleChangeObserver {
    
    private static final long serialVersionUID = -2793005857762897734L;
        
    private final String summaryKey;
    private final String contentKey;
    private final Paragraph content;
    
    protected HelpContentField(String summaryKey, String contentKey) {
        this.summaryKey = summaryKey;
        this.contentKey = contentKey;
        this.content = new Paragraph(getTranslation(contentKey));
        setSummaryText(getTranslation(summaryKey));
        setContent(content);
        addThemeVariants(DetailsVariant.FILLED);
    }
    
    protected HelpContentField(String summaryKey, Button button, String contentKey) {
        this(summaryKey, contentKey);
        HorizontalLayout layout = new HorizontalLayout(button, content);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        setContent(layout);
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
       setSummaryText(getTranslation(summaryKey));
       content.setText(getTranslation(contentKey));
    }

}
