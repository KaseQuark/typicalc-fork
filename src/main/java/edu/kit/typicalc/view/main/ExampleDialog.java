package edu.kit.typicalc.view.main;

import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Contains all predefined term examples as buttons.
 * Clicking on a button allows the user choose between a number of type assumptions.
 * Selecting the type assumptions inserts the selected term as well as the type assumptions into their
 * respective input fields.
 */
@CssImport("./styles/view/main/example-dialog.css")
public class ExampleDialog extends Dialog implements LocaleChangeObserver {
    private static final long serialVersionUID = -428675165625776559L;
    
    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String EXAMPLE_DIALOG_ID = "exampleDialog";
    private static final String CLOSE_ICON_ID = "closeIcon";
    private static final String EXAMPLE_CONTAINER_ID = "exampleContainer";
    
    private final H3 instruction;
    private final transient Consumer<Pair<String, String>> setExamples;
    private final VerticalLayout container;
    
    private String selectedTerm = null;

    /**
     * Creates a new ExampleDialog with a callback method that will receive the selected example.
     *
     * @param callback function to handle the selected lambda term and type assumptions
     */
    protected ExampleDialog(Consumer<Pair<String, String>> callback) {
        setExamples = callback;
        
        instruction = new H3();
        HorizontalLayout headingLayout = new HorizontalLayout();
        headingLayout.setId(HEADING_LAYOUT_ID);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);

        headingLayout.add(instruction);
        headingLayout.add(closeIcon);
        
        container = new VerticalLayout();
        setTermExamples();
        container.setId(EXAMPLE_CONTAINER_ID);
        
        add(headingLayout, container);
        setId(EXAMPLE_DIALOG_ID);
        setWidth("392px");
        setHeight("661px");
    }
    
    private void setAssumptionExamples(String term) {
        selectedTerm = term;
        container.removeAll();
        container.add(new AssumptionExampleContent(term, this::finishExampleSelection, this::setTermExamples));
        instruction.setText(getTranslation("root.selectAssumptions"));
    }
    
    private void setTermExamples() {
        container.removeAll();
        container.add(new TermExampleContent(this::setAssumptionExamples));
        instruction.setText(getTranslation("root.selectTerm"));
    }
    
    private void finishExampleSelection(String assumptions) {
        setExamples.accept(Pair.of(selectedTerm, assumptions));
        this.close();
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
        instruction.setText(getTranslation("root.selectTerm"));
    }
}
