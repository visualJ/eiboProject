package presentation;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import services.AudioCore;

public class RecordingButton extends KeyButton {
	
	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;

	public RecordingButton(String keyLabel, int keyCode, AudioCore audioCore) {
		super(keyLabel, keyCode);
		this.audioCore = audioCore;
		setBackground(Color.red);
		
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
	}

}
