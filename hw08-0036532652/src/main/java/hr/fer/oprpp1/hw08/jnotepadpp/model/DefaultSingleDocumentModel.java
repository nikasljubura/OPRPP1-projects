package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * implements SingleDocumentModel
 */

public class DefaultSingleDocumentModel implements SingleDocumentModel{

    private JTextArea textArea;
    private Path filePath;
    private boolean isModified;
    private List<SingleDocumentListener> listeners;



    public DefaultSingleDocumentModel(Path filePath, String documentText){

        this.textArea = new JTextArea(documentText);
        this.filePath = filePath;
        this.isModified = false;
        this.listeners = new ArrayList<>();

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               setModified(true);
            }
        });

    }
    /**
     * @return Component (instance of JTextComponent) showing this document
     */
    @Override
    public JTextArea getTextComponent() {
        return this.textArea;
    }

    /**
     * @return path to selected document or null if there is no path set
     */
    @Override
    public Path getFilePath() {
        return this.filePath;
    }

    /**
     * sets the path of document to passed path and notifies listeners registered
     *
     * @param path
     */
    @Override
    public void setFilePath(Path path) {
        this.filePath = path;

        for(SingleDocumentListener listener: listeners){
            listener.documentFilePathUpdated(this);
        }
    }

    /**
     * state of modification (true if model has been modified, false otherwise)
     *
     * @return true (document modified), false (document not modified)
     */
    @Override
    public boolean isModified() {
        return this.isModified;
    }

    /**
     * sets the state of modification of document to passed and notifies listeners registered to chamge
     *
     * @param modified
     */
    @Override
    public void setModified(boolean modified) {
        boolean past = this.isModified;

        this.isModified = modified;
        if(modified != past){
            for(SingleDocumentListener l: listeners){
                l.documentModifyStatusUpdated(this);
            }
        }
    }

    /**
     * add SingleDocumentListener to collection of listeners
     *
     * @param l
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
            listeners.add(l);
    }

    /**
     * remove SingleDocumentListener to collection of listeners
     *
     * @param l
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
            listeners.remove(l);
    }
}
