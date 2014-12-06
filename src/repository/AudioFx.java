package repository;

import ddf.minim.AudioOutput;
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
	private MoogFilter lowpassFilter = new MoogFilter(1000, 0.1f, MoogFilter.Type.LP);
	private MoogFilter highpassFilter = new MoogFilter(2200, 0.1f, MoogFilter.Type.HP);
	private float delTime;
	private boolean lowpass = false, highpass = false;
	
	public AudioFx(AudioOutput audioOutput){
		this.audioOutput = audioOutput;
		delTime = 1/audioOutput.sampleRate();
		delayFilter.setDelTime(delTime);
		patch(delayFilter).patch(audioOutput);
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
		delayFilter.setDelTime(delTime);
	}
	
	/**
	 * @return True, if the lowpass filter ist enabled
	 */
	public boolean isLowPass(){
		return lowpass;
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
			if(!lowpass){
				// Only do something, if not already enabled
				lowpass = true;
				if(highpass){
					// Disable the highpass filter first
					highpass = false;
					highpassFilter.unpatch(audioOutput);
					delayFilter.unpatch(highpassFilter);
					// Enable the lowpass filter now
					delayFilter.patch(lowpassFilter).patch(audioOutput);
				}else{
					// Just enable the lowpass filter
					delayFilter.unpatch(audioOutput);
					delayFilter.patch(lowpassFilter).patch(audioOutput);
				}
			}
		}else{
			if(lowpass){
				// Filter ausschalten, wenn eingeschaltet
				lowpass = false;
				lowpassFilter.unpatch(audioOutput);
				delayFilter.unpatch(lowpassFilter);
				delayFilter.patch(audioOutput);
			}
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
			if(!highpass){
				// Only do something, if not already enabled
				highpass = true;
				if(lowpass){
					// Disable the lowpass filter first
					lowpass = false;
					lowpassFilter.unpatch(audioOutput);
					delayFilter.unpatch(lowpassFilter);
					// Enable the highpass filter now
					delayFilter.patch(highpassFilter).patch(audioOutput);
				}else{
					// Just enable the highpass filter
					delayFilter.unpatch(audioOutput);
					delayFilter.patch(highpassFilter).patch(audioOutput);
				}
			}
		}else{
			if(highpass){
				// Filter ausschalten, wenn eingeschaltet
				highpass = false;
				highpassFilter.unpatch(audioOutput);
				delayFilter.unpatch(highpassFilter);
				delayFilter.patch(audioOutput);
			}
		}
	}
	
	/**
	 * @return True, if the highpass filter is enabled
	 */
	public boolean isHighPass(){
		return highpass;
	}
}