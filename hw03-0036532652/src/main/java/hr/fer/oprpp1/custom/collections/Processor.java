package hr.fer.oprpp1.custom.collections;

/**
 * The Processor is a model of an object capable of performing some operation on the passed object.
 */
@FunctionalInterface

public interface Processor<E> {
  /*
    public Processor(){

    }

    /*

    /**
     * performs a selected operation on an passed object based on the class which is using it
     * @param value The passed value of an object
     */
    public void process(E value);

}
