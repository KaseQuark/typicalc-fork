package edu.kit.typicalc.view.main;

import java.util.Locale;

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
    private final Select<String> languageSelect;

    /**
     * Create a new HelpDialog.
     */
    protected HelpDialog() {
        final HorizontalLayout headingLayout = new HorizontalLayout();
        heading = new H3(getTranslation("root.operatingHelp"));
        headingLayout.setId(HEADING_LAYOUT_ID);
        languageSelect = new Select<>(getTranslation("root.german"), getTranslation("root.english"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setValue(getTranslation("root." 
                + UI.getCurrent().getLocale().getDisplayLanguage(Locale.ENGLISH).toLowerCase()));
        languageSelect.addValueChangeListener(event -> onLaguageChange());
        languageSelect.setId(LANGUAGE_SELECT_ID);
        headingLayout.add(heading, languageSelect);

        final VerticalLayout contentLayout = new VerticalLayout();
        //TODO inputSyntax wird im inputDialog behandelt --> hier anderer Content
        inputSyntax = new H5(getTranslation("root.inputSyntax"));
        contentLayout.setId(CONTENT_LAYOUT_ID);
        contentLayout.add(inputSyntax);
        add(headingLayout, contentLayout);
    }

    private void onLaguageChange() {
        Locale newLocale = Locale.getDefault();
        if (languageSelect.getValue().equals(getTranslation("root.german"))) {
            newLocale = Locale.GERMAN;
        } else if (languageSelect.getValue().equals(getTranslation("root.english"))) {
            newLocale = Locale.ENGLISH;
        }
        UI.getCurrent().setLocale(newLocale);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.operatingHelp"));
        inputSyntax.setText(getTranslation("root.inputSyntax"));
    }
}
