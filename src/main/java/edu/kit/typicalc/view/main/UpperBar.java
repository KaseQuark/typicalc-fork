package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import edu.kit.typicalc.view.main.MainView.MainViewListener;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Consumer;


/**
 * Contains all the components constantly shown in the upper part of the webpage.
 */
@CssImport("./styles/view/main/upper-bar.css")
@CssImport(value = "./styles/view/button-hover.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/view/button-hover.css", themeFor = "vaadin-drawer-toggle")
public class UpperBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -7344967027514015830L;

    /*
     * IDs for the imported .css-file
     */
    private static final String VIEW_TITLE_ID = "viewTitle";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String HELP_ICON_ID = "helpIcon";
    private static final String UPPER_BAR_ID = "header";

    private final InputBar inputBar;
    private final Button toggle;
    private final Button helpButton;

    private final transient MainViewListener presenter;
    private final transient Consumer<Pair<String, Map<String, String>>> inputConsumer;

    /**
     * Initializes a new UpperBar with the provided mainViewListener.
     *
     * @param presenter    the listener used to communicate with the model
     * @param inputConsumer function to handle user input
     */
    protected UpperBar(MainViewListener presenter, Consumer<Pair<String, Map<String, String>>> inputConsumer) {

        this.presenter = presenter;
        this.inputConsumer = inputConsumer;

        toggle = new DrawerToggle();
        H1 viewTitle = new H1(new Anchor("/", getTranslation("root.typicalc")));
        viewTitle.setId(VIEW_TITLE_ID);
        this.inputBar = new InputBar(this::typeInfer);
        inputBar.setId(INPUT_BAR_ID);
        helpButton = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE));
        helpButton.addClickListener(event -> new HelpDialog().open());
        helpButton.setId(HELP_ICON_ID);

        add(toggle, viewTitle, inputBar, helpButton);
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
        inputConsumer.accept(termAndAssumptions);
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

    @Override
    public void localeChange(LocaleChangeEvent event) {
        toggle.getElement().setAttribute("title", getTranslation("root.drawerToggleTooltip"));
        helpButton.getElement().setAttribute("title", getTranslation("root.helpIconTooltip"));
    }
}
