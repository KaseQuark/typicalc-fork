package edu.kit.typicalc.view.main;


import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Contains all predefined examples as buttons.
 * Clicking on a button selects the example and allows the user to choose between various type assumptions.
 */
public class TermExampleContent extends VerticalLayout {
    private static final long serialVersionUID = 2882744650931562239L;

    /**
     * Sets up the predefined examples with a callback method that will receive the selected example.
     *
     * @param callback function to handle the selected lambda term
     */
    protected TermExampleContent(Consumer<String> callback) {
       String[] exampleTerms = getTranslation("root.exampleTerms").split(",");
        for (String term : exampleTerms) {
            Button button = new Button(term);
            button.addClickListener(click -> callback.accept(term));
            button.setId(term); // needed for integration test
            this.add(button);
        }
    }
}
