package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import com.vaadin.flow.i18n.I18NProvider;

import java.util.List;
import java.util.Locale;

public class ExplanationTranslationProvider implements I18NProvider {
    private static final long serialVersionUID = 5240864819723940755L;
    
    private final I18NProvider innerProvider;

    public ExplanationTranslationProvider(I18NProvider provider) {
        this.innerProvider = provider;
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return innerProvider.getProvidedLocales();
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        return innerProvider.getTranslation(key, locale, params);
    }

    public String getTranslationInstantiated(String key, Locale locale, Object... params) {
        return "";
    }
}
