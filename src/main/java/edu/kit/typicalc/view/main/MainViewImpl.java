package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Location;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.presenter.Presenter;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;

/**
 * Contains all the displayed components and builds the applications user interface (UI).
 * Vaadin's app layout provides the rough structure of the UI. Using this structure the UI always
 * consists of an upper bar at the top of the screen and a drawer on the left side of
 * the screen.
 */
@CssImport("./styles/view/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@JavaScript("./src/svg-pan-zoom.min.js")
@JavaScript("./src/tex-svg-full.js")
public class MainViewImpl extends AppLayout implements MainView {
    private static final long serialVersionUID = -2411433187835906976L;
    private static final String ROUTE = "infer";
    private static final String SLASH = "/";

    private String termToType = "lambda x.x"; //todo replace with real value
    /**
     * Creates a new MainViewImpl.
     */
    public MainViewImpl() {
        setDrawerOpened(false);
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        addToNavbar(true, new UpperBar(presenter, this::setContent));
        addToDrawer(new DrawerContent());
    }

    @Override
    public void setTypeInferenceView(final TypeInfererInterface typeInferer) {
        TypeInferenceView tiv = new TypeInferenceView(typeInferer);
        UI.getCurrent().getPage().getHistory().replaceState(null, new Location(ROUTE + SLASH + termToType));
        setContent(tiv);
    }

    @Override
    public void displayError(final ParseError error) {
        //TODO add error keys to bundle
        final Notification errorNotification = new ErrorNotification(getTranslation("root." + error.toString()));
        errorNotification.open();
    }
}
