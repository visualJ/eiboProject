package repository;


/**
 * This exception is thrown, when loading a soundpack failed
 * @author Benedikt Ringlein
 *
 */
public class SoundPackLoadingFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public SoundPackLoadingFailedException(String name){
		super("Failed to load soundpack '" + name + "'.");
	}
}
