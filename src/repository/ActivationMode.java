package repository;

/**
 * An activation mode describes how a sound is triggered 
 * @author Benedikt Ringlein
 *
 */
public enum ActivationMode {
	/**
	 * In this mode, the sound is looped when triggered. 
	 * If it is untriggered, it immediately stops.
	 */
	WHILE_TRIGGERED, 
	
	/**
	 * Triggering in this mode will start or stop the sound playback.
	 * The sound loops. And it finishes playing first, when untriggered.
	 */
	LOOP, 
	
	/**
	 * In this mode, the sound playes once when triggered and stops
	 * when finished, even if it is still triggered.
	 * When untriggered, the sound keeps playing, until it finished.
	 */
	PLAY_ONCE;
}
