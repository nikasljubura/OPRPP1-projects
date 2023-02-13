package hr.fer.oprpp1.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/*+
    Models a list of generated prim numbers
 */
public class PrimListModel implements ListModel<Integer> {

    private List<Integer> primList;
    private List<ListDataListener> listeners;

    private int primNum;

    public PrimListModel(){
        this.primList = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.primNum = 0;
    }


    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return primList.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    @Override
    public Integer getElementAt(int index) {
        return primList.get(index);
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    @Override
    public void addListDataListener(ListDataListener l) {
            listeners.add(l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
    }

    public void next(){

        addNewPrime();

        for(ListDataListener listener: listeners){ //listeners subscribed to change
            ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
            primList.size()-1, primList.size()-1); //one number was added to the list end
            listener.intervalAdded(event);
        }
    }

    /**
     * adds next prime number to listr
     */
    private void addNewPrime(){

        boolean isPrime = checkIfPrime(this.primNum);
        this.primNum++;

        while(!checkIfPrime(this.primNum)){
            this.primNum++;
        }

        primList.add(this.primNum);
    }

    /**
     * checks if number is prime
     * @param number
     * @return
     */
    private boolean checkIfPrime(int number){
        boolean isPrime = true;

        if((number % 2) == 0 && number != 2){
            isPrime = false;
        }

        for(int i = 3; i <= (int) Math.ceil(Math.sqrt(number)); i += 2){
            if(number % i == 0){
                isPrime = false;
            }
        }

        return isPrime;
    }
}
