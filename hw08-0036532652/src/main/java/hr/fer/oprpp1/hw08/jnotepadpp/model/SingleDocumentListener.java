package hr.fer.oprpp1.hw08.jnotepadpp.model;

public interface SingleDocumentListener {

    /**
     * callex when document(SingleDocumentModel) has been modified
     * @param model
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * called when document(SingleDocumentModel) path has been modified
     * @param model
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
