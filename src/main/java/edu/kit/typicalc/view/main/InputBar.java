package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Contains components which allow the user to enter a lambda term and start the type inference algorithm.
 */
@CssImport("./styles/view/main/input-bar.css")
public class InputBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -6099700300418752958L;

    /*
     * IDs for the imported .css-file
     */
    private static final String INPUT_FIELD_ID = "inputField";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String INFER_BUTTON_ID = "inferButton";
    private static final String EXAMPLE_BUTTON_ID = "exampleButton";

    private static final short MAX_INPUT_LENGTH = 1000;

    private final TextField inputField;
    private final TypeAssumptionsArea typeAssumptionsArea;
    private final Button inferTypeButton;
    private final Button typeAssumptions;

    /**
     * Creates an InputBar with a Consumer-object to call the inferType()-method in UpperBar.
     * The current user input is passed as the methods argument.
     *
     * @param callback Consumer to call the inferType()-method in UpperBar
     */
    protected InputBar(Consumer<Pair<String, Map<String, String>>> callback) {
        Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
        infoIcon.addClickListener(event -> onInfoIconClick());

        inputField = new TextField();
        inputField.setId(INPUT_FIELD_ID);
        inputField.setClearButtonVisible(true);
        inputField.setMaxLength(1000); // TODO: perhaps remove the error message? more than 1000 can't be entered now
        inputField.setValueChangeMode(ValueChangeMode.EAGER); // TODO: this causes a lot of network traffic
        // attach a listener that replaces \ with λ
        // JavaScript is used because Vaadin does not have APIs for selectionStart/selectionEnd
        // and this will be much faster than a bunch of network round trips per character entered!
        UI.getCurrent().getPage().executeJs(
                "document.getElementById('" + INPUT_FIELD_ID + "').addEventListener('keyup', e => {"
                + "var area = e.target.shadowRoot.querySelector('input');"
                + "if (area.value.indexOf('\\\\') >= 0) {"
                + "    var start = area.selectionStart;"
                + "    var end = area.selectionEnd;"
                + "    area.value = area.value.replace('\\\\', 'λ');"
                + "    area.selectionStart = start;"
                + "    area.selectionEnd = end;"
                + "}});");
        Button lambdaButton = new Button(getTranslation("root.lambda"), event -> onLambdaButtonClick());
        typeAssumptions = new Button(
                getTranslation("root.typeAssumptions"),
                event -> onTypeAssumptionsButton()
        ); // TODO
        typeAssumptionsArea = new TypeAssumptionsArea();
        Button exampleButton = new Button(getTranslation("root.examplebutton"), event -> onExampleButtonClick());
        exampleButton.setId(EXAMPLE_BUTTON_ID);
        inferTypeButton = new Button(getTranslation("root.typeInfer"), event -> onTypeInferButtonClick(callback));
        inferTypeButton.addClickShortcut(Key.ENTER).listenOn(this);
        inferTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        inferTypeButton.setId(INFER_BUTTON_ID);

        add(infoIcon, typeAssumptions, lambdaButton, inputField, exampleButton, inferTypeButton);
        setId(INPUT_BAR_ID);
    }

    /**
     * Sets the provided string as the value of the inputField and starts the type inference algorithm.
     *
     * @param term the provided string
     */
    protected void inferTerm(String term) {
        inputField.setValue(term);
        UI.getCurrent().getPage().executeJs(
                String.format("document.getElementById('%s').click()", INFER_BUTTON_ID));
    }

    private void onTypeInferButtonClick(Consumer<Pair<String, Map<String, String>>> callback) {
        String currentInput = inputField.getOptionalValue().orElse(StringUtils.EMPTY);

        if (currentInput.length() < MAX_INPUT_LENGTH) {
            UI.getCurrent().getPage().setTitle(getTranslation("root.typicalc") + " - " + currentInput);
            callback.accept(Pair.of(currentInput, typeAssumptionsArea.getTypeAssumptions()));
        } else {
            Notification errorNotification = new ErrorNotification(getTranslation("root.overlongInput"));
            errorNotification.open();
        }
    }

    private void onLambdaButtonClick() {
        StringBuilder inputBuilder = new StringBuilder();
        Optional<String> currentInput = inputField.getOptionalValue();
        currentInput.ifPresent(inputBuilder::append);
        inputBuilder.append(getTranslation("root.lambda"));
        inputField.setValue(inputBuilder.toString());
        inputField.focus();
    }

    private void onTypeAssumptionsButton() {
        typeAssumptionsArea.open();
    }

    private void onExampleButtonClick() {
        Consumer<String> setValue = value -> {
            inputField.setValue(value);
            inputField.focus();
        };
        Dialog exampleDialog = new ExampleDialog(setValue);
        exampleDialog.open();
    }

    private void onInfoIconClick() {
        Dialog infoDialog = new InfoDialog();
        infoDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        inferTypeButton.setText(getTranslation("root.typeInfer"));
        typeAssumptions.setText(getTranslation("root.typeAssumptions"));
    }
}
