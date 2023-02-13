package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * implements ILocalizationProvider
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider{

    private List<ILocalizationListener> listeners;

    public AbstractLocalizationProvider() {
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * notifies listeners subscribed to change
     */
    public void fire() {
       for(ILocalizationListener listener: listeners){
           listener.localizationChanged();
       }
    }
}
