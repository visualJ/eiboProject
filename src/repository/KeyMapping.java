package repository;

/**
 * A keymapping defines what sound is triggered on which key and how.
 * @author Benedikt Ringlein
 *
 */
public class KeyMapping {

	private int keyCode;
	private ActivationMode activationMode;
	private SoundSample soundSample;
	
	public int getKeyCode() {
		return keyCode;
	}

	public ActivationMode getActivationMode() {
		return activationMode;
	}

	public SoundSample getSoundSample() {
		return soundSample;
	}

	/**
	 * Creates a new keymapping which links a key to a soundfile with an activation mode.
	 * @param keyCode The keycode of the key, which should trigger the sound
	 * @param activationMode The activation mode, how to trigger the sound
	 * @param soundSample The actual soundsample to play.
	 */
	public KeyMapping(int keyCode, ActivationMode activationMode, SoundSample soundSample) {
		this.keyCode = keyCode;
		this.activationMode = activationMode;
		this.soundSample = soundSample;
	}
	
	
}
