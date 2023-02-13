package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * provides localization
 */
public interface ILocalizationProvider {

    public String getString(String key); //returns translated string
    public void addLocalizationListener(ILocalizationListener listener); //subscribes listener
    public void removeLocalizationListener(ILocalizationListener listener); //subscribes listener
    public String getCurrentLanguage(); //returns current language


}
