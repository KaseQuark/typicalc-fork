package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.presenter.Presenter;
import edu.kit.typicalc.view.content.infocontent.StartPageView;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Contains all the displayed components and builds the applications user interface (UI).
 * Vaadin's app layout provides the rough structure of the UI. Using this structure the UI always
 * consists of an upper bar at the top of the screen and a drawer on the left side of
 * the screen.
 */
@CssImport("./styles/view/main/main-view.css")
@CssImport(value = "./styles/view/main/app-layout.css", themeFor = "vaadin-app-layout")
@JavaScript("./src/svg-pan-zoom.min.js")
@JavaScript("./src/tex-svg-full.js")
@PageTitle("Typicalc")
@Route(TypeInferenceView.ROUTE + "/:term")
public class MainViewImpl extends AppLayout
        implements MainView, BeforeEnterObserver, HasErrorParameter<NotFoundException> {
    private static final long serialVersionUID = -2411433187835906976L;

    private final UpperBar upperBar;

    /**
     * Creates a new MainViewImpl.
     */
    public MainViewImpl() {
        setDrawerOpened(false);
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        upperBar = new UpperBar(presenter, this::setContent, this::setTermInURL);
        addToNavbar(upperBar);
        addToDrawer(new DrawerContent());
    }

    @Override
    public void setTypeInferenceView(TypeInfererInterface typeInferer) {
        TypeInferenceView tiv = new TypeInferenceView(typeInferer);
        setContent(tiv);
    }

    @Override
    public void displayError(ParseError error) {
        //TODO add error keys to bundle
        Notification errorNotification = new ErrorNotification(getTranslation("root." + error.toString()));
        errorNotification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getPath().matches(TypeInferenceView.ROUTE + "/.*")) {
            List<String> segments = event.getLocation().getSegments();
            String term = segments.get(segments.size() - 1);
            upperBar.inferTerm(decodeURL(term));
        } else if (event.getLocation().getPath().equals(TypeInferenceView.ROUTE)) {
            setContent(new StartPageView());
            upperBar.inferTerm(StringUtils.EMPTY);
        } else if (event.getLocation().getPath().equals(StringUtils.EMPTY)) {
            setContent(new StartPageView());
        }
    }


    private void setTermInURL(String lambdaTerm) {
        UI.getCurrent().getPage().getHistory().replaceState(null,
                new Location(TypeInferenceView.ROUTE + "/" + lambdaTerm));
    }

    private String decodeURL(String encodedUrl) {
        return java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        setContent(new NotFoundView());
        // TODO: actually return a real 404 response (not 200)
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
