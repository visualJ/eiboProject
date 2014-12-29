package repository;

/**
 * A simple listener, that listenes to everything beat related.
 * @author Benedikt Ringlein
 *
 */
public interface BeatListener {

	void beat();
	void bpmChanged(int bpm);
	
}
