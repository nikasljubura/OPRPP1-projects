package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Resizable array-backed collection of objects.
 * It has two private variables - size (number of elements stored in an array, and an array of Objects.
 * Duplicate elements are allowed; storage of null references is not allowed.
 */
public class ArrayIndexedCollection<E> implements List<E> {

    private int size; //number of elements stored in elements array
    private E[] elements; //mijenjano
    private long modificationCount = 0;

    /**
     * defualt constructor which delegates its construction process to variation #1 constructor below
     * and gives it a fixed size od 16 objects
     */

    public ArrayIndexedCollection() { //default constructor

        this(16);
    }

    /**
     * Throws an exception if an illegal initial size of array is passed
     * @param initialCapacity of the resizable array
     * @throws IllegalArgumentException
     */
    public ArrayIndexedCollection(int initialCapacity) { //variation #1

        if(initialCapacity < 1){
            throw new IllegalArgumentException();
        }

        this.size = initialCapacity;
        this.elements = (E[]) new Object[initialCapacity];

    }

    /**
     * Second constructor which passes this Collection to the constructor variation #2 to construct the object
     * @param other
     */
    public ArrayIndexedCollection(Collection<? extends E> other) { //second constructor

        this(other, other.size());

    }


    /**
     *if the initialCapacity is smaller than the size of the given collection, the size of
     * the given collection should be used for elements array preallocation. If the given collection is null, a
     * NullPointerException is thrown.
     * @param other some other Collection
     * @param initialCapacity the initial size of the collection
     * @throws NullPointerException
     */
    public ArrayIndexedCollection(Collection<? extends E> other, int initialCapacity) { //variation #2

        if(other == null) throw new NullPointerException();

        if(initialCapacity < other.size()){
            this.size = other.size();
            this.elements = (E[]) new Object[other.size()];
        }else{
            this.size = initialCapacity;
            this.elements = (E[]) new Object[initialCapacity];
        }

        this.addAll(other);

    }

    /**
     * adds given object into this collection
     * @param value which we want to add into collection
     * avg complexity = O(n)
     * @throws NullPointerException
     */
    @Override
    public void add(E value) {

        if(value ==  null) throw new NullPointerException();

        if(this.elements[this.size - 1] == null){//the array is not full -> add
            for(int i = 0; i < this.size; i++){
                if(this.elements[i] == null){
                    this.elements[i] = value;
                    break;
                }
            }
        }else{//the array is full -> reallocate and add

            Object[] newArr = new Object[this.size * 2];

            int i = 0;
            for(i = 0; i < this.size; i++){
                newArr[i] = this.elements[i];
            }
            this.elements =(E[]) newArr;

            this.elements[i] = value;
            this.size *= 2;

            modificationCount++; //................................................................

        }


    }

    /**
     * @param index of an element which we want to return
     * @return returns element at the given index
     * avg complexity = O(n)
     * @throws IndexOutOfBoundsException
     */
    public E get(int index){

        if(index < 0 || index > (this.size() -1 )) {
            throw new IndexOutOfBoundsException();
        }else{
            return this.elements[index];
        }
    }

    /**
     * removes all elements from collection
     */
    @Override
    public void clear() {

        for(int i = 0; i < this.size(); i++){
            this.elements[i] = null;
        }


        modificationCount++;//-------------------------------------------------

    }


    /**
     *
     * @return ElementsGetter object which will be used to get an Object of ArrayIndexedCollection
     */
    @Override
    public ElementsGetter<E> createElementsGetter() {


        return new ElementsGetterHelper(this);


    }




    /**
     * nested class used to get reference of the collection whose elements we will then get using its method
     * getNextElement
     */

    private static class ElementsGetterHelper<E> implements ElementsGetter<E> {

        private ArrayIndexedCollection<E> arr;
        private int counter; //internal counter
        private long savedModificationCount;

        /**
         * initializes counter to 0
         * @param arr reference to the ArrayIndexedCollection using the elements getter
         */
        private ElementsGetterHelper(ArrayIndexedCollection<E> arr){
            this.arr = arr;
            this.counter = 0;
            this.savedModificationCount = arr.modificationCount;
        }


        /**
         *
         * @return next element of the ARI
         */
        @Override
        public E getNextElement() {


            if(this.hasNextElement()){
                return arr.get(counter++);
            }else{
                throw new NoSuchElementException();
            }


        }

        /**
         * checks if the ARI has nextElement or no
         * @return true if ARI has next element, false otherwise
         * @throws  ConcurrentModificationException when the collection is modified
         */

        @Override
        public boolean hasNextElement() {

            if(this.savedModificationCount != arr.modificationCount){
                throw new ConcurrentModificationException();
            }

            if(this.counter <= arr.size() - 1) return true;
            return false;
        }


        /**
         *
         * @param p processor used on remaining objects in that collection
         */
        @Override
        public void processRemaining(Processor<? super E> p){

            for(int i = this.counter; i < arr.size(); i++){
                p.process(arr.get(i));
            }

        }

    }


    /**
     * Inserts the given value at the given position in array (observe that before actual
     * insertion elements at position and at greater positions must be shifted one place toward the end, so that an
     * empty place is created at position
     * @param value of the element we want to insert
     * @param position at which we want the element to be inserted
     * avg complexity =  O(n)
     * @throws IndexOutOfBoundsException
     */

    public void insert(E value, int position) {

        if(value ==  null) throw new NullPointerException();
        if(position < 0 || position > this.size()) throw new IndexOutOfBoundsException();
        if(position == this.size()){
            add(value);
            return;
        }else {
            int old_size = this.size(); //old size -> before adding
            add(this.elements[old_size-1]); //adding the last element on the first free spot

            int difference = old_size - position - 1; //how many elements to shift

            for(int i = 0; i < difference; i++){
                this.elements[old_size - i - 1] = this.elements[old_size - 2 - i];
            }

            this.elements[position] = value;
        }

        modificationCount++;//...............................................



    }

    /**
     * @param value the element whose index we wish to return
     * @return the index of the passed objecthttps://www.fer.unizg.hr/predmet/web1/materijali
     * avg complexity = O(n)
     */

    public int indexOf(E value) {

        int i = 0;
        if(value == null){
            System.out.println("The element cannot be found");
            return -1;
        } else {
            for(i = 0; i < this.size(); i++){
                if(value.equals(this.elements[i])) break;
            }
        }
        return i;
    }

    /**
     * removes element at specified index from collection
     * @param index of an element we want to remove
     * @throws IndexOutOfBoundsException
     */
    public void remove(int index){

        if(index < 0 || index > (this.size() - 1)) throw new IndexOutOfBoundsException();

        for(int i = this.size()-1; i > index; i--){
            this.elements[i-1] = this.elements[i];
        }

        this.elements[this.size()-1] = null;

        modificationCount++;//..............................................
    }


    /**
     *
     * @return returns true if collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {

        if(this.size() > 0) return false;

        return true;
    }

    /**
     * @return returns the size of a collction -> number of STORED Objects -> not nulls
     */
    @Override
    public int size() {
        int size = 0;
        for(int i = 0; i < this.size; i++){
            if(this.elements[i] != null) size++;

        }
        return size;
    }

    /**
     *
     * @param value which the collection containes
     * @return returns true only if the collection contains given value
     */
    @Override
    public boolean contains(Object value) {

        for(int i = 0; i < this.size(); i++){
            if(value.equals(this.elements[i])) return true;
        }

        return false;
    }


    /**
     *
     * @return allocates new array with size equals to the size of this collection
     * fills it with collection content and returns the array
     */
    @Override
    public E[] toArray() {

        E[] arr = (E[]) new Object[this.size()];
        for(int i = 0; i < this.size; i++){
            if(this.elements[i] != null){
                arr[i] = this.elements[i];
            }
        }
        return arr;
    }




    /**
     *
     * @param value which we want to remove from the collection
     * @return returns true only if the collection contains given value and removes one instance of it
     */
    @Override
    public boolean remove(E value) {

        if(this.contains(value)){
            int index = indexOf(value);
            remove(index);
            modificationCount++; //.................
            return true;
        }


        return false;
    }


}
