package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.List;
import java.util.function.Consumer;

/**
 * Contains all predefined examples as buttons. Clicking on a button inserts the example string into
 * the input bar.
 *
 */
public class ExampleDialog extends Dialog implements LocaleChangeObserver {
    
    private static final long serialVersionUID = 8718432784530464215L;
    
    private static final List<String> EXAMPLES =
            List.of("λx.x", "λx.λy.y x", "λx.λy.y (x x)", "let f = λx. g y y in f 3", "(λx.x x) (λx.x x)");
    private final Paragraph instruction;

    /**
     * Creates a new ExampleDialog with a callback method to insert the example string into the input
     * bar.
     * 
     * @param callback inserts the string of the chosen example into the input bar
     */
    protected ExampleDialog(Consumer<String> callback) {
        VerticalLayout layout = new VerticalLayout();
        instruction = new Paragraph(getTranslation("root.selectExample"));
        layout.add(instruction);
        for (String term : EXAMPLES) {
            Button button = new Button(term);
            button.addClickListener(click -> {
        	callback.accept(term);
        	this.close();
            });
            layout.add(button);
        }
        add(layout);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        instruction.setText(getTranslation("root.selectExample"));
    }
}