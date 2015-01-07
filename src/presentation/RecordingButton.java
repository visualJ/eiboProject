package presentation;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import repository.BeatListener;
import services.AudioCore;

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
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				if(RecordingButton.this.audioCore.isPerformanceRecording()){
					
					// Stop the recording
					RecordingButton.this.audioCore.endRecordingPerformance();
				}else{
					
					// Start recording to a new file
					try {
						String fileName = File.createTempFile("recording", ".wav",new File("./")).getPath(); 
						RecordingButton.this.audioCore.startRecordingPerformance(fileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		this.audioCore.addBeatListener(new BeatListener() {
			
			@Override
			public void bpmChanged(int bpm) {
			}
			
			@Override
			public void beat() {
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
