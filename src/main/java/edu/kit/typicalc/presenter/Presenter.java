package edu.kit.typicalc.presenter;

import java.util.Map;

import com.vaadin.flow.component.UI;
import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.util.Result;
import edu.kit.typicalc.view.main.MainView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;

import static edu.kit.typicalc.view.main.InputBar.TERM_INPUT_FIELD_ID;

/**
 * Manages data exchange between the view and the model.
 */
public class Presenter implements MainViewListener {

    private final Model model;
    private final MainView view;

    /**
     * Initializes a new presenter object with the provided model and view.
     *
     * @param model the implementation of the Model
     * @param view  the implementation of the MainView
     */
    public Presenter(Model model, MainView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void typeInferLambdaString(String lambdaTerm, Map<String, String> typeAssumptions) {
        Result<TypeInfererInterface, ParseError> result = model.getTypeInferer(lambdaTerm, typeAssumptions);
        if (result.isError()) {
            view.displayError(result.unwrapError());
            // focus the input bar again
            UI.getCurrent().getPage().executeJs(
                    "setTimeout(() => document.getElementById('" + TERM_INPUT_FIELD_ID + "').focus(), 0)");
        } else {
            view.setTypeInferenceView(result.unwrap());
        }
    }
}
