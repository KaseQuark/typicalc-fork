package edu.kit.typicalc.view;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
     * Prefix of all language related .property-files
     */
    public static final String LANGUAGE_BUNDLE_PREFIX = "language.translation";

    /**
     * Prefix of general, language independent .property file
     */
    public static final String GENERAL_BUNDLE_PREFIX = "language.general";

    private final transient ResourceBundle generalBundle = ResourceBundle.getBundle(GENERAL_BUNDLE_PREFIX);

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(Locale.GERMAN, Locale.ENGLISH);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        final ResourceBundle bundle = ResourceBundle.getBundle(LANGUAGE_BUNDLE_PREFIX, locale);
        String translation;


        if (bundle.containsKey(key)) {
            translation = bundle.getString(key);
        } else {
            try {
                translation = this.generalBundle.getString(key);
            } catch (final MissingResourceException exception) {
                // this should never be the case
                return "?[" + key + "]?";
            }
        }

        return translation;
    }

}
