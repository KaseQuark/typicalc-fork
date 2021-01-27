package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

@JsModule(".src/copy-to-clipboard.js")
public class InferenceRuleField extends Div implements LocaleChangeObserver {
    
    private final String latex;
    private final String nameKey;
    private final Button copyButton;
    
    public InferenceRuleField(final String latex, final String nameKey) {
        this.latex = latex;
        this.nameKey = nameKey;
        this.copyButton = new Button(getTranslation("root.copyLatex"), 
                event -> UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", latex));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        copyButton.setText(getTranslation("root.copyLatex"));
        //TODO use nameKey to change name of rule, when attribute is created
    }

}
