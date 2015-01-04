package presentation;

import java.awt.Color;

import javax.swing.ImageIcon;

import repository.KeyMapping;
import repository.SampleListener;
import repository.SoundSample;
import services.ActivationModeBehavior;
import services.AudioCore;

/**
 * A button that can display a key label, an activation mode icon and a sample icon.
 * You just have to give it a key mapping, and it does all the work, like playing samples when clicked.
 * @author Benedikt Ringlein
 *
 */
public class SampleKeyButton extends KeyButton {

	private static final long serialVersionUID = 1L;
	private static Color VIOLETT = new Color(170,20,240);
	private static Color ORANGE = new Color(240,170,20);
	private static Color GREEN = new Color(170,240,20);
	
	private KeyMapping keyMapping;
	private ActivationModeBehavior activationModeBehavior;
	private AudioCore audioCore;
	private SampleListener sampleListener = new SampleListener() {
		
		@Override
		public void stoppedSample() {
			setBackground(VIOLETT);
		}
		
		@Override
		public void sheduledSample() {
			setBackground(ORANGE);
		}
		
		@Override
		public void playedSample() {
			setBackground(GREEN);
		}
		
		@Override
		public SoundSample getSample() {
			return SampleKeyButton.this.keyMapping.getSoundSample();
		}

		@Override
		public void stoppedLoop() {
			setBackground(ORANGE);
		}
	};
	
	public SampleKeyButton(String keyLabel, int keyCode, AudioCore audioCore){
		super(keyLabel, keyCode);
		this.activationModeBehavior = ActivationModeBehavior.getInstance();
		this.audioCore = audioCore;
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				if(keyMapping != null){
					activationModeBehavior.trigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
		});
		setOnUntrigger(new Runnable() {
			
			@Override
			public void run() {
				if(keyMapping != null){
					activationModeBehavior.untrigger(keyMapping.getActivationMode(), keyMapping.getSoundSample());
				}
			}
		});

	}

	public KeyMapping getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
		
		// Remove the sample listener from the audio core
		if(sampleListener != null){
			audioCore.removeSampleListener(sampleListener);
		}
		
		setBackground(VIOLETT);
		
		if(keyMapping != null){
			
			// Set the icons
			setBigIcon(new ImageIcon(keyMapping.getImageFile()).getImage());
			setSmallIcon(UserInterface.getActivationModeImage(keyMapping.getActivationMode()));
			
			// Enable the button
			setEnabled(true);
			
			// Set the samplelistener in the audiocore
			audioCore.addSampleListener(sampleListener);
		}else{
			setBigIcon(null);
			setSmallIcon(null);
			
			// Disable the button
			setEnabled(false);
		}
	}
	
}
