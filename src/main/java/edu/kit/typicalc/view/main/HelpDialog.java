package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.html.Span;
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
    private static final String TYPE_BUTTON_COPY_ID = "typeButtonCopy";
    private static final String CLOSE_ICON_ID = "closeIcon";

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
        languageSelect.addValueChangeListener(event -> UI.getCurrent().getSession().setLocale(event.getValue()));
        languageSelect.setId(LANGUAGE_SELECT_ID);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);

        headingLayout.add(heading, languageSelect, closeIcon);

        VerticalLayout contentLayout = new VerticalLayout();
        Accordion content = createHelpContent();
        content.setId(ACCORDION_ID);
        contentLayout.add(content);
        contentLayout.setId(CONTENT_LAYOUT_ID);
        add(headingLayout, contentLayout);
    }

    private Accordion createHelpContent() {
        Accordion acc = new Accordion();
        acc.add(new HelpContentField("root.typeInferButton", "root.helpTypeInferButton"));
        acc.add(new HelpContentField("root.inputField", "root.helpInputField"));
        acc.add(new HelpContentField("root.typeAssumptions", "root.helpTypeAssumptions"));
        acc.add(new HelpContentField("root.shortcuts", createShortcutComponent()));
        acc.add(new HelpContentField("root.drawer",
                new Button(new Icon(VaadinIcon.MENU)), "root.helpDrawer"));
        acc.add(new HelpContentField("root.example",
                new Button(new Icon(VaadinIcon.PAPERCLIP)), "root.helpExample"));
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

    private Component createShortcutComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Div(
                new Span(getTranslation("root.shortcut0")),
                new Pre(getTranslation("root.shortcut0key"))));
        layout.add(new Div(
                new Span(getTranslation("root.shortcut1")),
                new Pre(getTranslation("root.shortcut1key"))));
        layout.add(new Div(
                new Span(getTranslation("root.shortcut2")),
                new Pre(getTranslation("root.shortcut2key"))));
        layout.add(new Div(
                new Span(getTranslation("root.shortcut3")),
                new Pre(getTranslation("root.shortcut3key"))));
        layout.add(new Div(
                new Span(getTranslation("root.shortcut4")),
                new Pre(getTranslation("root.shortcut4key"))));
        // TODO: localeChange, English translation texts
        return layout;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.operatingHelp"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setTextRenderer(renderer);
    }
}
