package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import edu.kit.typicalc.model.parser.TypeAssumptionParser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a single type assumption input component.
 * Each TypeAssumptionField is displayed in the TypeAssumptionsArea.
 */
@JsModule("./src/type-input-listener.js")
@CssImport("./styles/view/main/type-assumption-field.css")
@CssImport(value = "./styles/view/main/type-assumption-input-field.css", themeFor = "vaadin-text-field")
public class TypeAssumptionField extends HorizontalLayout implements LocaleChangeObserver {

    private static final long serialVersionUID = -81579298585584658L;

    /*
     * IDs for the imported .css-file
     */
    private static final String MINUS_ICON_ID = "minusIcon";
    private static final String ASS_DELETE_BUTTON_ID = "assDeleteButton";
    private static final String ASSUMPTIONS_FIELD_ID = "typeAssumptionField";

    private static final String TYPE_FIELD_CLASS = "typeFieldClass";

    /*
     * Unicode of subscripted digits in increasing order
     */
    private static final List<Character> SUBSCRIPTED_DIGITS =
            List.of('\u2080', '\u2081', '\u2082', '\u2083', '\u2084',
                    '\u2085', '\u2086', '\u2087', '\u2088', '\u2089');
    private static final char TAU = '\u03C4';

    private final TextField variableInputField;
    private final TextField typeInputField;

    /**
     * Creates a new TypeAssumptionField with initial values and a callback to remove this
     * type assumption from the {@link TypeAssumptionsArea}.
     *
     * @param deleteSelf deletes this object from the TypeAssumptionsArea
     * @param variable   variable of the type assumption
     * @param type       type of the type assumption
     */
    protected TypeAssumptionField(Consumer<TypeAssumptionField> deleteSelf, String variable, String type) {
        this(deleteSelf);
        variableInputField.setValue(variable);
        typeInputField.setValue(type);
    }

    /**
     * Creates a new TypeAssumptionField with a Consumer-object to remove this
     * type assumption from the {@link TypeAssumptionsArea}.
     *
     * @param deleteSelf deletes this object from the TypeAssumptionsArea
     */
    protected TypeAssumptionField(Consumer<TypeAssumptionField> deleteSelf) {
        variableInputField = new TextField();
        variableInputField.getElement().setAttribute(
                "pattern", TypeAssumptionParser.TYPE_NAME_PATTERN.pattern());
        typeInputField = new TextField();
        typeInputField.setClassName(TYPE_FIELD_CLASS);
        UI.getCurrent().getPage().executeJs("window.addTypeInputListener($0)", TYPE_FIELD_CLASS);
        Icon minusIcon = new Icon(VaadinIcon.TRASH);
        minusIcon.setId(MINUS_ICON_ID);
        Button deleteButton = new Button(minusIcon, event -> deleteSelf.accept(this));
        deleteButton.setId(ASS_DELETE_BUTTON_ID);
        deleteButton.setTabIndex(-1);
        add(variableInputField, typeInputField, deleteButton);
        setId(ASSUMPTIONS_FIELD_ID);
    }

    private String parseBackType(String type) {
        String rawType = type.replace(TAU, 't');
        char[] rawTypeArray = rawType.toCharArray();
        for (int i = 0; i < rawTypeArray.length; i++) {
            if (SUBSCRIPTED_DIGITS.contains(rawTypeArray[i])) {
                rawTypeArray[i] = Character.forDigit(SUBSCRIPTED_DIGITS.indexOf(rawTypeArray[i]), 10);
            }
        }
        return new String(rawTypeArray);
    }

    /**
     * This method is called when the dialog containing this field is reopened. Since Vaadin somehow detaches the
     * event listener, it has to be added again.
     */
    protected void refresh() {
        UI.getCurrent().getPage().executeJs("window.addTypeInputListener($0)", TYPE_FIELD_CLASS);
        typeInputField.focus();
        focus();
    }

    /**
     * Focus the variable field of this element.
     */
    protected void focus() {
        variableInputField.focus();
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
        return parseBackType(typeInputField.getOptionalValue().orElse(StringUtils.EMPTY));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        variableInputField.setLabel(getTranslation("root.variable"));
        typeInputField.setLabel(getTranslation("root.type"));
    }

}
