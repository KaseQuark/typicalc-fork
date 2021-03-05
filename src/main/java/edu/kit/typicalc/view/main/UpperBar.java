package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import edu.kit.typicalc.view.main.MainView.MainViewListener;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Consumer;


/**
 * Contains all the components constantly shown in the upper part of the webpage.
 */
@CssImport("./styles/view/main/upper-bar.css")
public class UpperBar extends HorizontalLayout {
    private static final long serialVersionUID = -7344967027514015830L;

    /*
     * IDs for the imported .css-file
     */
    private static final String VIEW_TITLE_ID = "viewTitle";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String HELP_ICON_ID = "helpIcon";
    private static final String UPPER_BAR_ID = "header";

    private final InputBar inputBar;

    private final transient MainViewListener presenter;
    private final transient Consumer<Pair<String, Map<String, String>>> setTermInURL;

    /**
     * Initializes a new UpperBar with the provided mainViewListener.
     *
     * @param presenter    the listener used to communicate with the model
     * @param setTermInURL function to set the term into the URL
     */
    protected UpperBar(MainViewListener presenter, Consumer<Pair<String, Map<String, String>>> setTermInURL) {

        this.presenter = presenter;
        this.setTermInURL = setTermInURL;

        H1 viewTitle = new H1(new Anchor("/", getTranslation("root.typicalc")));
        viewTitle.setId(VIEW_TITLE_ID);
        this.inputBar = new InputBar(this::typeInfer);
        inputBar.setId(INPUT_BAR_ID);
        Button helpIcon = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE));
        helpIcon.addClickListener(event -> onHelpIconClick());
        helpIcon.setId(HELP_ICON_ID);

        add(new DrawerToggle(), viewTitle, inputBar, helpIcon);
        setId(UPPER_BAR_ID);
        getThemeList().set("dark", true);
        setSpacing(false);
    }

    /**
     * Starts the type inference algorithm by passing the required arguments to the MainViewListener
     * and updating the URL.
     *
     * @param termAndAssumptions the lambda term to be type-inferred and the type assumptions to use
     */
    protected void typeInfer(Pair<String, Map<String, String>> termAndAssumptions) {
        setTermInURL.accept(termAndAssumptions);
        if (!"".equals(termAndAssumptions.getLeft())) {
            startInfer(termAndAssumptions.getLeft(), termAndAssumptions.getRight());
        }
    }

    private void startInfer(String term, Map<String, String> typeAssumptions) {
        presenter.typeInferLambdaString(term, typeAssumptions);
    }

    /**
     * Sets the lambda term and type assumptions in the InputBar and starts the inference.
     *
     * @param term            the provided string
     * @param typeAssumptions type assumptions to use
     */
    protected void inferTerm(String term, Map<String, String> typeAssumptions) {
        inputBar.setTypeAssumptions(typeAssumptions);
        inputBar.setTerm(term);
        startInfer(term, typeAssumptions);
    }

    private void onHelpIconClick() {
        Dialog helpDialog = new HelpDialog();
        helpDialog.open();
    }
}
