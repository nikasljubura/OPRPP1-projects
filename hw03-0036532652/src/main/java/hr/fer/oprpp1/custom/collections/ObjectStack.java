package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.EmptyStackException;


/**
 * Adaptor top-level class that acts as an interface which enables the user to use the stack
 * but has underlying ArrayIndexedCollection to support its use, using delegation
 */
public class ObjectStack<E> {

    private ArrayIndexedCollection<E> stack;

    /**
     * creates an instance of arrayIndexedCollection which will act as stack
     */
    public ObjectStack() {
        this.stack  = new ArrayIndexedCollection<E>();
    }

    /**
     *
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    /**
     *
     * @return size of the stack
     */
    public int size() {
        return this.stack.size();
    }

    /**
     * pushes the object on top of the stack using the underlying add method from arrayindexed collection
     * @param value of the element we push on stack
     */
    public void push(E value) {

        this.stack.add(value);
    }

    /**
     * removes last value pushed on stack and returns it
     * @return last object stored on stack
     * @throws EmptyStackException
     */
    public E pop() {

        if(this.stack.size() == 0) throw new EmptyStackException("The stack is empty!");

        E o = this.stack.get(this.stack.size() - 1);
        this.stack.remove(this.stack.size() - 1);

        return o;
    }

    /**
     * returns last value stored on stack
     * @return last object stored on stack
     * @throws EmptyStackException
     */
    public E peek() {

        if(this.stack.size() == 0) throw new EmptyStackException("The stack is empty!");

        return this.stack.get(this.stack.size() - 1);
    }

    /**
     * clears the stack
     */
    public void clear() {
        this.stack.clear();
    }


}