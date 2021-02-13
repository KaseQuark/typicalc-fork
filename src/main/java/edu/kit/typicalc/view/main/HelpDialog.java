package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
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
    private Accordion content;
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
        content = createHelpContent();
        contentLayout.add(content);
        contentLayout.setId(CONTENT_LAYOUT_ID);
        add(headingLayout, contentLayout);
        setWidth("1000px"); // cannot set width per css
        setHeight("400px"); // choose according to Accordion height
    }
    
    private Accordion createHelpContent() {
        // nur beispielhaft als Vorschlag, wie Hilfe-Dialog aussehen könnte
        Accordion acc = new Accordion();
        HorizontalLayout drawerAccContent = new HorizontalLayout(new DrawerToggle(), 
                new Paragraph(getTranslation("root.helpDrawer")));
        drawerAccContent.setAlignItems(Alignment.CENTER);
        drawerAccContent.setJustifyContentMode(JustifyContentMode.CENTER);
        AccordionPanel drawerAcc = new AccordionPanel("Drawer", drawerAccContent);
        drawerAcc.addThemeVariants(DetailsVariant.FILLED);
        
        HorizontalLayout exampleDialogAccContent = 
                new HorizontalLayout(new Button(getTranslation("root.examplebutton")),
                new Paragraph(getTranslation("root.helpExample")));
        exampleDialogAccContent.setAlignItems(Alignment.CENTER);
        exampleDialogAccContent.setJustifyContentMode(JustifyContentMode.CENTER);
        AccordionPanel exampleAcc = new AccordionPanel("Beispiele", exampleDialogAccContent);
        exampleAcc.addThemeVariants(DetailsVariant.FILLED);
        
        acc.add(drawerAcc);
        acc.add(exampleAcc);
        acc.setWidthFull();
        return acc;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.operatingHelp"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setTextRenderer(renderer);
    }
}
