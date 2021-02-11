package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
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

    private final H3 heading;
    private final H5 inputSyntax;
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
        //TODO inputSyntax wird im inputDialog behandelt --> hier anderer Content
        inputSyntax = new H5();
        contentLayout.setId(CONTENT_LAYOUT_ID);
        contentLayout.add(inputSyntax);
        add(headingLayout, contentLayout);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.operatingHelp"));
        inputSyntax.setText(getTranslation("root.inputSyntax"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setTextRenderer(renderer);
    }
}
