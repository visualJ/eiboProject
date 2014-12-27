package repository;

import services.AudioCore;

public class ActivationModeBehavior {

	private static ActivationModeBehavior instance = null;
	
	private AudioCore audioCore;
	
	/**
	 * Get the singleton instance of the Behavior
	 * @param audioCore The audio core
	 * @return The instance
	 */
	public static ActivationModeBehavior getInstance(){
		if(instance == null){
			throw new NotInitializedException("ActivationModeBehavior");
		}
		return instance;
	}
	
	/**
	 * Initializes the Behavior with an AudioCore
	 * @param audioCore The AudioCore to use
	 */
	public static void init(AudioCore audioCore){
		instance = new ActivationModeBehavior(audioCore);
	}
	
	/**
	 * Triggers playing a sound (call this when a key is pressed, a button is clicked, etc.)
	 * @param mode The {@link ActivationMode} to use
	 * @param sample The sample to play
	 */
	public void trigger(ActivationMode mode, SoundSample sample){
		if(sample == null) return;
		switch (mode) {
		case LOOP:
			if(audioCore.isLoopPlaying(sample)){
				audioCore.stopLoop(sample);
			}else{
				audioCore.playLoop(sample);
			}
			break;
		case WHILE_TRIGGERED:
			audioCore.playSample(sample, true);
			break;
		case PLAY_ONCE:
		case WHILE_TRIGGERED_ONCE:
			audioCore.playSample(sample);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Untriggers playing a sound (call this when a key or a button is released etc.)
	 * Depending on the mode, this might not have any effect.
	 * @param mode The {@link ActivationMode} to use
	 * @param sample The sample to top
	 */
	public void untrigger(ActivationMode mode, SoundSample sample){
		if(sample == null) return;
		switch (mode) {
		case WHILE_TRIGGERED:
		case WHILE_TRIGGERED_ONCE:
			audioCore.stopSample(sample);
			break;
		default:
			break;
		}
	}
	
	private ActivationModeBehavior(AudioCore audioCore){
		this.audioCore = audioCore;
	}
}
