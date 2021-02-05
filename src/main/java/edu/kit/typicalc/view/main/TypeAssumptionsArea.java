package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag("tc-type-assumptions")
public class TypeAssumptionsArea extends Dialog implements LocaleChangeObserver {
    private final List<TypeAssumptionField> fields = new ArrayList<>();

    protected TypeAssumptionsArea() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3(getTranslation("root.typeAssumptions")));
        fields.add(new TypeAssumptionField());
        for (Component c : fields) {
            layout.add(c);
        }
        // TODO
    }

    protected Map<String, String> getTypeAssumptions() {
        // TODO
        return Collections.emptyMap();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        // TODO
    }
}
