package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.presenter.Presenter;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/view/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JavaScript("./src/tex-svg-full.js")
public class MainViewImpl extends AppLayout implements MainView {

    private H1 viewTitle;

    public MainViewImpl() {
        setDrawerOpened(false);
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        addToNavbar(true, new UpperBar(presenter));
        addToDrawer(new DrawerContent());
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        layout.add(new Avatar());
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Typicalc logo"));
        logoLayout.add(new H1("Typicalc"));
        layout.add(logoLayout, menu);
        return layout;
    }

    @Override
    public void setTypeInferenceView(final TypeInfererInterface typeInferer) {
        this.getUI().ifPresent(ui -> ui.navigate(TypeInferenceView.class, typeInferer));
    }

    @Override
    public void displayError(final ParseError error) {
        final Span errorText = new Span(getTranslation("root." + error.toString()));
        final Notification errorNotification = new Notification();
        final Button closeButton = new Button(getTranslation("root.close"), event -> errorNotification.close());

        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.add(errorText, closeButton);
        errorNotification.setPosition(Position.MIDDLE);
        errorNotification.open();
    }
}
