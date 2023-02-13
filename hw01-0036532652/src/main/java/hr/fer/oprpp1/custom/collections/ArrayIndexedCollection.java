package hr.fer.oprpp1.custom.collections;

/**
 * Resizable array-backed collection of objects.
 * It has two private variables - size (number of elements stored in an array, and an array of Objects.
 * Duplicate elements are allowed; storage of null references is not allowed.
 */
public class ArrayIndexedCollection extends Collection {

    private int size; //number of elements stored in elements array
    private Object[] elements;

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
        this.elements = new Object[initialCapacity];

    }

    /**
     * Second constructor which passes this Collection to the constructor variation #2 to construct the object
     * @param other
     */
    public ArrayIndexedCollection(Collection other) { //second constructor

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
    public ArrayIndexedCollection(Collection other, int initialCapacity) { //variation #2

        if(other == null) throw new NullPointerException();

        if(initialCapacity < other.size()){
            this.size = other.size();
            this.elements = new Object[other.size()];
        }else{
            this.size = initialCapacity;
            this.elements = new Object[initialCapacity];
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
    public void add(Object value) {

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
            this.elements = newArr;

            this.elements[i] = value;
            this.size *= 2;

        }

    }

    /**
     * @param index of an element which we want to return
     * @return returns element at the given index
     * avg complexity = O(n)
     * @throws IndexOutOfBoundsException
     */
    public Object get(int index){

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

    public void insert(Object value, int position) {

        if(value ==  null) throw new NullPointerException();
        if(position < 0 || position > this.size()) throw new IndexOutOfBoundsException();
        if(position == this.size()){
            add(value);
        }else {
            int old_size = this.size(); //old size -> before adding
            add(this.elements[old_size-1]); //adding the last element on the first free spot

            int difference = old_size - position - 1; //how many elements to shift

            for(int i = 0; i < difference; i++){
                this.elements[old_size - i - 1] = this.elements[old_size - 2 - i];
            }

            this.elements[position] = value;
        }



    }

    /**
     * @param value the element whose index we wish to return
     * @return the index of the passed objecthttps://www.fer.unizg.hr/predmet/web1/materijali
     * avg complexity = O(n)
     */

    public int indexOf(Object value) {

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
    public Object[] toArray() {

        Object[] arr = new Object[this.size()];
        for(int i = 0; i < this.size; i++){
            if(this.elements[i] != null){
                arr[i] = this.elements[i];
            }
        }
        return arr;
    }


    /**
     * method calls processor.process(.) for each element of this collection
     * @param processor which we want to perform on each element
     */
     @Override
    public void forEach(Processor processor) {

        for(int i = 0; i < this.size(); i++){
            processor.process(this.elements[i]);
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
            return true;
        }

        return false;
    }


}
