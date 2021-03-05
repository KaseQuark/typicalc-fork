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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Contains components which allow the user to enter a lambda term and start the type inference algorithm.
 */
@CssImport("./styles/view/main/input-bar.css")
@JsModule("./src/lambda-button-listener.js")
public class InputBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -6099700300418752958L;

    /*
     * IDs for the imported .css-file
     */
    private static final String INPUT_FIELD_ID = "inputField";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String INFER_BUTTON_ID = "inferButton";
    private static final String EXAMPLE_BUTTON_ID = "exampleButton";
    private static final String LAMBDA_BUTTON_ID = "lambdaButton";

    private static final short MAX_INPUT_LENGTH = 1000;

    private final transient Consumer<Pair<String, Map<String, String>>> callback;
    private final TextField inputField;
    private TypeAssumptionsArea typeAssumptionsArea;
    private final Button inferTypeButton;
    private final Button typeAssumptions;

    /**
     * Creates an InputBar with a Consumer-object to call the inferType()-method in UpperBar.
     * The current user input is passed as the methods argument.
     *
     * @param callback Consumer to call the inferType()-method in UpperBar
     */
    protected InputBar(Consumer<Pair<String, Map<String, String>>> callback) {
        this.callback = callback;

        Button infoIcon = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
        infoIcon.addClickListener(event -> onInfoIconClick());

        inputField = new TextField();
        inputField.setPlaceholder(getTranslation("root.inputFieldPlaceholder"));
        inputField.setId(INPUT_FIELD_ID);
        inputField.setClearButtonVisible(true);
        inputField.setMaxLength(MAX_INPUT_LENGTH);

        // attach a listener that replaces \ with λ
        // JavaScript is used because Vaadin does not have APIs for selectionStart/selectionEnd
        UI.getCurrent().getPage().executeJs(
                "document.getElementById('" + INPUT_FIELD_ID + "').addEventListener('keyup', e => {"
                        + "var area = e.target.shadowRoot.querySelector('input');"
                        + "if (area.value.indexOf('\\\\') >= 0) {"
                        + "    var start = area.selectionStart;"
                        + "    var end = area.selectionEnd;"
                        + "    area.value = area.value.replaceAll('\\\\', 'λ');"
                        + "    area.selectionStart = start;"
                        + "    area.selectionEnd = end;"
                        + "}});");
        Button lambdaButton = new Button(getTranslation("root.lambda"));
        lambdaButton.setId(LAMBDA_BUTTON_ID);
        UI.getCurrent().getPage().executeJs("window.lambdaButtonListener($0, $1);", LAMBDA_BUTTON_ID, INPUT_FIELD_ID);
        typeAssumptions = new Button("", event -> onTypeAssumptionsButton());
        typeAssumptionsArea = new TypeAssumptionsArea();
        Button exampleButton = new Button(VaadinIcon.PAPERCLIP.create(), event -> onExampleButtonClick());
        exampleButton.setId(EXAMPLE_BUTTON_ID);
        inferTypeButton = new Button("", event -> onTypeInferButtonClick());
        inferTypeButton.addClickShortcut(Key.ENTER).listenOn(this);
        inferTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        inferTypeButton.setId(INFER_BUTTON_ID);

        add(infoIcon, typeAssumptions, lambdaButton, inputField, exampleButton, inferTypeButton);
        setId(INPUT_BAR_ID);
    }

    /**
     * Sets the provided string as the value of the inputField.
     *
     * @param term the provided string
     */
    protected void setTerm(String term) {
        inputField.setValue(term);
    }

    /**
     * Sets the type assumptions displayed in the type assumptions area.
     *
     * @param typeAssumptions the type assumptions as a map
     */
    protected void setTypeAssumptions(Map<String, String> typeAssumptions) {
        typeAssumptionsArea = new TypeAssumptionsArea(typeAssumptions);
    }

    private void onTypeInferButtonClick() {
        String currentInput = inputField.getOptionalValue().orElse(StringUtils.EMPTY);
        inputField.blur();

        callback.accept(Pair.of(currentInput, typeAssumptionsArea.getTypeAssumptions()));
    }

    private void onTypeAssumptionsButton() {
        typeAssumptionsArea.open();
    }

    private void onExampleButtonClick() {
        Dialog exampleDialog = new ExampleDialog(this::setTerm);
        exampleDialog.open();
    }

    private void onInfoIconClick() {
        Dialog infoDialog = new InfoDialog();
        infoDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        inputField.setPlaceholder(getTranslation("root.inputFieldPlaceholder"));
        inferTypeButton.setText(getTranslation("root.typeInfer"));
        typeAssumptions.setText(getTranslation("root.typeAssumptions"));
    }
}
