package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.io.Serial;
import java.util.Objects;

/**
 * localizes action
 */

public abstract class LocalizableAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private String key;
    private ILocalizationProvider provider;

    public LocalizableAction(String key, ILocalizationProvider provider) {
        this.key = Objects.requireNonNull(key, "Localization key cannot be null.");
        this.provider = Objects.requireNonNull(provider, "Localization provider cannot be null.");

        putValue();

        provider.addLocalizationListener(this::putValue);
    }

    private void putValue() {
        putValue(NAME, provider.getString(key));
    }


}
