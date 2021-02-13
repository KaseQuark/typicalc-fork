package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.ParentLayout;

/**
 * This is the view used when an unknown URL is requested by the user.
 */
@ParentLayout(MainViewImpl.class)
@CssImport("./styles/view/not-found.css")
public class NotFoundView extends VerticalLayout implements LocaleChangeObserver {
    private static final String ID = "not-found";

    private final H1 error404;
    private final H2 suggestion;

    /**
     * Initializes a new NotFoundView with an error message.
     */
    public NotFoundView() {
        setId(ID);
        error404 = new H1();
        suggestion = new H2();
        add(error404, suggestion); // todo make beautiful
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        error404.setText(getTranslation("root.title404"));
        suggestion.setText(getTranslation("root.message404"));
    }
}