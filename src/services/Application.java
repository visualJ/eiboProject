package services;

import java.io.File;

import presentation.UserInterface;

/**
 * 
 * @author Ulrike Kocher, Nadine Goebertshan, Benedikt Ringlein, Patrik Pezelj
 *
 */
public class Application {

	public static void main(String[] args) {

		Preferences.getInstance().loadPreferences("." + File.separator);
		
		AudioCore audioCore = new AudioCore();
		audioCore.init();

		SoundPackManager soundpackManager = new SoundPackManager();
		soundpackManager.init();

		UserInterface ui = new UserInterface(audioCore, soundpackManager);
		ui.init();
	}

}
