package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

/**
 * Contains components which allow the user to enter a lambda term and start the type inference algorithm.
 */
@CssImport("./styles/view/main/input-bar.css")
@JsModule("./src/input-bar-enhancements.ts")
@CssImport(value = "./styles/view/main/vaadin-text-field.css", themeFor = "vaadin-text-field")
public class InputBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -6099700300418752958L;

    /*
     * IDs for the imported .css-file
     */
    public static final String TERM_INPUT_FIELD_ID = "term-input-field";
    private static final String INFER_BUTTON_ID = "infer-button";
    private static final String EXAMPLE_BUTTON_ID = "example-button";
    private static final String LAMBDA_BUTTON_ID = "lambda-button";
    private static final String QUANTIFIER_BUTTON_ID = "quantifier-button";
    public static final String ASS_INPUT_FIELD_ID = "ass-input-field";

    private static final short MAX_INPUT_LENGTH = 1000;

    private final transient Consumer<Pair<String, String>> callback;
    private final Button infoIcon;
    private final TextField termInputField;
    private final AssumptionInputField assumptionInputField;
    private final Button exampleButton;
    private final Button inferTypeButton;

    /**
     * Creates an InputBar with a Consumer-object to call the inferType()-method in UpperBar.
     * The current user input is passed as the methods argument.
     *
     * @param callback Consumer to call the inferType()-method in UpperBar
     */
    protected InputBar(Consumer<Pair<String, String>> callback) {
        this.callback = callback;

        infoIcon = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        infoIcon.addClickListener(event -> onInfoIconClick());

        termInputField = new TextField();
        termInputField.getElement().setAttribute("autofocus", ""); // focus on page load
        termInputField.setId(TERM_INPUT_FIELD_ID);
        termInputField.setClearButtonVisible(true);
        termInputField.setMaxLength(MAX_INPUT_LENGTH);

        UI.getCurrent().getPage().executeJs("window.characterListener($0);", TERM_INPUT_FIELD_ID);
        Button lambdaButton = new Button(getTranslation("root.lambda"));
        lambdaButton.setId(LAMBDA_BUTTON_ID);
        UI.getCurrent().getPage().executeJs("window.buttonListener($0, $1);", LAMBDA_BUTTON_ID, TERM_INPUT_FIELD_ID);

        Button allQuantifierButton = new Button(getTranslation("root.allQuantifier"));
        allQuantifierButton.setId(QUANTIFIER_BUTTON_ID);
        UI.getCurrent().getPage().executeJs("window.buttonListener($0, $1);", QUANTIFIER_BUTTON_ID, ASS_INPUT_FIELD_ID);

        assumptionInputField = new AssumptionInputField();
        setupAssumptionsField();

        exampleButton = createExampleButton();
        exampleButton.addClickListener(event -> onExampleButtonClick());
        inferTypeButton = new Button("", event -> onTypeInferButtonClick());
        inferTypeButton.addClickShortcut(Key.ENTER).listenOn(this);
        inferTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        inferTypeButton.setId(INFER_BUTTON_ID);

        add(infoIcon, allQuantifierButton, assumptionInputField, lambdaButton, termInputField, exampleButton,
                inferTypeButton);
    }

    private void setupAssumptionsField() {
        assumptionInputField.setClearButtonVisible(true);
        UI.getCurrent().getPage().executeJs("window.characterListener($0);", ASS_INPUT_FIELD_ID);
        assumptionInputField.setId(ASS_INPUT_FIELD_ID);
    }

    public static Button createExampleButton() {
        Button button = new Button(UI.getCurrent().getTranslation("root.exampleButton"));
        button.setId(EXAMPLE_BUTTON_ID);
        return button;
    }

    /**
     * Sets the provided string as the value of the termInputField.
     *
     * @param term the provided string
     */
    protected void setTerm(String term) {
        termInputField.setValue(term);
    }

    /**
     * Sets the type assumptions displayed in the type assumptions area.
     *
     * @param typeAssumptions the type assumptions as a map
     */
    protected void setTypeAssumptions(String typeAssumptions) {
        assumptionInputField.setValue(typeAssumptions);
    }

    /**
     * Set to provided input as the value of the termInputField and assumptionInputField.
     *
     * @param input pair of a term (left) and type assumptions (right)
     */
    protected void setInputAndClickTypeInfer(Pair<String, String> input) {
        setTerm(input.getLeft());
        assumptionInputField.setValue(input.getRight());
        onTypeInferButtonClick();
    }

    private void onTypeInferButtonClick() {
        termInputField.blur();
        String assumptions = assumptionInputField.getValue();
        callback.accept(Pair.of(termInputField.getValue(), assumptions));
    }

    private void onExampleButtonClick() {
        Dialog exampleDialog = new ExampleDialog(this::setInputAndClickTypeInfer);
        exampleDialog.open();
    }

    private void onInfoIconClick() {
        Dialog infoDialog = new SyntaxDialog();
        infoDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        termInputField.setPlaceholder(getTranslation("root.inputFieldPlaceholder"));
        termInputField.setLabel(getTranslation("root.termLabel"));
        exampleButton.setText(getTranslation("root.exampleButton"));
        inferTypeButton.setText(getTranslation("root.typeInfer"));
        infoIcon.setText(getTranslation("root.inputSyntaxShort"));
        infoIcon.getElement().setAttribute("title", getTranslation("root.inputSyntax"));
        exampleButton.getElement().setAttribute("title", getTranslation("root.exampleTooltip"));
        assumptionInputField.setPlaceholder(getTranslation("root.typeInputFieldPlaceholder"));
        assumptionInputField.setLabel(getTranslation("root.typeAssumptions"));
    }
}
