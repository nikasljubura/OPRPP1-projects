package hr.fer.oprpp1.custom.collections;


/**
 * A collection represents some general collection of objects.
 */
public interface Collection<E> {

 /*


    protected Collection() { //protected default constructor

    }
*/

    /**
     * default implementation
     * @return returns true if collection containes objects, false otherwise
     */

    public default boolean isEmpty() {

        if(this.size() > 0) return false;

        return true;
    }

    /**
     * @return returns the size of a collction
     */
    public int size();

    /**
     * Adds the given Object to the collection
     * @param value which we want to add into collection
     */

    public void add(E value);

    /**
     *
     * @param value which the collection containes
     * @return returns true only if the collection contains given value
     */

    public boolean contains(Object value);

    /**
     *
     * @param value which we want to remove from the collection
     * @return returns true only if the collection contains given value and removes one instance of it
     */
    public boolean remove(E value);

    /**
     *
     * @return allocates new array with size equals to the size of this collection
     * fills it with collection content and returns the array
     * @throws UnsupportedOperationException
     */
    public E[] toArray();

    /**
     * method calls processor.process(.) for each element of this collection
     * @param processor which we want to perform on each element
     */
    public default void forEach(Processor<? super E> processor){
        ElementsGetter<E> eg = this.createElementsGetter();

        while(eg.hasNextElement()){
            E nextElement = eg.getNextElement();
            processor.process(nextElement);
        }
    }

    /**
     * method adds into the current collection all elements from the given collection
     * @param other the collection from which we want to add all elements from
     */
    public default void addAll(Collection<? extends E> other) {

        /**
         * Class which extends the Processor class, we use it to access its method process within the calling method
         */
        class CollectionProcessor implements Processor<E>{

            public CollectionProcessor() {
                super();
            }

            /**
             *
             * @param value The passed value of an object
             */
            @Override
            public void process(E value){
                add(value);
            }

        }

        other.forEach(new CollectionProcessor());

    }

    /**
     * removes all elements from collection
     */
    public void clear();

    public ElementsGetter<E> createElementsGetter();

    // tester - super - we dont want to call a tester that tests variables that children have but parents dont have
    /**
     *
     * @param col currect collection
     * @param tester tests to see if objects are satisfying
     */
    public default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {

        ElementsGetter<? extends E> eg = col.createElementsGetter();

        while(eg.hasNextElement()){
            E nextElement = eg.getNextElement();
            if(tester.test(nextElement)) this.add(nextElement);
        }
    }


    public static void main(String[] args) {

        List col1 = new ArrayIndexedCollection();
        List col2 = new LinkedListIndexedCollection();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection col3 = col1;
        Collection col4 = col2;
        col1.get(0);
        col2.get(0);
        //col3.get(0); // neće se prevesti! Razumijete li zašto?
        //col4.get(0); // neće se prevesti! Razumijete li zašto?
        col1.forEach(System.out::println); // Ivana
        col2.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna

    }


}
