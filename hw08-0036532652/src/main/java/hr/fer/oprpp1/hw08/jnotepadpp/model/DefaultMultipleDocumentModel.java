package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, MultipleDocumentListener {



    private SingleDocumentModel currentDocument; //currently document showed
    private List<SingleDocumentModel> allDocumentsOpened; //list of all currently opened documents
    private List<MultipleDocumentListener> multipleDocumentListeners; //listeners of changes in this
    private ImageIcon modifiedIcon = getIMIcon("icons/notmodified.PNG");
    private ImageIcon unmodifiedIcon = getIMIcon("icons/modified.PNG");

    public DefaultMultipleDocumentModel(){
        this.allDocumentsOpened = new ArrayList<>();
        this.multipleDocumentListeners = new ArrayList<>();

        this.addChangeListener(listener -> {
            SingleDocumentModel previous = this.currentDocument;
            int docIndex = this.getSelectedIndex();

            if(docIndex >= 0){
                this.currentDocument = allDocumentsOpened.get(docIndex);
            }else{
                this.currentDocument = null;
            }

            notifyCurrentDocChanged(previous, currentDocument);
        });
        this.multipleDocumentListeners.add(this);
    }

    public void notifyCurrentDocChanged(SingleDocumentModel previous, SingleDocumentModel currentDocument) {

        for(MultipleDocumentListener listener: multipleDocumentListeners){
            listener.currentDocumentChanged(previous, currentDocument);
        }
    }

    /**
     * loads image from disk and returns it as icon
     * @param filePath
     * @return
     */
    private ImageIcon getIMIcon(String filePath){

        InputStream stream = this.getClass().getResourceAsStream(filePath);
        byte[] imBytes = null;
        try{
            imBytes = stream.readAllBytes();
            stream.close();
            return new ImageIcon(imBytes);
        }catch(IOException exc){
            System.out.println("Error has occured while loading icons");
            throw new RuntimeException("Error getting image");
        }

    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return this.allDocumentsOpened.iterator();
    }

    /**
     * method called when current document is changed
     * selects current document
     * Only one of the arguments can be null
     *
     * @param previousModel previous document
     * @param currentModel  current document
     */
    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if(currentModel == null) return;
        setSelectedIndex(allDocumentsOpened.indexOf(currentModel));
    }

    /**
     * method called when SingleDocumentModel has been added to MultipleDocumentModel collection
     *
     * @param model
     */
    @Override
    public void documentAdded(SingleDocumentModel model) {
        //make new panel
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(model.getTextComponent());
        p.add(pane, BorderLayout.CENTER);

        //add it to tabbedPane
        this.addTab("(unnamed)", p);
        this.getNewTab(model);

        //add listeners and change tab when triggered
        model.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                getNewTab(model);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                getNewTab(model);
            }
        });

    }

    private void getNewTab(SingleDocumentModel model) {

        String defaultTitle = "(unnamed)";
        String defaultToolTip = "(unnamed)";

        if(model.getFilePath() != null){
            // new title and tooltip
            defaultTitle = model.getFilePath().getFileName().toString();
            defaultToolTip = model.getFilePath().toAbsolutePath().toString();
        }

        //set new title, tooltip and icon
        int index = this.allDocumentsOpened.indexOf(model);
        this.setTitleAt(index, defaultTitle);
        this.setToolTipTextAt(index, defaultToolTip);
        ImageIcon icon = null;
        if(model.isModified()){
            icon = modifiedIcon;
        }else{
            icon = unmodifiedIcon;
        }
        this.setIconAt(index,icon);
    }

    /**
     * method called when SingleDocumentModel has been removed from MultipleDocumentModel collection
     *
     * @param model
     */
    @Override
    public void documentRemoved(SingleDocumentModel model) {
        int index = this.allDocumentsOpened.indexOf(model);
        remove(index);

    }

    /**
     * @return the graphical component which is responsible for displaying the entire
     * MultipleDocumentModel‘s user interface
     */
    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    /**
     * Creates blank document and opens it to user
     *
     * @return created SingleDocumentModel document
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        
        SingleDocumentModel previous = this.currentDocument;
        this.currentDocument = new DefaultSingleDocumentModel(null, "");
        this.allDocumentsOpened.add(this.currentDocument);
        notifyDocumentAdded(this.currentDocument);
        notifyCurrentDocChanged(previous, this.currentDocument);
        
        return this.currentDocument;
    }

    private void notifyDocumentAdded(SingleDocumentModel currentDocument) {
        for(MultipleDocumentListener listener: multipleDocumentListeners){
            listener.documentAdded(currentDocument);
        }
    }

    /**
     * @return Current selected SingleDocumentModel or null if there is no model selected
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        return this.currentDocument;
    }

    /**
     * Loads a document from disk and adds it to internal collection
     *
     * @param path to document
     * @return created docuemnt (current document)
     */
    @Override
    public SingleDocumentModel loadDocument(Path path) {
       if(path == null) throw new NullPointerException("Path must be non null");

       //see if model with required path already exists
        SingleDocumentModel doc = null;
        SingleDocumentModel previous = this.currentDocument;
        for(SingleDocumentModel model: allDocumentsOpened){

            if(model.getFilePath() != null && model.getFilePath().equals(path)){
                doc = model;
                break;
            }
        }

        //already exists - set it as current
        if(doc != null){
            this.currentDocument = doc;
        }else{
            //doesnt exist, safe to load
            String doctext = null;
            try{
                doctext = Files.readString(path);
                this.currentDocument = new DefaultSingleDocumentModel(path, doctext);
                this.allDocumentsOpened.add(this.currentDocument);
                notifyDocumentAdded(this.currentDocument);
            }catch (IOException exc){
                System.out.println("Error occured while reading contents of: " + path.getFileName());
                System.exit(-1);
            }

        }

        notifyCurrentDocChanged(previous, this.currentDocument);

        return this.currentDocument;
    }

    /**
     * Saves passed document to disk with passed path
     * if path is null, saves it to current stored path
     * @param model
     * @param newPath
     */
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        Path newpath = null;
        if(newPath == null){ //new path not provided
            newpath = model.getFilePath();
        }else{ //new path is provided
            newpath = newPath;
            //is there a different model already stored on provided path?
            SingleDocumentModel doc = null;
            SingleDocumentModel previous = this.currentDocument;
            for(SingleDocumentModel m: allDocumentsOpened){
                if(model.getFilePath() == newpath && !m.equals(model)){
                    doc = model;
                    break;
                }
            }

            if(doc != null){
                System.out.println("There already exists a file with given path");
                System.exit(-1);
            }
        }

        if(newpath == null){
            throw new NullPointerException("Illegal arguments in save document");
        }

        try{
            Files.writeString(newpath, model.getTextComponent().getText());
            model.setFilePath(newpath);
            model.setModified(false);
        }catch(IOException exc){
            throw new RuntimeException("Error saving file");
        }

    }


    /**
     * Closes desired document
     *
     * @param model document we want to close
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
            notifyDocumentRemoved(model);
            this.allDocumentsOpened.remove(model);

            if(allDocumentsOpened.size() == 0){
                this.currentDocument = null; //no documents opened left
            }else{
                this.currentDocument = allDocumentsOpened.get(0); //show first
            }

            notifyCurrentDocChanged(model, this.currentDocument);
    }

    private void notifyDocumentRemoved(SingleDocumentModel model) {
        for(MultipleDocumentListener listener: multipleDocumentListeners){
            listener.documentRemoved(model);
        }
    }

    /**
     * adds a MultipleDocumentListener to collection of listeners
     *
     * @param l
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        this.multipleDocumentListeners.add(l);
    }

    /**
     * removes a MultipleDocumentListener from collection of listeners
     *
     * @param l
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        this.multipleDocumentListeners.remove(l);
    }

    /**
     * @return number of documents opened
     */
    @Override
    public int getNumberOfDocuments() {
        return this.allDocumentsOpened.size();
    }

    /**
     * @param index of the model we want to get
     * @return document at index from collection
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        return this.allDocumentsOpened.get(index);
    }

    /**
     * @param path
     * @return document stored for specified path
     */
    @Override
    public SingleDocumentModel findForPath(Path path) {
        SingleDocumentModel doc = null;
        for(SingleDocumentModel m: allDocumentsOpened){
            if(m.getFilePath() == path){
                doc = m;
                break;
            }
        }

        return doc;
    }

    /**
     * @param doc
     * @return index for document passed
     */
    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return this.allDocumentsOpened.indexOf(doc);
    }
}
