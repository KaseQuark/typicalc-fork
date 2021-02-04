package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Location;
import edu.kit.typicalc.view.content.infocontent.StartPageView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.function.Consumer;


/**
 * Contains all the components constantly shown in the upper part of the webage.
 */
@CssImport("./styles/view/main/upper-bar.css")
public class UpperBar extends HorizontalLayout implements LocaleChangeObserver {
    private static final long serialVersionUID = -7344967027514015830L;

    /*
     * IDs for the imported .css-file
     */
    private static final String VIEW_TITLE_ID = "viewTitle";
    private static final String INPUT_BAR_ID = "inputBar";
    private static final String HELP_ICON_ID = "helpIcon";
    private static final String UPPER_BAR_ID = "header";

    private final H1 viewTitle;
    private final InputBar inputBar;
    private final Icon helpIcon;
    private final Button rules;

    private final transient MainViewListener presenter;
    private final transient Consumer<String> setTermInURL;

    /**
     * Initializes a new UpperBar with the provided mainViewListener.
     *
     * @param presenter the listener used to communicate with the model
     * @param setContent function to set the content of the application
     * @param setTermInURL function to set the term into the URL
     */
    protected UpperBar(final MainViewListener presenter, final Consumer<Component> setContent,
                       final Consumer<String> setTermInURL) {

        this.presenter = presenter;
        this.setTermInURL = setTermInURL;

        this.viewTitle = new H1(getTranslation("root.typicalc"));
        viewTitle.addClickListener(event -> routeToStartPage(setContent));
        viewTitle.setId(VIEW_TITLE_ID);
        this.inputBar = new InputBar(this::typeInfer);
        inputBar.setId(INPUT_BAR_ID);
        this.helpIcon = new Icon(VaadinIcon.QUESTION_CIRCLE);
        helpIcon.addClickListener(event -> onHelpIconClick());
        helpIcon.setId(HELP_ICON_ID);
        this.rules = new DrawerToggle();
        rules.setText(getTranslation("root.inferenceRules"));

        add(rules, viewTitle, inputBar, helpIcon);
        setId(UPPER_BAR_ID);
        getThemeList().set("dark", true); //TODO remove magic string
        setSpacing(false);
    }

    /**
     * Starts the type inference algorithm by passing the required arguments to the MainViewListener.
     *
     * @param lambdaString the lambda term to be type-inferred
     */
    protected void typeInfer(final String lambdaString) {
        setTermInURL.accept(lambdaString);
        presenter.typeInferLambdaString(lambdaString, new HashMap<>());
    }
    
    /**
     * Calls the inferTerm method in {@link edu.kit.typicalc.view.main.InputBar} with the provided
     * string as the argument.
     * 
     * @param term the provided string
     */
    protected void inferTerm(final String term) {
        inputBar.inferTerm(term);
    }

    private void routeToStartPage(Consumer<Component> setContent) {
        setContent.accept(new StartPageView());
        UI.getCurrent().getPage().getHistory().replaceState(null, new Location(StringUtils.EMPTY));
    }

    private void onHelpIconClick() {
        Dialog helpDialog = new HelpDialog();
        helpDialog.open();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
       rules.setText(getTranslation("root.inferenceRules")); 
    }
}
