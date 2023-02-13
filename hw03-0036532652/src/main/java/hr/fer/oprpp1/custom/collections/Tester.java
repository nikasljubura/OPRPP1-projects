package hr.fer.oprpp1.custom.collections;

/**
 * we model objects that takes an objects and tests if the object is acceptable or not
 */
@FunctionalInterface
public interface Tester<E> {

    /**
     *
     * @param obj that we are testing
     * @return true if object is acceptable and false otherwise
     */
    public boolean test(E obj);

}
