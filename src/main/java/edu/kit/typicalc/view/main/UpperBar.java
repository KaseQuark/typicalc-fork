package edu.kit.typicalc.view.main;

import java.util.HashMap;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import edu.kit.typicalc.view.content.infocontent.StartPageView;
import edu.kit.typicalc.view.main.MainView.MainViewListener;

@CssImport("./styles/view/main/upper-bar.css")
public class UpperBar extends HorizontalLayout implements LocaleChangeObserver {
    
    private final H1 viewTitle;
    private final InputBar inputBar;
    private final Icon helpDialogIcon;
    
    private final MainViewListener presenter;
    
    protected UpperBar(final MainViewListener presenter) {
        this.presenter = presenter;
        
        add(new DrawerToggle());
        this.viewTitle = new H1(getTranslation("root.typicalc"));
        viewTitle.setId("viewTitle");
        this.inputBar = new InputBar(this::typeInfer);
        inputBar.setId("inputBar");
        this.helpDialogIcon = new Icon(VaadinIcon.QUESTION_CIRCLE);
        helpDialogIcon.setId("icon");
        
        viewTitle.addClickListener(event -> this.getUI().get().navigate(StartPageView.class));
        
        add(viewTitle, inputBar, helpDialogIcon);
        setId("header");
        getThemeList().set("dark", true); // remove magic string
        setWidthFull();
        setSpacing(false);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }
    
    protected void typeInfer(final String lambdaString) {
        presenter.typeInferLambdaString(lambdaString, new HashMap<String, String>());
    }
    
    private void createHelpDialog() {
        //TODO create help dialog here --> maybe move to separate class if too big
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        // TODO Auto-generated method stub
    }
}
