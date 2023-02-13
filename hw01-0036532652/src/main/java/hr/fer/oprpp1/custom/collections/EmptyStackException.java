package hr.fer.oprpp1.custom.collections;

/**
 * Custom unchecked exception that is thrown when the stack is empty
 */
public class EmptyStackException extends RuntimeException {


    /**
     * default construction
     */
    public EmptyStackException() {

    }

    /**
     *
     * @param mess message that is passed when the exception is thrown
     */
    public EmptyStackException(String mess) {
        super(mess);
    }


    /**
     *
     * @param mess message that is passed when the exception is thrown
     * @param err error that caused the exception
     */
    public EmptyStackException(String mess, Throwable err) {
        super(mess, err);
    }


    /**
     *
     * @param err error that cause the exception
     */
    public EmptyStackException(Throwable err) {
        super(err);
    }


}
