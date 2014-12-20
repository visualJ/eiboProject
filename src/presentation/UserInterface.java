package presentation;

import javax.swing.JFrame;

import repository.BeatListener;
import repository.SoundPack;
import services.AudioCore;
import services.SoundPackManager;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private SoundPackManager soundpackManager;
	private BackgroundPanel background = new BackgroundPanel();
	private Thread backgroundUpdate = new Thread(){
		@Override
		public void run() {
			while(true){
				background.update(audioCore.getOutputLevel());
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
		}
	};

	public UserInterface(AudioCore audioCore, SoundPackManager soundpackManager) {
		this.audioCore = audioCore;
		this.soundpackManager = soundpackManager;
	}

	public void init(){
		//TODO initialize the window and GUI components
		setTitle("eiboProject");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400);
		setVisible(true);
		
		add(background);
		
		// Add a beat listener, so that the UI can react to beats
		audioCore.addBeatListener(new BeatListener() {
			@Override
			public void beat() {
				background.beat();
			}
			@Override
			public void bpmChanged(int bpm) {
				background.setBpm(bpm);
			}
		});
		
		// Set the bpm right
		background.setBpm(audioCore.getBpm());
		
		// Start updating the background
		backgroundUpdate.start();
	}
	
}
