package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import edu.kit.typicalc.view.main.MainView.MainViewListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Contains all the components constantly shown in the upper part of the webpage.
 */
@CssImport("./styles/view/main/upper-bar.css")
@CssImport(value = "./styles/view/button-hover.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/view/button-hover.css", themeFor = "vaadin-drawer-toggle")
@CssImport(value = "./styles/view/main/select-text-field.css", themeFor = "vaadin-select-text-field")
public class UpperBar extends VerticalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -7344967027514015830L;

    /*
     * IDs for the imported .css-file
     */
    private static final String VIEW_TITLE_ID = "viewTitle";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String HELP_ICON_ID = "helpIcon";
    private static final String UPPER_BAR_ID = "header";
    private static final String TOP_LINE_ID = "top-line";
    private static final String LANGUAGE_SELECT_ID = "language-select";

    private final InputBar inputBar;
    private final Button helpButton;
    private final Select<Locale> languageSelect;
    private final ItemLabelGenerator<Locale> renderer;

    private final transient MainViewListener presenter;
    private final transient Consumer<Pair<String, Map<String, String>>> inputConsumer;

    /**
     * Initializes a new UpperBar with the provided mainViewListener.
     *
     * @param presenter     the listener used to communicate with the model
     * @param inputConsumer function to handle user input
     */
    protected UpperBar(MainViewListener presenter, Consumer<Pair<String, Map<String, String>>> inputConsumer) {

        this.presenter = presenter;
        this.inputConsumer = inputConsumer;

        H1 viewTitle = new H1(new Anchor("/", getTranslation("root.typicalc")));
        viewTitle.setId(VIEW_TITLE_ID);
        this.inputBar = new InputBar(this::typeInfer);
        inputBar.setId(INPUT_BAR_ID);
        helpButton = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE));
        helpButton.addClickListener(event -> new HelpDialog().open());
        helpButton.setId(HELP_ICON_ID);

        renderer = item -> getTranslation("root." + item.getDisplayLanguage(Locale.ENGLISH).toLowerCase());
        languageSelect = new Select<>(Locale.GERMAN, Locale.ENGLISH);
        languageSelect.setTextRenderer(renderer);
        languageSelect.setValue(UI.getCurrent().getLocale());
        languageSelect.addValueChangeListener(event -> UI.getCurrent().getSession().setLocale(event.getValue()));
        languageSelect.setId(LANGUAGE_SELECT_ID);
        HorizontalLayout topLine = new HorizontalLayout(languageSelect, viewTitle, helpButton);
        topLine.setId(TOP_LINE_ID);

        add(topLine, inputBar);
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
        helpButton.getElement().setAttribute("title", getTranslation("root.helpIconTooltip"));
        languageSelect.setLabel(getTranslation("root.selectLanguage"));
        languageSelect.setTextRenderer(renderer);
    }
}
