package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * localized JMenu
 */
public class LocalizableJMenu extends JMenu {
    private String key;
    private static final long serialVersionUID = 1L;
    private ILocalizationProvider provider;

    public LocalizableJMenu(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;
        if(this.key == null) throw new NullPointerException("Key cannot be null");
        if(this.provider == null) throw new NullPointerException("Provider cannot be null");
        this.updateMenu();
        provider.addLocalizationListener(this::updateMenu);

    }

    private void updateMenu() {
        this.setText(provider.getString(key));
    }
}
