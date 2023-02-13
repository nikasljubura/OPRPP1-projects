package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * models a provider
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    private static LocalizationProvider instance;
    private String currentLanguage;
    private ResourceBundle resourceBundle;

    private LocalizationProvider(){
        this.currentLanguage = "en"; //default

        Locale locale = Locale.forLanguageTag(this.currentLanguage);
        this.resourceBundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp", locale);

    }

    public static LocalizationProvider getInstance(){
        if(instance == null) instance = new LocalizationProvider();
        return instance;
    }

    public void setLanguage(String language)  {
        this.currentLanguage = language;

        Locale locale = Locale.forLanguageTag(this.currentLanguage);
        this.resourceBundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp", locale);

        fire();
    }
    @Override
    public String getString(String key) {
        return resourceBundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return this.currentLanguage;
    }


}
