package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Container for the components displayed in the drawer area of the web page.
 */
@CssImport("./styles/view/main/drawer-content.css")
public class DrawerContent extends VerticalLayout implements LocaleChangeObserver {

    private static final long serialVersionUID = -5751275682270653335L;

    /*
     * IDs for the imported .css-file
     */
    private static final String RULE_CONTAINER_ID = "ruleContainer";
    private static final String DRAWER_CONTENT_ID = "drawerContent";

    private final H3 heading;
    private final VerticalLayout ruleContainer;

    /**
     * Creates a new DrawerContent. 
     */
    protected DrawerContent() {
        heading = new H3(getTranslation("root.inferenceRules"));
        ruleContainer = new VerticalLayout();
        ruleContainer.setId(RULE_CONTAINER_ID);
        initRuleContainer();
        add(heading, ruleContainer);
        setId(DRAWER_CONTENT_ID);
    }

    private void initRuleContainer() {
        //TODO just demo content, exchange with correct rules
        ruleContainer.add(new InferenceRuleField(getTranslation("abs-rule"), "root.absRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("abs-rule"), "root.absRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("abs-rule"), "root.absRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("abs-rule"), "root.absRule"));
        ruleContainer.add(new InferenceRuleField(getTranslation("abs-rule"), "root.absRule"));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.inferenceRules"));
    }
}
