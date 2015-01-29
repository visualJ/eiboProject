package presentation;

import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import repository.BeatListener;
import services.AudioCore;
import services.Preferences;

/**
 * A button that starts and stops performance recording
 * @author Benedikt Ringlein
 *
 */
public class RecordingButton extends KeyButton {
	
	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private Color normal = new Color(240,80,20);
	private Color glowing = new Color(240,40,20);

	public RecordingButton(String keyLabel, int keyCode, AudioCore audioCore) {
		super(keyLabel, keyCode);
		this.audioCore = audioCore;
		setBackground(normal);
		setBigIcon(UserInterface.recIcon);
		setToolTipText("Audiodatei aufnehmen");
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				if(RecordingButton.this.audioCore.isPerformanceRecording()){
					
					// Stop the recording
					RecordingButton.this.audioCore.endRecordingPerformance();
				}else{
					String fileName = Preferences.getInstance().getRecordFolder() + File.separator +"recording_" + (new SimpleDateFormat("YYYYMMDD_HHmmss").format(new Date())) + ".wav";
					RecordingButton.this.audioCore.startRecordingPerformance(fileName);
				}
			}
		});
		
		this.audioCore.addBeatListener(new BeatListener() {
			
			@Override
			public void bpmChanged(int bpm) {
			}
			
			@Override
			public void beat(int beatInBar) {
				// When recording, let the button blink
				if(RecordingButton.this.audioCore.isPerformanceRecording() && getBackground().equals(normal)){
					setBackground(glowing);
				}else{
					setBackground(normal);
				}
			}
		});
	}

}
