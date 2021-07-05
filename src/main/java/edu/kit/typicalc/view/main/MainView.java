package edu.kit.typicalc.view.main;

import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;

/**
 * Provides an interface for the presenter to interact with the view. The interaction consists of
 * either passing a TypeInfererInterface or a ParseError to the view.
 */
public interface MainView {

    /**
     * Starts the creation of the visual representation of the provided typeInferer.
     * After the process is finished, the first step of the type inference tree is displayed.
     *
     * @param typeInferer the result of the computation of the type inference algorithm
     */
    void setTypeInferenceView(TypeInfererInterface typeInferer);

    /**
     * Displays the provided error indicating syntactically invalid input.
     *
     * @param error the error which is displayed to indicate invalid input
     */
    void displayError(ParseError error);

    /**
     * Provides an interface for the view to interact with the model.
     */
    interface MainViewListener {

        /**
         * Provides the user input to the model and provide the result to the main view.
         *
         * @param lambdaTerm      the lambda term to type-infer
         * @param typeAssumptions type assumptions to use during the calculation
         */
        void typeInferLambdaString(String lambdaTerm, String typeAssumptions);
    }
}
