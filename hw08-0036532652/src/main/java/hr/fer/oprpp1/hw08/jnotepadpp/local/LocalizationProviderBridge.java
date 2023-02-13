package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * localization provider that is able to connect and disconnect from current provider
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    private boolean connected;
    private ILocalizationProvider localizationProvider;
    private ILocalizationListener listener;
    private String currentLanguage;

    public LocalizationProviderBridge(ILocalizationProvider provider){
        this.localizationProvider = provider;
        this.listener = this::fire;
    }

    @Override
    public String getString(String key) {
        return localizationProvider.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return localizationProvider.getCurrentLanguage();
    }

    /**
     * connects to localization provider
     */
    public void connect(){
        if(!this.connected){
            if(currentLanguage != null && !currentLanguage.equals(localizationProvider.getCurrentLanguage())){
                fire();
            }
            this.connected = true;
            this.localizationProvider.addLocalizationListener(listener);
        }else{
            return;
        }
    }


    public void disconnect(){
        if(!this.connected){
            return;
        }else{
            this.connected = false;
            this.localizationProvider.removeLocalizationListener(listener);
            currentLanguage = localizationProvider.getCurrentLanguage();
        }
    }
}
