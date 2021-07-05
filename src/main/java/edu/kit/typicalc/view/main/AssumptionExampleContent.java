package edu.kit.typicalc.view.main;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * 
 *
 */
@CssImport("./styles/view/main/assumption-example-content.css")
public class AssumptionExampleContent extends VerticalLayout {
    private static final long serialVersionUID = -8157345417001452273L;
    
    private static final String SELECTED_TERM_BAR_ID = "selected-term-bar"; // exchange with better name
    private static final String SELCETED_TERM_ID = "selected-term";
    
    /**
     * Sets up the type assumptions for the previously selected term.
     * 
     * @param term the term previously selected by the user
     * @param forwardCallback callback to start the algorithm with the chosen example
     * @param backwardsCallback callback to return to the terms selection stage
     */
    protected AssumptionExampleContent(String term, Consumer<String> forwardCallback, Runnable backwardsCallback) {
        TextField selectedTerm = new TextField();
        selectedTerm.setLabel(getTranslation("root.selectedTerm"));
        selectedTerm.setValue(term);
        selectedTerm.setReadOnly(true);
        selectedTerm.setId(SELCETED_TERM_ID);
        
        Icon goBack = VaadinIcon.ANGLE_DOUBLE_LEFT.create();
        goBack.addClickListener(event -> backwardsCallback.run());
        
        VerticalLayout selectedTermBar = new VerticalLayout(goBack, selectedTerm);
        selectedTermBar.setId(SELECTED_TERM_BAR_ID);
        this.add(selectedTermBar);
        
        String[] exampleAssumptions = getTranslation("root." + term.replaceAll("(\\s+|=)", "")).split(",");
        for (String assumptions : exampleAssumptions) {
            Button button = new Button(assumptions);
            button.addClickListener(click -> forwardCallback.accept(assumptions.
                    replace(getTranslation("root.emptySet"), StringUtils.EMPTY)));
            this.add(button);
        }
    }
}
