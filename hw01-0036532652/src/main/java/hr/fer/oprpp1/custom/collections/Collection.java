package hr.fer.oprpp1.custom.collections;

/**
 * A collection represents some general collection of objects.
 */
public class Collection {


    /**
     * protected default constructor
     */
    protected Collection() { //protected default constructor

    }

    /**
     *
     * @return returns true if collection containes objects, false otherwise
     */

    public boolean isEmpty() {

        if(this.size() > 0) return false;

        return true;
    }

    /**
     * @return returns the size of a collction
     */
    public int size() {
        return 0;
    }

    /**
     * Adds the given Object to the collection
     * @param value which we want to add into collection
     */

    public void add(Object value) {

    }

    /**
     *
     * @param value which the collection containes
     * @return returns true only if the collection contains given value
     */

    public boolean contains(Object value) {
        return false;
    }

    /**
     *
     * @param value which we want to remove from the collection
     * @return returns true only if the collection contains given value and removes one instance of it
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     *
     * @return allocates new array with size equals to the size of this collection
     * fills it with collection content and returns the array
     * @throws UnsupportedOperationException
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * method calls processor.process(.) for each element of this collection
     * @param processor which we want to perform on each element
     */
    public void forEach(Processor processor) {
    }

    /**
     * method adds into the current collection all elements from the given collection
     * @param other the collection from which we want to add all elements from
     */
    public void addAll(Collection other) {

        /**
         * Class which extends the Processor class, we use it to access its method process within the calling method
         */
        class CollectionProcessor extends Processor{

            public CollectionProcessor() {
                super();
            }

            /**
             *
             * @param value The passed value of an object
             */
            @Override
            public void process(Object value){
                add(value);
            }

        }

        other.forEach(new CollectionProcessor());

    }

    /**
     * removes all elements from collection
     */
    public void clear(){

    }


}
