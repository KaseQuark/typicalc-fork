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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Dialog which allows the user to create and delete type assumptions.
 * The current type assumptions are stored after closing the dialog.
 */
@CssImport("./styles/view/main/type-assumptions-area.css")
public class TypeAssumptionsArea extends Dialog implements LocaleChangeObserver {

    private static final long serialVersionUID = 5809437476076331403L;

    /*
     * IDs for the imported .css-file
     */
    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String ASS_LAYOUT_ID = "assLayout";
    private static final String ASS_BUTTONS_ID = "assButtons";
    private static final String ASS_CONTAINER_ID = "assContainer";
    private static final String CLOSE_ICON_ID = "closeIcon";

    private final H3 heading;
    private final VerticalLayout assumptionContainer;
    private final Button addAssumption;
    private final Button deleteAll;
    private final Button saveAssumptions;

    private final List<TypeAssumptionField> fields = new ArrayList<>();

    /**
     * Creates a new TypeAssumptionsArea with initial type assumptions.
     *
     * @param types map containing the values for the initial type assumptions
     */
    protected TypeAssumptionsArea(Map<String, String> types) {
        heading = new H3("");

        VerticalLayout layout = new VerticalLayout();
        layout.setId(ASS_LAYOUT_ID);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setId(ASS_BUTTONS_ID);
        addAssumption = new Button("", new Icon(VaadinIcon.PLUS_CIRCLE));
        addAssumption.setIconAfterText(true);
        addAssumption.addClickListener(event -> onAddAssumptionClicked());
        deleteAll = new Button("", new Icon(VaadinIcon.TRASH));
        deleteAll.addClickListener(event -> onDeleteAllClick());
        deleteAll.setIconAfterText(true);
        deleteAll.addThemeVariants(ButtonVariant.LUMO_ERROR);
        saveAssumptions = new Button(getTranslation("root.save"), event -> this.close());
        saveAssumptions.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        buttons.add(addAssumption, deleteAll, saveAssumptions);

        assumptionContainer = new VerticalLayout();
        assumptionContainer.setId(ASS_CONTAINER_ID);

        initializeWithAssumptions(types);
        layout.add(buttons, assumptionContainer);
        HorizontalLayout headingLayout = makeHeader();
        add(headingLayout, layout);
        // attach and trigger javascript event listener after reopening the dialog
        addOpenedChangeListener(e -> {
           if (e.isOpened()) { 
                   fields.forEach(TypeAssumptionField::refresh); 
               } 
           } 
        );
    }

    private HorizontalLayout makeHeader() {
        HorizontalLayout headingLayout = new HorizontalLayout();
        headingLayout.setId(HEADING_LAYOUT_ID);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);
        headingLayout.add(heading);
        headingLayout.add(closeIcon);
        return headingLayout;
    }
    /**
     * Creates a new empty TypeAssumptionsArea.
     */
    protected TypeAssumptionsArea() {
        this(new HashMap<>());
    }

    private void initializeWithAssumptions(Map<String, String> types) {
        for (Map.Entry<String, String> param : types.entrySet()) {
            TypeAssumptionField assumption = new TypeAssumptionField(value -> {
                assumptionContainer.remove(value);
                fields.remove(value);
            }, param.getKey(), param.getValue());
            assumptionContainer.add(assumption);
            fields.add(assumption);
        }
    }

    private void onAddAssumptionClicked() {
        TypeAssumptionField assumption = new TypeAssumptionField(value -> {
            assumptionContainer.remove(value);
            fields.remove(value);
        });
        assumptionContainer.add(assumption);
        fields.add(assumption);
        assumption.focus();
    }

    private void onDeleteAllClick() {
        assumptionContainer.removeAll();
        fields.clear();
    }

    /**
     * Returns the current type assumptions.
     * If multiple type assumptions define the same variable only the first type assumption is returned.
     * The map is sorted by the variable names.
     *
     * @return the current type assumptions as mappings from a variable to a type
     */
    protected Map<String, String> getTypeAssumptions() {
        return fields.stream()
                .map(field -> Pair.of(field.getVariable(), field.getType()))
                .filter(pair -> !pair.getLeft().isEmpty()) // ignore empty input fields
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight,
                        (existing, replacement) -> existing, TreeMap::new));
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        heading.setText(getTranslation("root.typeAssumptions"));
        addAssumption.setText(getTranslation("root.addAssumption"));
        deleteAll.setText(getTranslation("root.deleteAll"));
        saveAssumptions.setText(getTranslation("root.save"));
    }
}
