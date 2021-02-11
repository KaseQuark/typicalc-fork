package edu.kit.typicalc;

import com.vaadin.flow.component.page.AppShellConfigurator;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import edu.kit.typicalc.view.content.typeinferencecontent.TypeInferenceView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

import java.util.regex.Pattern;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the * and some desktop browsers.
 *
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer
        implements AppShellConfigurator, VaadinServiceInitListener {
    private static final Pattern ROUTE_PATTERN = Pattern.compile("/" + TypeInferenceView.ROUTE + "/[^/]+");

    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addRequestHandler((session, request, response) -> {
            // Vaadin does not set the error code:
            // https://github.com/vaadin/flow/issues/8942
            String url = request.getPathInfo();
            if (!url.equals("/") && !ROUTE_PATTERN.matcher(url).matches()) {
                response.setStatus(404);
            }
            return false;
        });
    }
}
