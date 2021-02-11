package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Contains all predefined examples as buttons.
 * Clicking on a button inserts the example string into the input field.
 *
 */
public class ExampleDialog extends Dialog implements LocaleChangeObserver {

    private static final long serialVersionUID = 8718432784530464215L;

    private static final String EXAMPLE_DIALOG_ID = "exampleDialog";

    private static final List<String> EXAMPLES =
            List.of("λx.x", "λx.λy.y x", "λx.λy.y (x x)", "let f = λx. g y y in f 3", "(λx.x x) (λx.x x)",
                    "(λx.λy.y (x y)) (λz. λa. z g a)", "let f = λx. let g = λy. y in g x in f 3",
                    "let f = λx. let g = λy.5 5 in g x in f 3");
    private final H3 instruction;

    /**
     * Creates a new ExampleDialog with a callback method that will receive the selected example.
     *
     * @param callback function to handle the selected lambda term
     */
    protected ExampleDialog(Consumer<String> callback) {
        VerticalLayout layout = new VerticalLayout();
        instruction = new H3();
        layout.add(instruction);
        for (String term : EXAMPLES) {
            Button button = new Button(term);
            button.addClickListener(click -> {
        	callback.accept(term);
        	this.close();
            });
            button.setId(term); // needed for integration test
            layout.add(button);
        }
        add(layout);
        setId(EXAMPLE_DIALOG_ID);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        instruction.setText(getTranslation("root.selectExample"));
    }
}
