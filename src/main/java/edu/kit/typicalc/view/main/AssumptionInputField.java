package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Textfield which allows the user to enter an arbitrary number of type assumptions.
 */
@JsModule("./src/type-input-listener.ts")
public class AssumptionInputField extends TextField {
    private static final long serialVersionUID = -3887662656039679338L;
    
    private static final String ASSUMPTION_INPUT_FIELD_CLASS = "assumption-input-field";

    /**
     * Creates an AssumptionInputField.
     */
    protected AssumptionInputField() {
        setClassName(ASSUMPTION_INPUT_FIELD_CLASS);
        UI.getCurrent().getPage().executeJs("window.addTypeInputListener($0)", ASSUMPTION_INPUT_FIELD_CLASS);
    }
    
}
