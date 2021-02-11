package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

/**
 * Represents a single type assumption. Each TypeAssumptionField is displayed in the TypeAssumptionsArea. 
 */
@CssImport("./styles/view/main/type-assumption-field.css")
public class TypeAssumptionField extends HorizontalLayout implements LocaleChangeObserver {

    private static final long serialVersionUID = -81579298585584658L;

    /*
     * IDs for the imported .css-file
     */
    private static final String MINUS_ICON_ID = "minusIcon";
    private static final String ASS_DELETE_BUTTON_ID = "assDeleteButton";
    private static final String ASSUMPTIONS_FIELD_ID = "typeAssumptionField";

    private final TextField variableInputField;
    private final TextField typeInputField;

    /**
     * Creates a new TypeAssumptionField with initial values and a Consumer-object to remove this
     * type assumption from the {@link edu.kit.typicalc.view.main.TypeAssumptionsArea}.
     * 
     * @param deleteSelf deletes this object from the TypeAssumptionsArea
     * @param variable variable of the type assumption
     * @param type type of the type assumption
     */
    protected TypeAssumptionField(Consumer<TypeAssumptionField> deleteSelf, String variable, String type) {
        this(deleteSelf);
        variableInputField.setValue(variable);
        typeInputField.setValue(type);
    }

    /**
     * Creates a new TypeAssumptionField with a Consumer-object to remove this
     * type assumption from the {@link edu.kit.typicalc.view.main.TypeAssumptionsArea}.
     * 
     * @param deleteSelf deletes this object from the TypeAssumptionsArea
     */
    protected TypeAssumptionField(Consumer<TypeAssumptionField> deleteSelf) {
        variableInputField = new TextField();
        variableInputField.setLabel(getTranslation("root.variable"));
        typeInputField = new TextField();
        typeInputField.setLabel(getTranslation("root.type"));
        Icon minusIcon = new Icon(VaadinIcon.TRASH);
        minusIcon.setId(MINUS_ICON_ID);
        Button deleteButton = new Button(minusIcon, event -> deleteSelf.accept(this));
        deleteButton.setId(ASS_DELETE_BUTTON_ID);
        add(variableInputField, typeInputField, deleteButton);
        setId(ASSUMPTIONS_FIELD_ID);
    }

    /**
     * Gets the variable of the type assumption.
     * 
     * @return the variable of the type assumption
     */
    protected String getVariable() {
        return variableInputField.getOptionalValue().orElse(StringUtils.EMPTY);
    }

    /**
     * Gets the type of the type assumption.
     * 
     * @return the type of the type assumption
     */
    protected String getType() {
        return typeInputField.getOptionalValue().orElse(StringUtils.EMPTY);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        variableInputField.setLabel(getTranslation("root.variable"));
        typeInputField.setLabel(getTranslation("root.type"));
    }

}
