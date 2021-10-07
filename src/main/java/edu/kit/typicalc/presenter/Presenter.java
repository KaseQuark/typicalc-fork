package edu.kit.typicalc.presenter;

import com.vaadin.flow.component.UI;
import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.util.Result;
import edu.kit.typicalc.view.main.MainView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;

import java.util.Optional;

import static edu.kit.typicalc.view.main.InputBar.ASS_INPUT_FIELD_ID;
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
    public void typeInferLambdaString(String lambdaTerm, String typeAssumptions) {
        Result<TypeInfererInterface, ParseError> result = model.getTypeInferer(lambdaTerm, typeAssumptions);
        if (result.isError()) {
            ParseError e = result.unwrapError();
            view.displayError(e);
            // focus the corresponding input field
            String inputFieldId = e.getErrorType().equals(Optional.of(ParseError.ErrorSource.TERM_ERROR))
                    ? TERM_INPUT_FIELD_ID : ASS_INPUT_FIELD_ID;
            UI.getCurrent().getPage().executeJs(
                    "setTimeout(() => document.getElementById('" + inputFieldId + "').focus(), 0)");
        } else {
            view.setTypeInferenceView(result.unwrap());
        }
    }
}
