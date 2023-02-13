package hr.fer.oprpp1.gui.calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Models button whose operation changes when "Inv" operation is checked
 */

public class MyButton extends JButton implements ActionListener, ItemListener {

    private boolean buttonInverted; //state of button
    private String currentText; // text on button before it is inverted
    private String invertedText; //text on button after it is inverted
    private Runnable currentOperation; //operation to execute if the button is clicked (not inverted)
    private  Runnable invertedOperation; //operation to execute if button is clicked and inverted

    public MyButton(String currentText, String invertedText, Runnable currentOperation,
                    Runnable invertedOperation, Color buttonColor){

        this.setBackground(buttonColor);
        this.addActionListener(this);

        this.currentText = currentText;
        this.invertedText = invertedText;
        this.currentOperation = currentOperation;
        this.invertedOperation = invertedOperation;

        this.generateText(); //set text on button

    }



    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!this.buttonInverted){ //perform operation set on button
            this.currentOperation.run();
        }else{
            this.invertedOperation.run();
        }

    }

    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     *
     * @param e the event to be processed
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        this.buttonInverted = e.getStateChange() == ItemEvent.SELECTED;
        this.generateText(); //button changes text when Inv is selected.
    }

    /**
     * sets text on button (calls JButton setText method)
     */
    private void generateText() {
        if(!this.buttonInverted){
            setText(currentText);
        }else{
            setText(invertedText);
        }
    }
}
