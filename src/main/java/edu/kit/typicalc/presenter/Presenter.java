package edu.kit.typicalc.presenter;

import java.util.Map;
import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.view.main.MainView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;

/**
 * Manages data exchange between the view and the model.
 */
public class Presenter implements MainViewListener {

    private Model model;
    private MainView view;

    /**
     * Initializes a new presenter object with the provided model and view.
     * @param model the implementation of the Model
     * @param view the implementation of the MainView
     */
    public Presenter(final Model model, final MainView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void typeInferLambdaString(String lambdaTerm, Map<String, String> typeAssumptions) {
        //TODO: Wo den Error verarbeiten? Wahrscheinlich Parameter von setTypeInferenceView ändern
        view.setTypeInferenceView(model.getTypeInferer(lambdaTerm, typeAssumptions).unwrap());
    }
}
