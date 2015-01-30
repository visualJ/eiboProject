package presentation;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import repository.RecordingListener;
import services.AudioCore;

/**
 * A button that opens the recording folder
 * @author Patrik Pezelj
 *
 */
public class RecordingFolderButton extends MenuButton {

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private final Color standart = new Color(100,100,100);
	private final Color blink = new Color(255,0,0);
	private Timer timer = new Timer();
	
	public RecordingFolderButton(String keyLabel, AudioCore audioCore) {
		super(keyLabel);
		this.audioCore = audioCore;
		
		this.audioCore.addRecordingListener(new RecordingListener() {
			
			@Override
			public void recordingStopped() {
				setBackground(blink);
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						setBackground(standart);
					}
				}, 500);
			}
			
			@Override
			public void recordingStarted() {
			}
		});
	}

}
