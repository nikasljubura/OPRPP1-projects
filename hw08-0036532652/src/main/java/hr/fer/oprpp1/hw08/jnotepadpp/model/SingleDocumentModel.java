package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.nio.file.Path;

public interface SingleDocumentModel {

    /**
     *
     * @return Component (instance of JTextComponent) showing this document
     */
    JTextArea getTextComponent();

    /**
     *
     * @return path to selected document or null if there is no path set
     */
    Path getFilePath();

    /**
     * sets the path of document to passed path and notifies listeners registered
     * @param path
     */
    void setFilePath(Path path);

    /**
     * state of modification (true if model has been modified, false otherwise)
     * @return true (document modified), false (document not modified)
     */
    boolean isModified();

    /**
     * sets the state of modification of document to passed and notifies listeners registered to chamge
     * @param modified
     */
    void setModified(boolean modified);

    /**
     * add SingleDocumentListener to collection of listeners
     * @param l
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * remove SingleDocumentListener to collection of listeners
     * @param l
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
