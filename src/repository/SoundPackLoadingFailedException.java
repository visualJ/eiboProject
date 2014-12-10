package repository;

public class SoundPackLoadingFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public SoundPackLoadingFailedException(String name){
		super("Failed to load soundpack '" + name + "'.");
		//This is a test
	}
}
