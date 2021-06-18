package edu.kit.typicalc.view.main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.textfield.TextField;

@JsModule("./src/type-input-listener.ts")
public class AssumptionInputField extends TextField {
    private static final long serialVersionUID = -3887662656039679338L;
    
    private static final String ASSUMPTION_INPUT_FIELD_CLASS = "assumption-input-field";
    
    /*
     * Unicode of subscripted digits in increasing order
     */
    private static final List<Character> SUBSCRIPTED_DIGITS =
            List.of('\u2080', '\u2081', '\u2082', '\u2083', '\u2084',
                    '\u2085', '\u2086', '\u2087', '\u2088', '\u2089');
    private static final char TAU = '\u03C4';

    protected AssumptionInputField() {
        setClassName(ASSUMPTION_INPUT_FIELD_CLASS);
        UI.getCurrent().getPage().executeJs("window.addTypeInputListener($0)", ASSUMPTION_INPUT_FIELD_CLASS);
    }
    
    protected List<String> getTypeAssumptions() {
        return Arrays.stream(parseBack(getOptionalValue().orElse(StringUtils.EMPTY)).split(";"))
                .collect(Collectors.toList());
    }
    
    private String parseBack(String value) {
        String rawValue = value.replace(TAU, 't');
        char[] rawValueArray = rawValue.toCharArray();
        for (int i = 0; i < rawValueArray.length; i++) {
            if (SUBSCRIPTED_DIGITS.contains(rawValueArray[i])) {
                rawValueArray[i] = Character.forDigit(SUBSCRIPTED_DIGITS.indexOf(rawValueArray[i]), 10);
            }
        }
        return new String(rawValueArray);
    }
}
