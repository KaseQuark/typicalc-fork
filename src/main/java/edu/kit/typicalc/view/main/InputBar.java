package edu.kit.typicalc.view.main;

import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Contains components which allow the user to enter a lambda term and start the type inference algorithm.
 */
@CssImport("./styles/view/main/input-bar.css")
public class InputBar extends HorizontalLayout implements LocaleChangeObserver {

    private final Tooltip infoTooltip;
    private final Icon infoIcon;
    private final Button lambdaButton;
    private final ComboBox<String> inputField;
    private final Button inferTypeButton;

    /**
     * Creates an InputBar with a Consumer-object to call the inferType()-method in UpperBar.
     *  The current user input is passed as the methods argument.
     *
     * @param callback Consumer to call the inferType()-method in UpperBar
     */
    protected InputBar(final Consumer<String> callback) {
        infoIcon =  new Icon(VaadinIcon.INFO_CIRCLE);
        // TODO: where is this tooltip supposed to show up?
        infoTooltip = new Tooltip();
        initInfoTooltip();
        infoTooltip.attachToComponent(infoIcon);
        infoTooltip.setPosition(TooltipPosition.BOTTOM);
        infoTooltip.setAlignment(TooltipAlignment.TOP);

        inputField = new ComboBox<>();
        initComboBox();
        lambdaButton = new Button(getTranslation("root.lambda"), event -> onlambdaButtonClick());
        inferTypeButton = new Button(getTranslation("root.typeInfer"), event -> onTypeInferButtonClick(callback));
        inferTypeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(infoIcon, infoTooltip, lambdaButton, inputField, inferTypeButton);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void onTypeInferButtonClick(final Consumer<String> callback) {
        final Optional<String> currentInput = inputField.getOptionalValue();
        currentInput.ifPresentOrElse(callback::accept, () -> callback.accept(StringUtils.EMPTY));
    }

    private void onlambdaButtonClick() {
        final StringBuilder inputBuilder = new StringBuilder();
        final Optional<String> currentInput = inputField.getOptionalValue();
        currentInput.ifPresent(inputBuilder::append);
        inputBuilder.append(getTranslation("root.lambda"));
        inputField.setValue(inputBuilder.toString());
        inputField.focus();
    }

    private void initComboBox() {
	//TODO remove magic string in this method (used for demo)
	inputField.setId("inputField");
	inputField.setClearButtonVisible(true);
	inputField.setAllowCustomValue(true);
	inputField.setItems("λx.x", "λx.λy.y x", "λx.λy.y (x x)", "let f = λx. g y y in f 3", "(λx.x x) (λx.x x)");
	inputField.addCustomValueSetListener(event -> inputField.setValue(event.getDetail()));

	//TODO seems to be the only solution to "immediately" parse backslash
	inputField.addValueChangeListener(event -> {
	   if (inputField.getOptionalValue().isPresent()) {
	       String value = inputField.getValue();
	       value = value.replace("\\", "λ");
	       inputField.setValue(value);
	   }
	});
    }

    private void initInfoTooltip() {
	infoTooltip.add(new H5("Hallo"));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        inferTypeButton.setText(getTranslation("root.typeInfer"));
    }
}
