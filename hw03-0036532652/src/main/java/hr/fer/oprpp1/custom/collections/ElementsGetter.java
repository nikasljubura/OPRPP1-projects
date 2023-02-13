package hr.fer.oprpp1.custom.collections;

/**
 * enables the user to get elements of every collection
 */
public interface ElementsGetter<E> {
    public boolean hasNextElement();
    public E getNextElement();

    public void processRemaining(Processor<? super E> p);

}
