package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Visual representation of an inference rule. The component is composed of the rule itself (displayed
 * in LaTeX rendered by MathJax), the name of the rule and a button to copy the LaTeX-code to the 
 * clipboard. Each InferenceRuleField is displayed in drawer area of the web page.
 */
@CssImport("./styles/view/main/inference-rule-field.css")
@JsModule("./src/copy-to-clipboard.js")
public class InferenceRuleField extends VerticalLayout implements LocaleChangeObserver {
    
    private static final long serialVersionUID = -8551851183297707985L;
    
    /*
     * IDs for the imported .css-file
     */
    private static final String INFERENCE_RULE_FIELD_ID = "inferenceRuleField";
    private static final String HEADER_ID = "headerField";
    private static final String MAIN_ID = "main";
    private static final String RULE_NAME_ID = "ruleName";
    
    private final String nameKey;
    private final Button copyButton;
    private final H4 ruleName;
    private final MathjaxDisplay rule;
    
    /**
     * Initializes an InferenceRuleField with a key to get the name of the inference rule and the LaTeX-code
     * for its visual representation.
     * 
     * @param latex the LaTeX-code
     * @param nameKey the key to get the name of the inference rule
     */
    protected InferenceRuleField(final String latex, final String nameKey) {
        this.nameKey = nameKey;
        
        final HorizontalLayout header = new HorizontalLayout();
        header.setId(HEADER_ID);
        this.ruleName = new H4(getTranslation(nameKey));
        ruleName.setId(RULE_NAME_ID);
        header.add(ruleName);
        
        final VerticalLayout main = new VerticalLayout();
        main.setId(MAIN_ID);
        this.copyButton = new Button(getTranslation("root.copyLatex"), new Icon(VaadinIcon.CLIPBOARD));
        this.rule = new MathjaxDisplay(latex); //TODO scale, when method implemented
        copyButton.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", latex));
        main.add(rule, copyButton);
        add(header, main);
        setId(INFERENCE_RULE_FIELD_ID);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        copyButton.setText(getTranslation("root.copyLatex"));
        ruleName.setText(getTranslation(nameKey));
    }

}
