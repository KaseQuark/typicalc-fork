package edu.kit.typicalc.view.main;

import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class InputBar extends HorizontalLayout implements LocaleChangeObserver {

    private final Tooltip infoTooltip;
    private final Icon infoIcon;
    private final Button lambdaButton;
    private final ComboBox<String> inputField;
    private final Button inferTypeButton;
    
    protected InputBar(final Consumer<String> callback) {
        infoIcon =  new Icon(VaadinIcon.INFO_CIRCLE);
        infoTooltip = new Tooltip();
        initInfoTooltip();
        infoTooltip.attachToComponent(infoIcon);
        
        lambdaButton = new Button(getTranslation("root.lambda"), event -> onlambdaButtonClick());
        inputField = new ComboBox<>();
        initComboBox();
        inferTypeButton = new Button(getTranslation("root.typeInfer"), event -> onTypeInferButtonClick(callback));
        
        //TODO add components to layout
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
    }
    
    private void initComboBox() {
        // TODO add examples to the combo box here
    }

    private void initInfoTooltip() {
        //TODO add text too info tooltip here
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        inferTypeButton.setText(getTranslation("root.typeInfer"));
    }
}
