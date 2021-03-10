package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Type inference rules overview.
 */
@CssImport("./styles/view/main/type-inference-rules.css")
@Tag("div")
public class TypeInferenceRules extends Component implements LocaleChangeObserver, HasComponents {

    private static final long serialVersionUID = -5751275682270653335L;

    /*
     * IDs for the imported .css-file
     */
    private static final String ID = "type-inference-rules";
    private static final String RULE_CONTAINER_ID = "rule-container";

    private final H3 heading;
    private final VerticalLayout ruleContainer;

    /**
     * Initialize the content of this container.
     */
    public TypeInferenceRules() {
        setId(ID);
        heading = new H3();
        ruleContainer = new VerticalLayout();
        ruleContainer.setId(RULE_CONTAINER_ID);
        add(heading, ruleContainer);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.inferenceRules"));
        ruleContainer.removeAll();
        ruleContainer.add(new InferenceRuleField(getTranslation("root.absLatex"), "root.absRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.appLatex"), "root.appRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.varLatex"), "root.varRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.constLatex"), "root.constRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.letLatex"), "root.letRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.absLetLatex"), "root.absRuleLet"));
        ruleContainer.add(new InferenceRuleField(getTranslation("root.varLetLatex"), "root.varRuleLet"));
    }
}
