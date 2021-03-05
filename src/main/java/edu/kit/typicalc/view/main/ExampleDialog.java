package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.function.Consumer;

/**
 * Contains all predefined examples as buttons.
 * Clicking on a button inserts the example string into the input field.
 */
@CssImport("./styles/view/main/example-dialog.css")
public class ExampleDialog extends Dialog implements LocaleChangeObserver {

    private static final long serialVersionUID = 8718432784530464215L;

    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String EXAMPLE_DIALOG_ID = "exampleDialog";
    private static final String CLOSE_ICON_ID = "closeIcon";

    private final H3 instruction;

    /**
     * Creates a new ExampleDialog with a callback method that will receive the selected example.
     *
     * @param callback function to handle the selected lambda term
     */
    protected ExampleDialog(Consumer<String> callback) {
        instruction = new H3();
        HorizontalLayout headingLayout = new HorizontalLayout();
        headingLayout.setId(HEADING_LAYOUT_ID);

        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);

        headingLayout.add(instruction);
        headingLayout.add(closeIcon);

        String[] examples = getTranslation("root.exampleTerms").split(",");
        VerticalLayout layout = new VerticalLayout();
        for (String term : examples) {
            Button button = new Button(term);
            button.addClickListener(click -> {
                callback.accept(term);
                this.close();
            });
            button.setId(term); // needed for integration test
            layout.add(button);
        }
        add(headingLayout, layout);
        setId(EXAMPLE_DIALOG_ID);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        instruction.setText(getTranslation("root.selectExample"));
    }
}
