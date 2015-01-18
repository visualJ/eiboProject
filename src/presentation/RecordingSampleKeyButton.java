package presentation;

import java.awt.Color;

import repository.ActivationMode;
import repository.KeyMapping;
import repository.RecSampleModeListener;
import repository.RecordingSampleMode;
import repository.SoundSample;
import services.AudioCore;

/**
 * A button that can record an play samples
 * @author Benedikt Ringlein
 *
 */
public class RecordingSampleKeyButton extends SampleKeyButton {

	private static final long serialVersionUID = 1L;
	private SoundSample sample;
	private KeyPanel keyPanel;
	private Color colorEmpty = new Color(0,0,0);
	private Color colorHighlight = new Color(100,100,100);
	private Color colorWithSample = new Color(170,20,240);
	
	public RecordingSampleKeyButton(String keyLabel, int keyCode,
			AudioCore audioCore, KeyPanel keyPanel) {
		super(keyLabel, keyCode, audioCore);
		this.keyPanel = keyPanel;
		
		// Set the background color
		STANDART = colorEmpty;	
		setBackground(STANDART);
		
		// Set the trigger and untrigger methods
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				switch (RecordingSampleKeyButton.this.keyPanel.getRecSampleMode()) {
				case DELETING:
					if(sample != null){
						// Delete this buttons sample
						RecordingSampleKeyButton.this.audioCore.unloadSoundSample(sample);
						sample = null;
						setSmallIcon(null);
						STANDART = colorEmpty;
						setBackground(STANDART);
						setBigIcon(null);
					}
					break;
				case PLAYING:
					if(sample != null){
						// Play this buttons sample
						activationModeBehavior.trigger(ActivationMode.PLAY_ONCE, sample);
					}
					break;
				case RECORDING:
					// Start recording a sample
					RecordingSampleKeyButton.this.audioCore.startRecordingMic("samplerecording_" + RecordingSampleKeyButton.this.getKeyCode() + ".wav");
					setBigIcon(UserInterface.recordingSampleIcon);
					break;
				default:
					break;
				}
			}
		});
		
		setOnUntrigger(new Runnable() {
			
			@Override
			public void run() {
				switch (RecordingSampleKeyButton.this.keyPanel.getRecSampleMode()) {
				case DELETING:
					break;
				case PLAYING:
					if(sample != null){
						// Stop the sample
						activationModeBehavior.untrigger(ActivationMode.PLAY_ONCE, sample);
					}
					break;
				case RECORDING:
					// End the recording and make the sample available for this button
					sample = RecordingSampleKeyButton.this.audioCore.endRecordingMic();
					RecordingSampleKeyButton.this.setKeyMapping(new KeyMapping(RecordingSampleKeyButton.this.getKeyCode(), ActivationMode.PLAY_ONCE, sample, null));
					STANDART = colorHighlight;
					setBackground(STANDART);
					break;
				default:
					break;
				}
			}
		});
		
		// add a recSampleModeListener to the key panel
		this.keyPanel.addRecSampleModeListener(new RecSampleModeListener() {
			
			@Override
			public void recordingSampleModeChanged(RecordingSampleMode mode) {
				if(mode == RecordingSampleMode.RECORDING){
					// Highlight all buttons in recording mode
					STANDART = colorHighlight;
					setBackground(STANDART);
				}else if(mode == RecordingSampleMode.PLAYING){
					// while playing, show buttons with normal colors
					if(sample == null){
						STANDART = colorEmpty;
						setBackground(STANDART);
					}else{
						STANDART = colorWithSample;
						setBackground(STANDART);
					}
				}else{
					// while deleting, highlight non-empty buttons
					if(sample == null){
						STANDART = colorEmpty;
						setBackground(STANDART);
					}else{
						STANDART = colorHighlight;
						setBackground(STANDART);
					}
				}
			}
		});
	}

}
