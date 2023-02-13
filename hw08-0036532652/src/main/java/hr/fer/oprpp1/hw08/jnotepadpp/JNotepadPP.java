package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.text.Collator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class JNotepadPP extends JFrame {

   //document actions
    private DefaultMultipleDocumentModel docModel;
    private Action saveAction;
    private Action createNewDocumentAction;
    private Action openExistingDocumentAction;
    private Action saveAsAction;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action showStatsAction;
    private Action exitAppAction;
    private Action closeAction;

    //case actions
    private Action toLCAction;
    private Action toUCAction;
    private Action invTextAction;
    private Action sortDescAction;
    private Action sortAscAction;
    private Action uniqueAction;

    //localization actions
    private FormLocalizationProvider formProvider;
    private Action switchToEng;
    private Action switchToHr;
    private Action switchToDe;

    String GUItitle = "JNotepad++";

    public JNotepadPP() {


        this.setTitle(getGUItitle());
        this.setSize(800,800);
        this.formProvider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp(); //exit on window close
            }
        });
        initGUI();
        /**
         * add state of document listener and subscribe/unsubscribe documents
         */
        docModel.addMultipleDocumentListener(new MultipleDocumentListener() {

            private SingleDocumentListener listener = new SingleDocumentListener() {
                @Override
                public void documentModifyStatusUpdated(SingleDocumentModel model) {

                }

                @Override
                public void documentFilePathUpdated(SingleDocumentModel model) {
                        setPageTitle(model);
                }
            };
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if(previousModel!=null) previousModel.removeSingleDocumentListener(listener);
                if(currentModel!=null) currentModel.addSingleDocumentListener(listener);

                setPageTitle(currentModel);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });

    }

    private void initGUI() {
        JPanel panel = new JPanel(new BorderLayout());
        this.docModel = new DefaultMultipleDocumentModel();
        panel.add(this.docModel.getVisualComponent(), BorderLayout.CENTER);
        JPanel statusBar = getStatusBar();
        panel.add(statusBar, BorderLayout.PAGE_END);

        this.add(panel, BorderLayout.CENTER);

        createActionsAndKeys();
        createMenuBar();
        JToolBar dockableToolBar = getToolBar();
        this.add(dockableToolBar, BorderLayout.PAGE_START);


    }

    private void createActionsAndKeys() {

        //promijenit lambde

        saveAction = new LocalizableAction("saveDocument", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    docModel.saveDocument(docModel.getCurrentDocument(), null);
                }
                catch (RuntimeException ex) {
                    showErr(ex.getMessage());
                }
            }
        };

        createNewDocumentAction = new LocalizableAction("createNewDocument", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                docModel.createNewDocument();
            }
        };

        openExistingDocumentAction = new LocalizableAction("openExistingDocument", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openExistingDocument();
            }
        };

        saveAsAction = new LocalizableAction("saveAsDocument", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsAction(docModel.getCurrentDocument());

            }
        };

        cutAction = new LocalizableAction("cut", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.CutAction().actionPerformed(null);
            }
        };
        copyAction = new LocalizableAction("copy", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.CopyAction().actionPerformed(null);
            }
        };
        pasteAction = new LocalizableAction("paste", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.PasteAction().actionPerformed(null);
            }
        };
        showStatsAction = new LocalizableAction("statistics", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
               showStatistics();
            }
        };
        exitAppAction = new LocalizableAction("exitApp", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApp();
            }
        };
        closeAction = new LocalizableAction("closeDocument", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                    closeDocument();
            }
        };
        toLCAction = new LocalizableAction("toLowercase", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                toLC();
            }
        };
        toUCAction = new LocalizableAction("toUppercase", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                toUC();
            }
        };
        invTextAction = new LocalizableAction("invert", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                invertCase();
            }
        };
        sortDescAction = new LocalizableAction("sortDescending", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortDesc();
            }
        };
        sortAscAction = new LocalizableAction("sortAscending", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortAsc();
            }
        };
        uniqueAction = new LocalizableAction("unique", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                unique();
            }
        };

        switchToEng = new LocalizableAction("chnglanguageEng", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        };

        switchToHr = new LocalizableAction("chnglanguageHr", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        };
        switchToDe = new LocalizableAction("chnglanguageDe", this.formProvider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
            }
        };


        setKeyAction(saveAction, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S);
        setKeyAction(createNewDocumentAction, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_N);
        setKeyAction(openExistingDocumentAction, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O);
        setKeyAction(saveAsAction, KeyStroke.getKeyStroke("control shift S"), KeyEvent.VK_A);
        setKeyAction(cutAction, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_U);
        setKeyAction(copyAction, KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C);
        setKeyAction(pasteAction, KeyStroke.getKeyStroke("control V"), KeyEvent.VK_P);
        setKeyAction(exitAppAction, KeyStroke.getKeyStroke("control alt X"), KeyEvent.VK_X);
        setKeyAction(closeAction, KeyStroke.getKeyStroke("control W"), KeyEvent.VK_C);
        setKeyAction(showStatsAction, KeyStroke.getKeyStroke("control shift T"), KeyEvent.VK_T);

        setKeyAction(toUCAction, KeyStroke.getKeyStroke("control shift alt U"), KeyEvent.VK_U);
        setKeyAction(toLCAction, KeyStroke.getKeyStroke("control shift alt L"), KeyEvent.VK_L);
        setKeyAction(invTextAction, KeyStroke.getKeyStroke("control shift alt I"), KeyEvent.VK_I);

        setKeyAction(sortAscAction, KeyStroke.getKeyStroke("control shift alt A"), KeyEvent.VK_A);
        setKeyAction(sortDescAction, KeyStroke.getKeyStroke("control shift alt D"), KeyEvent.VK_D);
        setKeyAction(uniqueAction, KeyStroke.getKeyStroke("control shift alt Q"), KeyEvent.VK_Q);

        setKeyAction(switchToEng, KeyStroke.getKeyStroke("control alt E"), KeyEvent.VK_E);
        setKeyAction(switchToHr, KeyStroke.getKeyStroke("control alt H"), KeyEvent.VK_H);
        setKeyAction(switchToDe, KeyStroke.getKeyStroke("control alt D"), KeyEvent.VK_D);


        //enable - disable actions (save, saveAs, closeDoc) -> if problem in review


        // Sorting, Changing case should only be enabled if there is something selected
        Predicate<SingleDocumentModel> test_enabled = document ->
                document != null &&
                document.getTextComponent().getSelectedText() != null;

        toLCAction.setEnabled(false);
        toUCAction.setEnabled(false);
        invTextAction.setEnabled(false);
        sortAscAction.setEnabled(false);
        sortDescAction.setEnabled(false);
        uniqueAction.setEnabled(false);

        ifSelectedEnable(toUCAction,test_enabled);
        ifSelectedEnable(toLCAction, test_enabled);
        ifSelectedEnable(invTextAction, test_enabled);
        ifSelectedEnable(sortDescAction,test_enabled);
        ifSelectedEnable(sortAscAction, test_enabled);
        ifSelectedEnable(uniqueAction, test_enabled);

    }

    /**
     * enables action if selected text emerges, ticked off by change of caret position or change of document
     * @param action
     * @param test_enabled
     */
    private void ifSelectedEnable(Action action, Predicate<SingleDocumentModel> test_enabled) {
        this.docModel.addMultipleDocumentListener(new MultipleDocumentListener() {
            private CaretListener listener = e -> action.setEnabled(test_enabled.test
                    (docModel.getCurrentDocument()));
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.getTextComponent().removeCaretListener(listener);
                }
                if (currentModel != null) {
                    currentModel.getTextComponent().addCaretListener(listener);
                }
                action.setEnabled(test_enabled.test(currentModel));
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {}

            @Override
            public void documentRemoved(SingleDocumentModel model) {}
        });
    }

    private void unique() {
        if (this.docModel.getCurrentDocument() == null) {
            return;
        }
        JTextComponent textComponent = this.docModel.getCurrentDocument().getTextComponent();
        Document doc = textComponent.getDocument();
        Caret caret = textComponent.getCaret();
        Element root = doc.getDefaultRootElement();
        int mark = caret.getMark();
        int dot = caret.getDot();
        int start = min(mark, dot);
        int end = max(mark, dot);
        int lineStart = root.getElementIndex(start);
        int lineEnd = root.getElementIndex(end);
        int pos_start = root.getElement(lineStart).getStartOffset();
        int pos_end = min(root.getElement(lineEnd).getEndOffset(), doc.getLength());

        try {
            String text = doc.getText(pos_start,pos_end - pos_start);
            String[] lines = text.split("\n");
            String[] transformText = Arrays.stream(lines).distinct().toArray(String[]::new);
            StringBuilder transformed = new StringBuilder();
            for(String line : transformText) {
                transformed.append(line + "\n");
            }
            doc.remove(pos_start, pos_end - pos_start);
            doc.insertString(pos_start, transformed.toString(), null);
        } catch (BadLocationException ignored) {
            showErr("Error transforming text");
        }
    }

    private void sortAsc() {

        if (this.docModel.getCurrentDocument() == null) {
            return;
        }
        Locale locale = new Locale(this.formProvider.getCurrentLanguage());
        Collator c = Collator.getInstance(locale);
        JTextComponent textComponent = this.docModel.getCurrentDocument().getTextComponent();
        Document doc = textComponent.getDocument();
        Caret caret = textComponent.getCaret();
        Element root = doc.getDefaultRootElement();
        int mark = caret.getMark();
        int dot = caret.getDot();
        int start = min(mark, dot);
        int end = max(mark, dot);
        int lineStart = root.getElementIndex(start);
        int lineEnd = root.getElementIndex(end);
        int pos_start = root.getElement(lineStart).getStartOffset();
        int pos_end = min(root.getElement(lineEnd).getEndOffset(), doc.getLength());

        try {
            String text = doc.getText(pos_start,pos_end - pos_start);
            String[] lines = text.split("\n");
            Arrays.sort(lines, c);
            StringBuilder transformed = new StringBuilder();
            for(String line : lines) {
                transformed.append(line + "\n");
            }
            doc.remove(pos_start, pos_end - pos_start);
            doc.insertString(pos_start, transformed.toString(), null);
        } catch (BadLocationException ignored) {
            showErr("Error transforming text");
        }
    }

    /**
     * sort selected lines
     */
    private void sortDesc() {
        if (this.docModel.getCurrentDocument() == null) {
            return;
        }
        Locale locale = new Locale(this.formProvider.getCurrentLanguage());
        Collator c = Collator.getInstance(locale);
        JTextComponent textComponent = this.docModel.getCurrentDocument().getTextComponent();
        Document doc = textComponent.getDocument();
        Caret caret = textComponent.getCaret();
        Element root = doc.getDefaultRootElement();
        int mark = caret.getMark();
        int dot = caret.getDot();
        int start = min(mark, dot);
        int end = max(mark, dot);
        int lineStart = root.getElementIndex(start);
        int lineEnd = root.getElementIndex(end);
        int pos_start = root.getElement(lineStart).getStartOffset();
        int pos_end = min(root.getElement(lineEnd).getEndOffset(), doc.getLength());

        try {
            String text = doc.getText(pos_start,pos_end - pos_start);
            String[] lines = text.split("\n");
            Arrays.sort(lines, c.reversed());
            StringBuilder transformed = new StringBuilder();
            for(String line : lines) {
                transformed.append(line + "\n");
            }
            doc.remove(pos_start, pos_end - pos_start);
            doc.insertString(pos_start, transformed.toString(), null);
        } catch (BadLocationException ignored) {
            showErr("Error transforming text");
        }
    }

    /**
     * invert case
     */
    private void invertCase() {

        JTextComponent text = this.docModel.getCurrentDocument().getTextComponent();
        if (text.getSelectedText() == null) {
            return;
        }
        char[] selection = text.getSelectedText().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : selection) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            }
            else if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            }
            else {
                sb.append(c);
            }
        }
        text.replaceSelection(sb.toString());
    }

    /**
     * to uppercase
     */
    private void toLC() {
        JTextComponent text = this.docModel.getCurrentDocument().getTextComponent();
        if (text.getSelectedText() == null) {
            return;
        }
        char[] selection = text.getSelectedText().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : selection) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        text.replaceSelection(sb.toString());
    }

    /**
     * to lowercase
     */
    private void toUC() {

        JTextComponent text = this.docModel.getCurrentDocument().getTextComponent();
        if (text.getSelectedText() == null) {
            return;
        }
        char[] selection = text.getSelectedText().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : selection) {
            if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            }
            else {
                sb.append(c);
            }
        }
        text.replaceSelection(sb.toString());
    }

    /**
     * Attempts to save current document when save as key is pressed
     * @param model
     */
    private void saveAsAction(SingleDocumentModel model){
        JFileChooser jfc = new JFileChooser();

        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            if (Files.exists(jfc.getSelectedFile().toPath())) {
               JOptionPane.showConfirmDialog(this, formProvider.getString("overwriteExisting"),
                        formProvider.getString("question"), JOptionPane.YES_NO_OPTION);
            }
            try {
                docModel.saveDocument(model, jfc.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showErr(e.getMessage());
            }

        }

    }


    /**
     * sets action on given key
     * @param action
     * @param keyStroke
     * @param keyEvent
     */
    private void setKeyAction(Action action, KeyStroke keyStroke, int keyEvent) {
        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        action.putValue(Action.MNEMONIC_KEY, keyEvent);
    }

    /**
     * closes current document
     */
    private void closeDocument() {

        SingleDocumentModel currentdocument = docModel.getCurrentDocument();

        if (currentdocument == null) {
            return;
        }
        if (currentdocument.isModified()) {
            try {
                String doctitle;
                if (currentdocument.getFilePath() != null) {
                    doctitle = currentdocument.getFilePath().getFileName().toString();
                } else {
                    doctitle = "<unnamed>";
                }
                String popUpMessage = String.format(formProvider.getString("discardChanges"), doctitle);
                int response = JOptionPane.showConfirmDialog(this, popUpMessage);
                if (response == JOptionPane.CANCEL_OPTION) {
                    throw new RuntimeException("Closing cancelled!");
                } else if (response == JOptionPane.NO_OPTION) { //dont discard
                    //try to save as

                    JFileChooser jfc = new JFileChooser();

                    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        if (Files.exists(jfc.getSelectedFile().toPath())) {
                            int result = JOptionPane.showConfirmDialog(this,
                                    formProvider.getString("overwriteExisting"),
                                    formProvider.getString("question"), JOptionPane.YES_NO_OPTION);
                            //something missing?
                        }
                        try {
                            this.docModel.saveDocument(currentdocument, jfc.getSelectedFile().toPath());
                        } catch (RuntimeException e) {
                            throw new RuntimeException("'Saving as' failed.");
                        }
                    }
                    this.docModel.closeDocument(currentdocument);
                } else if (response == JOptionPane.YES_OPTION) {
                    this.docModel.closeDocument(currentdocument); //discard
                }
            }catch (Exception e){
                showErr(e.getMessage());
            }
        }else{
            docModel.closeDocument(currentdocument);
        }
    }


    /**
     * counts number of characters, number of rows and number of nonSpaceCharacters
     */
    private void showStatistics() {
        SingleDocumentModel currentDocument = this.docModel.getCurrentDocument();
        JTextComponent text = currentDocument.getTextComponent();


        int numOfNonBlankChars = text.getText().replaceAll("\\s", "").length();
        int rows = text.getDocument().getDefaultRootElement().getElementCount();
        int numOfAllChars = text.getText().length();

        String report = String.format(this.formProvider.getString("statsReport"),
                numOfAllChars, numOfNonBlankChars, rows);
        //////////////////////////
        JOptionPane.showMessageDialog(this, report , this.formProvider.getString("statistics"),
                JOptionPane.INFORMATION_MESSAGE);

    }


    /**
     * opens file chooser and loads document from disk
     */
    private void openExistingDocument() {

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                this.docModel.loadDocument(fileChooser.getSelectedFile().toPath());
            }
            catch (RuntimeException e) {
                showErr(e.getMessage());
            }
        }
    }

    /**
     * creates menubar with all actions listed
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu1 = new LocalizableJMenu("File", formProvider);
        menu1.add(openExistingDocumentAction);
        menu1.add(createNewDocumentAction);
        menu1.addSeparator();
        menu1.add(saveAction);
        menu1.add(saveAsAction);
        menu1.addSeparator();
        menu1.add(closeAction);
        menu1.add(exitAppAction);

        JMenu menu2 = new LocalizableJMenu("Edit", formProvider);
        menu2.add(copyAction);
        menu2.add(pasteAction);
        menu2.add(cutAction);

        JMenu menu3 = new LocalizableJMenu("Tools", formProvider);
        JMenu submenu = new LocalizableJMenu("Change_Case", this.formProvider);
        submenu.add(toLCAction);
        submenu.add(toUCAction);
        submenu.add(invTextAction);
        menu3.add(submenu);

        JMenu menu6 = new LocalizableJMenu("Sort", formProvider);
        menu6.add(sortAscAction);
        menu6.add(sortDescAction);
        menu6.add(uniqueAction);

        JMenu menu4 = new LocalizableJMenu("Statistics", formProvider);
        menu4.add(showStatsAction);

        JMenu menu5 = new LocalizableJMenu("Language", formProvider);
        menu5.add(switchToHr);
        menu5.add(switchToEng);
        menu5.add(switchToDe);


        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu5);
        menuBar.add(menu6);
        menuBar.add(menu4);



       setJMenuBar(menuBar);

    }

    /**
     * creates toolbar with all actions listed
     * @return
     */
    private JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(openExistingDocumentAction);
        toolBar.add(createNewDocumentAction);
        toolBar.add(saveAction);
        toolBar.add(saveAsAction);
        toolBar.add(closeAction);
        toolBar.add(exitAppAction);

        toolBar.addSeparator();
        toolBar.add(copyAction);
        toolBar.add(pasteAction);
        toolBar.add(cutAction);

        toolBar.addSeparator();
        toolBar.add(toLCAction);
        toolBar.add(toUCAction);
        toolBar.add(invTextAction);
        toolBar.add(sortAscAction);
        toolBar.add(sortDescAction);
        toolBar.add(uniqueAction);

        toolBar.addSeparator();
        toolBar.add(showStatsAction);

        toolBar.addSeparator();
        toolBar.add(switchToHr);
        toolBar.add(switchToEng);
        toolBar.add(switchToDe);

        return toolBar;
    }

    /**
     * creates a status bar with statistics and time
     * @return
     */
    private JPanel getStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
        statusBar.add(getStats(), BorderLayout.LINE_START);
        statusBar.add(new ClockPanel(),BorderLayout.CENTER);

        return statusBar;
    }


    /**
     * returns panel showing statistics
     * @return
     */
    private JPanel getStats() {

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(1,2));
        JLabel documentPosition = new JLabel("");
        JLabel caretPosition = new JLabel(""); //poravnaj

        DocumentListener  documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDocumentPosition(documentPosition);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDocumentPosition(documentPosition);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDocumentPosition(documentPosition);
            }
        };

        CaretListener clistener = new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateCaretPosition(caretPosition);
            }
        };

        this.formProvider.addLocalizationListener(() -> updateDocumentPosition(documentPosition));
        this.formProvider.addLocalizationListener(() -> updateCaretPosition(caretPosition));

        this.docModel.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (previousModel != null) {
                    previousModel.getTextComponent().removeCaretListener(clistener);
                    previousModel.getTextComponent().getDocument().removeDocumentListener(documentListener);
                }

                if (currentModel != null) {
                    currentModel.getTextComponent().addCaretListener(clistener);
                    currentModel.getTextComponent().getDocument().addDocumentListener(documentListener);
                }
                updateDocumentPosition(documentPosition);
                updateCaretPosition(caretPosition);
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });

        documentPosition.setBorder(BorderFactory.createLineBorder(Color.black));
        statsPanel.add(documentPosition);
        caretPosition.setBorder(BorderFactory.createLineBorder(Color.black));
        statsPanel.add(caretPosition);

        return statsPanel;

    }

    /**
     * updates length of current document in statistics label
     * @param documentPosition
     */
    private void updateDocumentPosition(JLabel documentPosition) {
        SingleDocumentModel current = this.docModel.getCurrentDocument();

        if(current == null){
            documentPosition.setText(""); //or empty??
            return;
        }

        String docLength = String.valueOf(current.getTextComponent().getText().length()); //len of current text
        documentPosition.setText(String.format(formProvider.getString("documentStats"), docLength)); //sets text
    }


    /**
     * updates caret position on position of caret in current document
     *
     * @param caretPosition
     */
    private void updateCaretPosition(JLabel caretPosition) {
        SingleDocumentModel current = this.docModel.getCurrentDocument();

        if(current == null){
            caretPosition.setText(""); //or empty??
            return;
        }

        JTextComponent text = current.getTextComponent();
        Document doc = text.getDocument();
        Element root = doc.getDefaultRootElement();

        int row = root.getElementIndex(text.getCaretPosition()) + 1;
        int col = text.getCaretPosition() - root.getElement(row - 1).getStartOffset() + 1;

        int selectedTextLen = 0;
        if(text.getSelectedText() != null) selectedTextLen = text.getSelectedText().length();
        caretPosition.setText(String.format(formProvider.getString("caretStats"), row, col, selectedTextLen));

    }

    /**
     * helper class to showcase date and time
     */
    private static class ClockPanel extends JPanel {
        private JLabel clock;
        public ClockPanel() {
            setLayout(new BorderLayout());
            clock = new JLabel();
            clock.setHorizontalAlignment(JLabel.CENTER);
            tick();
            add(clock);

            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tick();
                }
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.setInitialDelay(0);
            timer.start();
        }

        private void tick() {
            clock.setText(DateFormat.getDateTimeInstance().format(new Date()));
        }
    }


    /**
     * sets new updated title as current viewed document changes
     * @param currentDocument
     */
    private void setPageTitle(SingleDocumentModel currentDocument) {
       String prevTitle = getGUItitle();
       if(currentDocument != null && currentDocument.getFilePath() != null){
           //has name
          setGUItitle(currentDocument.getFilePath().toAbsolutePath().toString() + " - JNotepad++");
       }else{
           setGUItitle("(unnamed)"+ " - " + "JNotepad++");
       }

       this.setGUItitle(getGUItitle());
    }

    public String getGUItitle() {
        return GUItitle;
    }

    public void setGUItitle(String newTitle){
       this.GUItitle = newTitle;
       this.setTitle(this.GUItitle);
    }

    /**
     * shuts down program
     * before shutting program, ask user to discard or save all opened tabs
     *
     */
    private void exitApp() {

        //try{
        SingleDocumentModel cur = docModel.getCurrentDocument();

            for(SingleDocumentModel doc: this.docModel){
                docModel.currentDocumentChanged(cur, doc);
                cur = doc;
                //docModel.closeDocument(doc);
                //closeAction.actionPerformed(null);
                SingleDocumentModel currentdocument = docModel.getCurrentDocument();

                if (currentdocument == null) {
                    return;
                }
                if (currentdocument.isModified()) {
                    try {
                        String doctitle;
                        if (currentdocument.getFilePath() != null) {
                            doctitle = currentdocument.getFilePath().getFileName().toString();
                        } else {
                            doctitle = "<unnamed>";
                        }
                        String popUpMessage = String.format(formProvider.getString("discardChanges"), doctitle);
                        int response = JOptionPane.showConfirmDialog(this, popUpMessage);
                        if (response == JOptionPane.CANCEL_OPTION) {
                            throw new RuntimeException("Closing cancelled!");
                        } else if (response == JOptionPane.NO_OPTION) { //dont discard
                            //try to save as

                            JFileChooser jfc = new JFileChooser();

                            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                                if (Files.exists(jfc.getSelectedFile().toPath())) {
                                    int result = JOptionPane.showConfirmDialog(this,
                                            formProvider.getString("overwriteExisting"),
                                            formProvider.getString("question"), JOptionPane.YES_NO_OPTION);
                                    //something missing?
                                }
                                try {
                                    this.docModel.saveDocument(currentdocument, jfc.getSelectedFile().toPath());
                                } catch (RuntimeException e) {
                                    throw new RuntimeException("'Saving as' failed.");
                                }
                            }
                            //this.docModel.closeDocument(currentdocument);
                        } else if (response == JOptionPane.YES_OPTION) {
                            //this.docModel.closeDocument(currentdocument); //discard
                        }
                    }catch (Exception e){
                        showErr(e.getMessage());
                    }
                }else{
                    //docModel.closeDocument(currentdocument);
                }
            }

            this.dispose();
            System.exit(1);

        //}catch(Exception e){
            //System.out.println("problemi");
            //showErr(e.getMessage());
        //}
    }

    /**
     * Creates a JOptionPane with an error message
     * @param message
     */
    private void showErr(String message) {
        JOptionPane.showMessageDialog(this, message, formProvider.getString("errorMessage"),
                JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

}
