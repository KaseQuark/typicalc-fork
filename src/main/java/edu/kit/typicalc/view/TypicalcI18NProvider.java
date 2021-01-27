package edu.kit.typicalc.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.vaadin.flow.i18n.I18NProvider;

/**
 * Provides a simple implementation of the I18NProvider.
 * Allows for multiple languages and retrieving static Strings from .property-files.
 */
@Component
public class TypicalcI18NProvider implements I18NProvider {

    private static final long serialVersionUID = 8261479587838699070L;

    /**
     * Prefix of all .property-files
     */
    public static final String BUNDLE_PREFIX = "language.translation";

    @Override
    public List<Locale> getProvidedLocales() {
        return Collections.unmodifiableList(Arrays.asList(Locale.GERMAN, Locale.ENGLISH));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            return StringUtils.EMPTY;
        }

        String translation;

        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
            translation = bundle.getString(key);
        } catch (final MissingResourceException exception) {
            throw new IllegalStateException("this should never happen:"
                    + " either an invalid locale is set or an invalid key is provided.");
        }

        return translation;
    }

}
