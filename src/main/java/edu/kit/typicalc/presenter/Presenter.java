package edu.kit.typicalc.presenter;

import java.util.Map;
import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.util.Result;
import edu.kit.typicalc.view.main.MainView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;

/**
 * Manages data exchange between the view and the model.
 */
public class Presenter implements MainViewListener {

    private final Model model;
    private final MainView view;

    /**
     * Initializes a new presenter object with the provided model and view.
     * @param model the implementation of the Model
     * @param view the implementation of the MainView
     */
    public Presenter(Model model, MainView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Provides the user input to the model and provide the result to the main view.
     * @param lambdaTerm the lambda term to type-infer
     * @param typeAssumptions type assumptions to use during the calculation
     */
    @Override
    public void typeInferLambdaString(String lambdaTerm, Map<String, String> typeAssumptions) {
        Result<TypeInfererInterface, ParseError> result = model.getTypeInferer(lambdaTerm, typeAssumptions);
        if (result.isError()) {
            view.displayError(result.unwrapError());
        } else {
            view.setTypeInferenceView(result.unwrap());
        }
    }
}
