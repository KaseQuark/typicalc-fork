package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CssImport("./styles/view/main/type-assumptions-area.css")
public class TypeAssumptionsArea extends Dialog implements LocaleChangeObserver {

    private static final long serialVersionUID = 5809437476076331403L;

    /*
     * IDs for the imported .css-file
     */
    private static final String ASS_LAYOUT_ID = "assLayout";
    private static final String ASS_BUTTONS_ID = "assButtons";
    private static final String ASS_CONTAINER_ID = "assContainer";

    private final H3 heading;
    private final VerticalLayout assumptionContainer;
    private final Button addAssumption;
    private final Button deleteAll;

    private final List<TypeAssumptionField> fields = new ArrayList<>();

    protected TypeAssumptionsArea(Map<String, String> types) {
        heading = new H3(getTranslation("root.typeAssumptions"));

        VerticalLayout layout = new VerticalLayout();
        layout.setId(ASS_LAYOUT_ID);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setId(ASS_BUTTONS_ID);
        addAssumption = new Button(getTranslation("root.addAssumption"), new Icon(VaadinIcon.PLUS_CIRCLE));
        addAssumption.setIconAfterText(true);
        addAssumption.addClickListener(event -> onAddAssumptionClicked());
        deleteAll = new Button(getTranslation("root.deleteAll"), new Icon(VaadinIcon.TRASH));
        deleteAll.addClickListener(event -> onDeleteAllClick());
        deleteAll.setIconAfterText(true);
        deleteAll.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttons.add(addAssumption, deleteAll);

        assumptionContainer = new VerticalLayout();
        assumptionContainer.setId(ASS_CONTAINER_ID);

        for (Map.Entry<String, String> param : types.entrySet()) {
            TypeAssumptionField assumption = new TypeAssumptionField(value -> {
                assumptionContainer.remove(value);
                fields.remove(value);
            }, param.getKey(), param.getValue());
            assumptionContainer.add(assumption);
            fields.add(assumption);
        }

        layout.add(heading, buttons, assumptionContainer);
        layout.setPadding(false);
        add(layout);
    }

    protected TypeAssumptionsArea() {
        this(new HashMap<>());
    }

    private void onAddAssumptionClicked() {
        TypeAssumptionField assumption = new TypeAssumptionField(value -> {
            assumptionContainer.remove(value);
            fields.remove(value);
        });
        assumptionContainer.add(assumption);
        fields.add(assumption);
    }

    private void onDeleteAllClick() {
        assumptionContainer.removeAll();
        fields.clear();
    }

    protected Map<String, String> getTypeAssumptions() {
        return fields.stream()
                .collect(Collectors.toMap(TypeAssumptionField::getVariable, TypeAssumptionField::getType));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.typeAssumptions"));
        addAssumption.setText(getTranslation("root.addAssumption"));
        deleteAll.setText(getTranslation("root.deleteAll"));
    }
}
