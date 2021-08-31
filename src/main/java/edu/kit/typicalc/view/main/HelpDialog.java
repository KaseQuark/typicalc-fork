package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private static final String HEADING_LAYOUT_ID = "help-heading-layout";
    private static final String CONTENT_LAYOUT_ID = "help-content-layout";
    private static final String ACCORDION_ID = "accordion";
    private static final String CLOSE_ICON_CLASS = "close-icon";
    private static final String TYPICALC_INFO_ID = "typicalc-info";

    private final H3 heading;
    private final Paragraph typicalcInfo;
    private final Button exampleButton;

    /**
     * Create a new HelpDialog.
     */
    protected HelpDialog() {
        HorizontalLayout headingLayout = new HorizontalLayout();
        heading = new H3();
        headingLayout.setId(HEADING_LAYOUT_ID);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setClassName(CLOSE_ICON_CLASS);

        headingLayout.add(heading, closeIcon);

        exampleButton = InputBar.createExampleButton();
        VerticalLayout contentLayout = new VerticalLayout();
        Accordion content = createHelpContent();
        content.setId(ACCORDION_ID);
        contentLayout.add(content);
        contentLayout.setId(CONTENT_LAYOUT_ID);

        typicalcInfo = new Paragraph();
        typicalcInfo.setId(TYPICALC_INFO_ID);
        contentLayout.add(typicalcInfo);

        add(headingLayout, contentLayout);
    }

    private Accordion createHelpContent() {
        Accordion acc = new Accordion();
        acc.add(new HelpContentField("root.typeInferButton", "root.helpTypeInferButton"));
        acc.add(new HelpContentField("root.inputField", "root.helpInputField"));
        acc.add(new HelpContentField("root.typeAssumptions", "root.helpTypeAssumptions"));
        acc.add(new HelpContentField("root.inferenceViewFeatures", "root.helpInferenceViewFeatures"));
        acc.add(new HelpContentField("root.shortcuts", "root.helpShortcuts"));
        acc.add(new HelpContentField("root.hoverEffects", "root.helpHoverEffects"));
        acc.add(new HelpContentField("root.drawer", "root.helpDrawer"));
        acc.add(new HelpContentField("root.explanationTextsButton", "root.helpExplanationTexts"));
        acc.add(new HelpContentField("", exampleButton, "root.helpExample"));
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
        exampleButton.setText(getTranslation("root.exampleButton"));
        heading.setText(getTranslation("root.operatingHelp"));
        typicalcInfo.getElement().setProperty("innerHTML", getTranslation("help.typicalcInfo"));
    }
}
