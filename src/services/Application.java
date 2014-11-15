package services;

import presentation.UserInterface;

public class Application {

	public static void main(String[] args) {
		UserInterface ui = new UserInterface(new AudioCore(), new SoundPackManager());
		ui.init();
	}

}
