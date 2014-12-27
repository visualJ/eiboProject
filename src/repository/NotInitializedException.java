package repository;

/**
 * This exception is thrown, when a class or factory is not initialized before use.
 * @author Benedikt Ringlein
 *
 */
public class NotInitializedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotInitializedException(String thing) {
		super(thing+" was not initialized!");
	}

}
