package hr.fer.oprpp1.custom.collections;

import java.util.List;

/**
 * Implementation of linked list backed collection of objects.
 *
 */

public class LinkedListIndexedCollection extends Collection {

    /**
     * Private static class that defines a LIstNode, which is an Object of a Linked List.
     *
     */
    private static class ListNode{

        Object value; //value of stored object
        ListNode prev; //previous node
        ListNode next; //next node

    }

    private int size;
    private ListNode first;
    private ListNode last;

    /**
     * Creates an empty List with first and last nodes pointing to null
     */
    public LinkedListIndexedCollection () {

        this.first = this.last = null;
    }


    /**
     *
     * @return size of the linked list, size increments by each element we add in the linked list
     */
    @Override
    public int size(){
        return this.size;
    }

    /**
     * @param other reference to some other Collection whose elements are copied to this class
     */
    public LinkedListIndexedCollection (Collection other){

        //implement .....
        this.addAll(other);
    }

    /**
     * @throws NullPointerException
     * @param value which we want to add into collection
     */
    @Override
    public void add(Object value){

        ListNode newNode = new ListNode();
        if(value ==  null) throw new NullPointerException();

        newNode.value = value;
        newNode.next = null;
        newNode.prev = this.last; //tail

        if(this.last == null){
            //tail is null
            this.first = newNode; //head
        }else{
            this.last.next = newNode;
        }

        this.last = newNode;
        this.size++;

    }

    /**
     * Searches the linked list depending on the closeness of the wanted object in regards to head and tail
     * @param index of the element we are trying to get
     * @return value of the object we get
     * @throws IndexOutOfBoundsException
     */
    public Object get(int index){

        ListNode theNode = new ListNode();
        if(index < 0 || index > (this.size() -1 )) {
            throw new IndexOutOfBoundsException();
        }else{
            if(this.size() / 2 >= index){
                //the element is closer to head, search from head
                theNode = this.first;
                for(int i = 0; i < index; i++){
                    theNode = theNode.next;
                }
            }else{
                //the element is closer to tail, search from tail
                theNode = this.last;
                for(int i = this.size - 1; i > index; i--){
                    theNode = theNode.prev;
                }
            }
        }
        return theNode.value;
    }


    /**
     * Clears the linked list
     */
    @Override
    public void clear(){

        this.first = this.last = null;
        this.size = 0;
    }


    /**
     * Inserts the object into sorted list onto wanted position with regards to searching from head or tail
     * Time complexity -> n/2 + 1
     * @param value of the object we want to insert into the list
     * @param position of the object where we weant to insert it
     * @throws IndexOutOfBoundsException
     */
    public void insert(Object value, int position){

        if(value ==  null) throw new NullPointerException();
        if(position < 0 || position > this.size()) throw new IndexOutOfBoundsException();

        ListNode theNode = new ListNode();

        if(position == size){
            add(value);
        }else{
            if(this.size() / 2 >= position){
                //the element is closer to head, search from head
                theNode = this.first;
                for(int i = 0; i < position; i++){
                    theNode = theNode.next;
                }
            }else{
                //the element is closer to tail, search from tail
                theNode = this.last;
                for(int i = this.size - 1; i > position; i--){
                    theNode = theNode.prev;
                }
            }

            ListNode newNode = new ListNode();
            newNode.value = value;

            newNode.next = theNode; //theNode pointed to the wanted position, he is shifted now to the right
            newNode.prev = theNode.prev;

            if(position != 0){
                theNode.prev.next = newNode; // back connect
                theNode.prev = newNode;
            }else{
                this.first = newNode;
            }


        }

        this.size++;

    }

    /**
     * @param value the element whose index we wish to return
     * @return the index of the passed objecthttps://www.fer.unizg.hr/predmet/web1/materijali
     * avg complexity = O(n)
     */

    public int indexOf(Object value) {

        int i = 0;
        ListNode newNode = new ListNode();
        if(value == null){
            System.out.println("The element cannot be found");
            return -1;
        } else {

            for(newNode = this.first; !newNode.value.equals(value) && newNode != null; newNode = newNode.next){
                i++;
            }

        }

        if(newNode != null){
            return i;
        }

        return -1;
    }


    /**
     * @throws IndexOutOfBoundsException
     * @param index
     */
    public void remove(int index){

        if(index < 0 || index > (this.size() - 1)) throw new IndexOutOfBoundsException();

        ListNode theNode = new ListNode();

        if(this.size() / 2 >= index){
            //the element is closer to head, search from head
            theNode = this.first;
            for(int i = 0; i < index; i++){
                theNode = theNode.next;
            }
        }else{
            //the element is closer to tail, search from tail
            theNode = this.last;
            for(int i = this.size - 1; i > index; i--){
                theNode = theNode.prev;
            }
        }


        theNode.prev.next = theNode.next;

        if(theNode.next != null){
            theNode.next.prev = theNode.prev;
        }else{
            this.last = theNode;
        }

        this.size--;
    }

    /**
     *
     * @return true if linked list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {

        if(this.size() > 0) return false;

        return true;
    }

    /**
     *
     * @param value which the collection containes
     * @return returns true only if the collection contains given value
     */
    @Override
    public boolean contains(Object value) {
        int i = 0;
        ListNode newNode = new ListNode();
        if(value == null){
            System.out.println("The element cannot be found");
            return false;
        } else {
            for(newNode = this.first; newNode != null && !newNode.value.equals(value); newNode = newNode.next){
                i++;
            }
        }
        if(newNode != null){
            return true;
        }
        return false;
    }


    /**
     *
     * @return allocates new array with size equals to the size of this collection
     * fills it with collection content and returns the array
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[this.size()];
        ListNode newNode = new ListNode();
        int i = 0;
        for(newNode = this.first; newNode != null; newNode = newNode.next){
            arr[i] = newNode.value;
            i++;
        }
        return arr;
    }

    /**
     * method calls processor.process(.) for each element of this collection
     * @param processor which we want to perform on each element
     */
    @Override
    public void forEach(Processor processor) {
        ListNode newNode = new ListNode();
        for(newNode = this.first; newNode != null; newNode = newNode.next){
           processor.process(newNode.value);
        }
    }


    /**
     *
     * @param value which we want to remove from the collection
     * @return returns true only if the collection contains given value and removes one instance of it
     */
    @Override
    public boolean remove(Object value) {

        if(this.contains(value)){
            int index = indexOf(value);
            remove(index);
            this.size--;
            return true;
        }
        return false;
    }


}

