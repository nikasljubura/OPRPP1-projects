package hr.fer.oprpp1.hw08.jnotepadpp.model;

public interface MultipleDocumentListener {

    /**
     * method called when current document is changed
     * Only one of the arguments can be null
     * @param previousModel previous document
     * @param currentModel current document
     */
    void currentDocumentChanged(SingleDocumentModel previousModel,
                                SingleDocumentModel currentModel);

    /**
     * method called when SingleDocumentModel has been added to MultipleDocumentModel collection
     * @param model
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * method called when SingleDocumentModel has been removed from MultipleDocumentModel collection
     * @param model
     */
    void documentRemoved(SingleDocumentModel model);
}
