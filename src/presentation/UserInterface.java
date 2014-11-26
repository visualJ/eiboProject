package presentation;

import javax.swing.JFrame;

import repository.SoundPack;
import repository.SoundPackLoadingFailedException;
import services.AudioCore;
import services.SoundPackManager;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private SoundPackManager soundpackManager;

	public UserInterface(AudioCore audioCore, SoundPackManager soundpackManager) {
		this.audioCore = audioCore;
		this.soundpackManager = soundpackManager;
	}

	public void init(){
		//TODO initialize the window and GUI components
		setTitle("eiboProject");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400); // setzt die Fenstergroesse auf 500 x 400 px
		setVisible(true);
		

	}
	
}
