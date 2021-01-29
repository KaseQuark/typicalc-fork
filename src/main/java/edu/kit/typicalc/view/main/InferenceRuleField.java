package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

@JsModule("./src/copy-to-clipboard.js")
public class InferenceRuleField extends Div implements LocaleChangeObserver {
    //TODO add css!
    
    private static final long serialVersionUID = -8551851183297707985L;
    
    private static final String INFERENCE_RULE_FIELD_ID = "inferenceRuleField";
    
    private final String nameKey;
    private final Button copyButton;
    private final H5 ruleName;
    private final MathjaxDisplay rule;
    
    public InferenceRuleField(final String latex, final String nameKey) {
        this.nameKey = nameKey;
        this.ruleName = new H5(getTranslation(nameKey));
        this.copyButton = new Button(getTranslation("root.copyLatex"), new Icon(VaadinIcon.CLIPBOARD));
        this.rule = new MathjaxDisplay(latex); //TODO scale, when method implemented
        copyButton.addClickListener(event -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", latex));
        add(ruleName, rule, copyButton);
        setId(INFERENCE_RULE_FIELD_ID);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        copyButton.setText(getTranslation("root.copyLatex"));
        ruleName.setText(getTranslation(nameKey));
    }

}
