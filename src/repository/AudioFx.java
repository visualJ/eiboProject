package repository;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.Summer;

/**
 * Processes audio with effects and is patched
 * to the standart output
 * @author Benedikt Ringlein
 *
 */
public class AudioFx extends Summer{
	
	private AudioOutput audioOutput;
	private Delay delay = new Delay();
	private float delTime;
	
	public AudioFx(AudioOutput audioOutput){
		this.audioOutput = audioOutput;
		delTime = 1/audioOutput.sampleRate();
		delay.setDelTime(delTime);
		patch(delay).patch(audioOutput);
	}
	
	/**
	 * 
	 * @return the delay time
	 */
	public float getDelTime(){
		return delTime;
	}
	
	/**
	 * Sets the delay time of the delay effect. The value
	 * should be between 0 and 1 (lowest value depends on sample rate of
	 * the audio output)
	 * @param delTime The new delay time
	 */
	public void setDelTime(float delTime){
		this.delTime = Math.max(delTime, 1/audioOutput.sampleRate());
		delay.setDelTime(delTime);
	}
}