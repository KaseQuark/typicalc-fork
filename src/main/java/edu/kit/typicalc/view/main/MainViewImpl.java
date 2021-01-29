package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.presenter.Presenter;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;

/**
 * Contains all the displayed components and builds the applications user interface (UI). 
 * Vaadins app layout provides the rough structure of the UI. Using this structure the UI always
 * consists of an upper bar at the top of the screen and a drawer on the left side of
 * the screen.
 */
@CssImport("./styles/view/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JavaScript("./src/tex-svg-full.js")
public class MainViewImpl extends AppLayout implements MainView {

    /**
     * Creates a new MainViewImpl.
     */
    public MainViewImpl() {
        setDrawerOpened(false);
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        addToNavbar(true, new UpperBar(presenter));
        addToDrawer(new DrawerContent());
    }

    @Override
    public void setTypeInferenceView(final TypeInfererInterface typeInferer) {
        this.getUI().ifPresent(ui -> ui.navigate(TypeInferenceView.class, typeInferer));
    }

    @Override
    public void displayError(final ParseError error) {
	//TODO add error keys to bundle
        final Span errorText = new Span(getTranslation("root." + error.toString()));
        final Notification errorNotification = new Notification();
        final Button closeButton = new Button(getTranslation("root.close"), event -> errorNotification.close());

        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.add(errorText, closeButton);
        errorNotification.setPosition(Position.MIDDLE);
        errorNotification.open();
    }
}
