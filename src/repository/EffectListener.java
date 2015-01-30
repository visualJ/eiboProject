package repository;

/**
 * An effect listener interface
 * To listen for events when effects get turned on or off 
 *
 */
public interface EffectListener {
	void effectOn(EffectMode effect);
	void effectOff(EffectMode effect);
}
