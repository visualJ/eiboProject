package services;

import presentation.MenuPanel;
import presentation.UserInterface;

public class Application {

	public static void main(String[] args) {
		AudioCore audioCore = new AudioCore();
		audioCore.init();
		
		SoundPackManager soundpackManager = new SoundPackManager();
		soundpackManager.init();
		
		UserInterface ui = new UserInterface(audioCore,soundpackManager);
		ui.init();
		
		MenuPanel menu = new MenuPanel();
	}

}
