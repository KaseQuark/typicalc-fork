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
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contains all the displayed components and builds the applications user interface (UI).
 * Vaadin's app layout provides the rough structure of the UI. Using this structure the UI always
 * consists of an upper bar at the top of the screen and a drawer on the left side of
 * the screen.
 */
@CssImport("./styles/view/main/main-view.css")
@CssImport("./styles/global.css")
@CssImport(value = "./styles/view/main/app-layout.css", themeFor = "vaadin-app-layout")
@JavaScript("./src/svg-pan-zoom.min.js")
@JavaScript("./src/tex-svg-full.js")
@Route(TypeInferenceView.ROUTE + "/:term")
public class MainViewImpl extends AppLayout
        implements MainView, BeforeEnterObserver, HasErrorParameter<NotFoundException> {
    private static final long serialVersionUID = -2411433187835906976L;

    public static final String PAGE_TITLE = "Typicalc";

    private final UpperBar upperBar;
    private transient Optional<TypeInferenceView> tiv = Optional.empty();

    /**
     * Creates a new MainViewImpl.
     */
    public MainViewImpl() {
        setDrawerOpened(false);
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        upperBar = new UpperBar(presenter, this::setTermInURL);
        addToNavbar(upperBar);
        addToDrawer(new DrawerContent());
    }

    @Override
    public void setTypeInferenceView(TypeInfererInterface typeInferer) {
        tiv = Optional.of(new TypeInferenceView(typeInferer));
        setContent(tiv.get());
    }

    @Override
    public void displayError(ParseError error) {
        Notification errorNotification = new ErrorNotification(error);
        errorNotification.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        tiv = Optional.empty();
        if (event.getLocation().getPath().matches(TypeInferenceView.ROUTE + "/.*")) {
            Location url = event.getLocation();
            List<String> segments = url.getSegments();
            String term = segments.get(segments.size() - 1);
            Map<String, String> types = url.getQueryParameters().getParameters().entrySet().stream().map(entry ->
                    Pair.of(entry.getKey(), entry.getValue().get(0))
            ).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
            upperBar.inferTerm(decodeURL(term), types);
        } else if (event.getLocation().getPath().equals(TypeInferenceView.ROUTE)) {
            setContent(new StartPageView());
            upperBar.inferTerm(StringUtils.EMPTY, Collections.emptyMap());
        } else if (event.getLocation().getPath().equals(StringUtils.EMPTY)) {
            setContent(new StartPageView());
        }
    }

    @Override
    protected void afterNavigation() {
        // this method ensures that the content is visible after navigation
        tiv.ifPresent(this::setContent);
    }

    private void setTermInURL(Pair<String, Map<String, String>> lambdaTermAndAssumptions) {
        String lambdaTerm = lambdaTermAndAssumptions.getLeft();
        if ("".equals(lambdaTerm)) {
            UI.getCurrent().getPage().getHistory().pushState(null, "./");
            setContent(new StartPageView());
            return;
        }
        StringBuilder types = new StringBuilder();
        for (Map.Entry<String, String> type : lambdaTermAndAssumptions.getRight().entrySet()) {
            if (types.length() > 0) {
                types.append('&');
            }
            types.append(type.getKey());
            types.append('=');
            types.append(type.getValue());
        }
        String typeAssumptions = "";
        if (types.length() > 0) {
            typeAssumptions = "?" + types.toString();
        }
        UI.getCurrent().getPage().getHistory().pushState(null,
                new Location(TypeInferenceView.ROUTE + "/" + lambdaTerm + typeAssumptions));
    }

    private String decodeURL(String encodedUrl) {
        return java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        setContent(new NotFoundView());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
