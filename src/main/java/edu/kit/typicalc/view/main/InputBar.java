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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Contains components which allow the user to enter a lambda term and start the type inference algorithm.
 */
@CssImport("./styles/view/main/input-bar.css")
@JsModule("./src/input-bar-enhancements.ts")
public class InputBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -6099700300418752958L;

    /*
     * IDs for the imported .css-file
     */
    public static final String TERM_INPUT_FIELD_ID = "term-input-field";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String INFER_BUTTON_ID = "inferButton";
    private static final String EXAMPLE_BUTTON_ID = "exampleButton";
    private static final String LAMBDA_BUTTON_ID = "lambdaButton";
    private static final String QUANTIFIER_BUTTON_ID = "quantifier-button";
    private static final String ASS_INPUT_FIELD_ID = "ass-input-field";

    private static final short MAX_INPUT_LENGTH = 1000;

    private final transient Consumer<Pair<String, Map<String, String>>> callback;
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
    protected InputBar(Consumer<Pair<String, Map<String, String>>> callback) {
        this.callback = callback;

        setId(INPUT_BAR_ID);

        infoIcon = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        infoIcon.addClickListener(event -> onInfoIconClick());

        termInputField = new TextField();
        termInputField.getElement().setAttribute("autofocus", ""); // focus on page load
        termInputField.setId(TERM_INPUT_FIELD_ID);
        termInputField.setClearButtonVisible(true);
        termInputField.setMaxLength(MAX_INPUT_LENGTH);

        // attach a listener that replaces \ with λ
        // JavaScript is used because this is a latency-sensitive operation
        // (and Vaadin does not have APIs for selectionStart/selectionEnd)
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
     * Sets the provided string as the value of the inputField.
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
    protected void setTypeAssumptions(Map<String, String> typeAssumptions) {
        assumptionInputField.setValue(
                typeAssumptions.entrySet().stream()
                        .map(entry -> entry.getKey().trim() + ": " + entry.getValue().trim())
                        .collect(Collectors.joining("; ")));
    }

    protected void setTermAndClickTypeInfer(String term) {
        setTerm(term);
        onTypeInferButtonClick();
    }

    private void onTypeInferButtonClick() {
        termInputField.blur();
        String assumptions = assumptionInputField.getValue();
        Map<String, String> assumptionsMap = Arrays.stream(assumptions.split(";"))
                .filter(entry -> entry.length() > 0).map(entry -> {
            if (entry.contains(":")) {
                String[] parts = entry.split(":", 2);
                return Pair.of(parts[0].trim(), parts[1].trim());
            } else {
                return Pair.of(entry, "");
            }
        }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight,
                (existing, replacement) -> existing, LinkedHashMap::new));
        callback.accept(Pair.of(termInputField.getValue(), assumptionsMap));
    }

    private void onExampleButtonClick() {
        Dialog exampleDialog = new ExampleDialog(this::setTermAndClickTypeInfer);
        exampleDialog.open();
    }

    private void onInfoIconClick() {
        Dialog infoDialog = new InfoDialog();
        infoDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        termInputField.setPlaceholder(getTranslation("root.inputFieldPlaceholder"));
        termInputField.setLabel(getTranslation("root.term"));
        exampleButton.setText(getTranslation("root.exampleButton"));
        inferTypeButton.setText(getTranslation("root.typeInfer"));
        infoIcon.getElement().setAttribute("title", getTranslation("root.inputSyntax"));
        exampleButton.getElement().setAttribute("title", getTranslation("root.exampleTooltip"));
        assumptionInputField.setPlaceholder("TODO"); // TODO replace with usefull placeholder
        assumptionInputField.setLabel(getTranslation("root.typeAssumptions"));
    }
}
