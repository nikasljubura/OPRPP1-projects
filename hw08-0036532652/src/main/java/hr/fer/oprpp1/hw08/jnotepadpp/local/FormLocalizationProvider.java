package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * provider bridge associated with JFrame
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{

    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);

        if(frame == null) throw new NullPointerException("Frame can not be null");

        frame.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window has been opened.
             *
             * @param e
             */
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            /**
             * Invoked when a window has been closed.
             *
             * @param e
             */
            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });

    }
}
