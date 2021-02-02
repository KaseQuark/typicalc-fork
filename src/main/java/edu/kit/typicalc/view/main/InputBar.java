package edu.kit.typicalc.view.main;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.flow.component.textfield.TextField;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

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

    private final Icon infoIcon;
    private final Button exampleButton;
    private final Button lambdaButton;
    private final TextField inputField;
    private final Button inferTypeButton;

    /**
     * Creates an InputBar with a Consumer-object to call the inferType()-method in UpperBar.
     * The current user input is passed as the methods argument.
     *
     * @param callback Consumer to call the inferType()-method in UpperBar
     */
    protected InputBar(final Consumer<String> callback) {
        infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
        infoIcon.addClickListener(event -> onInfoIconClick());

        inputField = new TextField();
        inputField.setId(INPUT_FIELD_ID);
        inputField.setClearButtonVisible(true);
        inputField.addValueChangeListener(event -> onInputFieldValueChange());
        lambdaButton = new Button(getTranslation("root.lambda"), event -> onlambdaButtonClick());
        exampleButton = new Button(getTranslation("root.examplebutton"), event -> onExampleButtonClick());
        exampleButton.setId(EXAMPLE_BUTTON_ID);
        inferTypeButton = new Button(getTranslation("root.typeInfer"), event -> onTypeInferButtonClick(callback));
        inferTypeButton.addClickShortcut(Key.ENTER).listenOn(this);
        inferTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        inferTypeButton.setId(INFER_BUTTON_ID);

        add(infoIcon, exampleButton, lambdaButton, inputField, inferTypeButton);
        setId(INPUT_BAR_ID);
    }

    protected void reset() {
        inputField.clear();
    }

    private void onInputFieldValueChange() {
        inputField.getOptionalValue().ifPresent(value -> inputField
                .setValue(value.replace("\\", getTranslation("root.lambda"))));
    }

    private void onTypeInferButtonClick(final Consumer<String> callback) {
        final String currentInput = inputField.getOptionalValue().orElse(StringUtils.EMPTY);

        if (currentInput.length() < MAX_INPUT_LENGTH) {
            callback.accept(currentInput);
        } else {
            final Notification errorNotification = new ErrorNotification(getTranslation("root.overlongInput"));
            errorNotification.open();
        }
    }

    private void onlambdaButtonClick() {
        final StringBuilder inputBuilder = new StringBuilder();
        final Optional<String> currentInput = inputField.getOptionalValue();
        currentInput.ifPresent(inputBuilder::append);
        inputBuilder.append(getTranslation("root.lambda"));
        inputField.setValue(inputBuilder.toString());
        inputField.focus();
    }

    private void onExampleButtonClick() {
        final Dialog exampleDialog = new ExampleDialog(inputField::setValue);
        exampleDialog.open();
    }

    private void onInfoIconClick() {
        final Dialog infoDialog = new InfoDialog();
        infoDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        inferTypeButton.setText(getTranslation("root.typeInfer"));
    }
}
