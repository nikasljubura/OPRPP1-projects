package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.nio.file.Path;

/**
 * collection of SingleDocumentModel objects
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel>{

    /**
     * @return the graphical component which is responsible for displaying the entire
     * MultipleDocumentModel‘s user interface
     */
    JComponent getVisualComponent();

    /**
     * Creates blank document
     * @return created SingleDocumentModel document
     */
    SingleDocumentModel createNewDocument();

    /**
     *
     * @return Current selected SingleDocumentModel or null if there is no model selected
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document from disk and adds it to internal collection
     * @param path to document
     * @return created docuemnt
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves passed document to disk with passed path
     * @param model
     * @param newPath
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes desired document
     * @param model document we want to close
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * adds a MultipleDocumentListener to collection of listeners
     * @param l
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * removes a MultipleDocumentListener from collection of listeners
     * @param l
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     *
     * @return number of documents opened
     */
    int getNumberOfDocuments();

    /**
     *
     * @param index of the model we want to get
     * @return document at index from collection
     */
    SingleDocumentModel getDocument(int index);

    /**
     *
     * @param path
     * @return document stored for specified path
     */
    SingleDocumentModel findForPath(Path path); //null, if no such model exists

    /**
     *
     * @param doc
     * @return index for document passed
     */
    int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
}
