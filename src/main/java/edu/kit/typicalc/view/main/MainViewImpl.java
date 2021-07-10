package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.router.*;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import edu.kit.typicalc.model.parser.ParseError;
import edu.kit.typicalc.presenter.Presenter;
import edu.kit.typicalc.view.content.errorcontent.ErrorView;
import edu.kit.typicalc.view.content.infocontent.StartPageView;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Contains all the displayed components and builds the applications user interface (UI).
 * Vaadin's app layout provides the rough structure of the UI. Using this structure the UI always
 * consists of an upper bar at the top of the screen and a drawer on the left side of
 * the screen.
 */
@CssImport("./styles/view/main/main-view.css")
@CssImport("./styles/global.css")
@CssImport(value = "./styles/view/main/app-layout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/view/vaadin-button.css", themeFor = "vaadin-button")
@JavaScript("./src/hammer.min.js")
@JavaScript("./src/svg-pan-zoom.min.js")
@JavaScript("./src/tex-svg-full.js")
public class MainViewImpl extends AppLayout
        implements MainView, HasErrorParameter<NotFoundException>, AfterNavigationObserver {
    private static final long serialVersionUID = -2411433187835906976L;

    /**
     * The title of the application
     */
    public static final String PAGE_TITLE = "Typicalc";
    private static final String ASSUMPTION_PARAMETER = "assumptions";

    private final UpperBar upperBar;

    /**
     * Creates a new MainViewImpl.
     */
    public MainViewImpl() {
        MainViewListener presenter = new Presenter(new ModelImpl(), this);
        upperBar = new UpperBar(presenter, this::processInput);
        addToNavbar(upperBar);
    }

    @Override
    public void setTypeInferenceView(TypeInfererInterface typeInferer) {
        setContent(new TypeInferenceView(typeInferer));
    }

    @Override
    public void displayError(ParseError error) {
        setContent(new ErrorView(error));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        this.handleLocation(event.getLocation());
    }

    private void handleLocation(Location url) {
        if (url.getPath().matches(TypeInferenceView.ROUTE + "/.*")) {
            List<String> segments = url.getSegments();
            String term = segments.get(segments.size() - 1);
            List<String> types = url.getQueryParameters().getParameters().get(ASSUMPTION_PARAMETER);
            String assumptions = types != null && types.size() == 1 ? types.get(0) : "";
            upperBar.inferTerm(decodeURL(term), assumptions);
        } else if (url.getPath().equals(TypeInferenceView.ROUTE)) {
            setContent(new StartPageView());
            upperBar.inferTerm(StringUtils.EMPTY, "");
        } else if (url.getPath().equals(StringUtils.EMPTY)) {
            setContent(new StartPageView());
        }
    }

    private void processInput(Pair<String, String> lambdaTermAndAssumptions) {
        String lambdaTerm = lambdaTermAndAssumptions.getLeft();
        if (lambdaTerm.isBlank()) {
            UI.getCurrent().navigate("./");
            return;
        }
        String assumptions = lambdaTermAndAssumptions.getRight();
        if (assumptions.isEmpty()) {
            UI.getCurrent().navigate(TypeInferenceView.ROUTE + "/"
                    + URLEncoder.encode(lambdaTerm, StandardCharsets.UTF_8));
        } else {
            QueryParameters qp = new QueryParameters(
                    Map.of(ASSUMPTION_PARAMETER, List.of(lambdaTermAndAssumptions.getRight())));
            UI.getCurrent().navigate(TypeInferenceView.ROUTE + "/"
                    + URLEncoder.encode(lambdaTerm, StandardCharsets.UTF_8), qp);
        }
    }

    private String decodeURL(String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        setContent(new NotFoundView());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
