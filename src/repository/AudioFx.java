package repository;

import ddf.minim.AudioOutput;
import ddf.minim.ugens.Bypass;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.MoogFilter;
import ddf.minim.ugens.Summer;

/**
 * Processes audio with effects and is patched
 * to the standart output by the AudioCore
 * @author Benedikt Ringlein
 *
 */
public class AudioFx extends Summer{
	
	private AudioOutput audioOutput;
	private Delay delayFilter = new Delay();
	private Bypass<MoogFilter> lowpassFilter = new Bypass<MoogFilter>(new MoogFilter(1000, 0.1f, MoogFilter.Type.LP));
	private Bypass<MoogFilter> highpassFilter = new Bypass<MoogFilter>(new MoogFilter(2200, 0.1f, MoogFilter.Type.HP));
	private float delTime;
	
	public AudioFx(AudioOutput audioOutput){
		this.audioOutput = audioOutput;
		delTime = 1/audioOutput.sampleRate();
		delayFilter.setDelTime(delTime);
		
		// DEactivate Lowpass und highpassfilter (activates bypass for those filters)
		lowpassFilter.activate();
		highpassFilter.activate();
		
		// Patch everything together, so the audio will be passed
		// thru the filter chain
		patch(delayFilter).patch(lowpassFilter).patch(highpassFilter).patch(audioOutput);
	}
	
	/**
	 * 
	 * @return the delay time
	 */
	public float getDelTime(){
		return delTime;
	}
	
	/**
	 * Returns, if the delay effect is on
	 * @return True, if delay effect is active
	 */
	public boolean isDelay(){
		return delTime > 1/audioOutput.sampleRate();
	}
	
	/**
	 * Sets the delay time of the delay effect. The value
	 * should be between 0 and 1 (lowest value depends on sample rate of
	 * the audio output)
	 * @param delTime The new delay time
	 */
	public void setDelTime(float delTime){
		this.delTime = Math.max(delTime, 1/audioOutput.sampleRate());
		delayFilter.setDelTime(this.delTime);
	}
	
	/**
	 * @return True, if the lowpass filter ist enabled
	 */
	public boolean isLowPass(){
		return !lowpassFilter.isActive();
	}
	
	/**
	 * Enables or disables the lowpass filter
	 * If the highpass filter is enabled, activating this disables
	 * the highpass filter.
	 * @param on Determines, if filter should be en- oder disabled
	 */
	public void setLowPass(boolean on){
		if(on){
			// Activate the low pass filter
			lowpassFilter.deactivate();
			highpassFilter.activate();
		}else{
			lowpassFilter.activate();
		}
	}
	
	/**
	 * Enables or disables the highpass filter
	 * If the lowpass filter is enabled, activating this disables
	 * the lowpass filter.
	 * @param on Determines, if filter should be en- oder disabled
	 */
	public void setHighPass(boolean on){
		if(on){
			// Activate the high pass filter
			highpassFilter.deactivate();
			lowpassFilter.activate();
		}else{
			highpassFilter.activate();
		}
	}
	
	/**
	 * @return True, if the highpass filter is enabled
	 */
	public boolean isHighPass(){
		return !highpassFilter.isActive();
	}
}