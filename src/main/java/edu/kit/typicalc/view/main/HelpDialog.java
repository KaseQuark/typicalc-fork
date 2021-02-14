package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.Locale;

/**
 * Contains information on how to use the application.
 */
@CssImport("./styles/view/main/help-dialog.css")
public class HelpDialog extends Dialog implements LocaleChangeObserver {
    private static final long serialVersionUID = 4470277770276296164L;

    /*
     * IDs for the imported .css-file
     */
    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String CONTENT_LAYOUT_ID = "contentLayout";
    private static final String LANGUAGE_SELECT_ID = "languageSelect";
    private static final String ACCORDION_ID = "accordion";

    private final H3 heading;
    private final Select<Locale> languageSelect;
    private final ItemLabelGenerator<Locale> renderer;

    /**
     * Create a new HelpDialog.
     */
    protected HelpDialog() {
        HorizontalLayout headingLayout = new HorizontalLayout();
        renderer = item -> getTranslation("root." + item.getDisplayLanguage(Locale.ENGLISH).toLowerCase());
        heading = new H3();
        headingLayout.setId(HEADING_LAYOUT_ID);
        languageSelect = new Select<>(Locale.GERMAN, Locale.ENGLISH);
        languageSelect.setTextRenderer(renderer);
        languageSelect.setValue(UI.getCurrent().getLocale());
        languageSelect.addValueChangeListener(event -> UI.getCurrent().setLocale(event.getValue()));
        languageSelect.setId(LANGUAGE_SELECT_ID);
        headingLayout.add(heading, languageSelect);

        VerticalLayout contentLayout = new VerticalLayout();
        Accordion content = createHelpContent();
        content.setId(ACCORDION_ID);
        contentLayout.add(content);
        contentLayout.setId(CONTENT_LAYOUT_ID);
        add(headingLayout, contentLayout);
        // setting size of overlay does not work if all accordion panels are closed
        setWidth("1000px");
        setHeight("600px");
    }

    private Accordion createHelpContent() {
        Accordion acc = new Accordion();
        acc.add(new HelpContentField("root.drawer", new DrawerToggle(), "root.helpDrawer"));
        acc.add(new HelpContentField("root.example",
                new Button(getTranslation("root.examplebutton")), "root.helpExample"));
        acc.add(new HelpContentField("root.inputField", "root.helpInputField"));
        acc.add(new HelpContentField("root.typeAssumptions", "root.helpTypeAssumptions"));
        acc.add(new HelpContentField("root.firstStepButton",
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)), "root.helpFirstStepButton"));
        acc.add(new HelpContentField("root.previousStepButton",
                new Button(new Icon(VaadinIcon.ANGLE_LEFT)), "root.helpPreviousStepButton"));
        acc.add(new HelpContentField("root.nextStepButton",
                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)), "root.helpNextStepButton"));
        acc.add(new HelpContentField("root.lastStepButton",
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)), "root.helpLastStepButton"));
        acc.add(new HelpContentField("root.shareButton",
                new Button(new Icon(VaadinIcon.CONNECT)), "root.helpShareButton"));
        return acc;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.operatingHelp"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setTextRenderer(renderer);
        // TODO update help texts
        // language-switch already works
    }
}
