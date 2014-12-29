package repository;

/**
 * A sample listener listens to events about a specific sound sample.
 * @author Benedikt Ringlein
 *
 */
public interface SampleListener {

	SoundSample getSample();
	void sheduledSample();
	void playedSample();
	void stoppedSample();
	
}
