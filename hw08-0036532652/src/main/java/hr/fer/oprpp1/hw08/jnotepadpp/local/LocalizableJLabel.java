package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * JLabel with localizable text
 */
public class LocalizableJLabel extends JLabel {

    private String key;
    private static final long serialVersionUID = 1L;
    private ILocalizationProvider provider;

    public LocalizableJLabel(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;
        if(this.key == null) throw new NullPointerException("Key cannot be null");
        if(this.provider == null) throw new NullPointerException("Provider cannot be null");
        this.updateLabel();
        provider.addLocalizationListener(this::updateLabel);

    }

    private void updateLabel() {
        this.setText(provider.getString(key));
    }


}
