package hr.fer.zemris.java.hw06.shell;

/**
 * An exception thrown when reading or writing fails
 */
public class ShellIOException extends RuntimeException {

    public ShellIOException(String mess){
        super(mess);
    }
}
