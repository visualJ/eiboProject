package presentation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import repository.ActivationModeBehavior;
import repository.KeyMapping;
import repository.SoundPack;
import services.AudioCore;
import services.SoundPackManager;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private SoundPackManager soundpackManager;
	private SoundPack currentSoundPack;
	private BackgroundPanel background;
	private KeyPanel keys;

	public UserInterface(AudioCore audioCore, SoundPackManager soundpackManager) {
		this.audioCore = audioCore;
		this.soundpackManager = soundpackManager;
	}

	public void init(){
		//TODO initialize the window and GUI components
		setTitle("eiboProject");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 700);
		
		// Initialize the ActivationModeBehavior
		ActivationModeBehavior.init(audioCore);
		
		background = new BackgroundPanel(audioCore);
		background.setLayout(new GridBagLayout());
		add(background);
		
		keys = new KeyPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.weightx = 1;
		JPanel upperPanel = new JPanel();
		upperPanel.setOpaque(false);
		background.add(upperPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 1;
		background.add(keys, gbc);
		
		// Make the frame visible
		setVisible(true);
		
		setSoundPack(soundpackManager.getSoundpacksInDirectory("./")[0]);
	}
	
	/**
	 * Set a SoundPack as the current one
	 * @param sp The Soundpack to use from now on
	 */
	public void setSoundPack(SoundPack sp){
		
		// Remove the old soundpack samples
		if(currentSoundPack!=null){
			for(KeyMapping k:currentSoundPack.getKeyMappings()){
				audioCore.unloadSoundSample(k.getSoundSample());
			}
		}
		
		// load the new soundpack samples
		for(KeyMapping k:sp.getKeyMappings()){
			audioCore.loadSoundSample(k.getSoundSample());
		}
		
		// set the bpm
		audioCore.setBpm(sp.getBpm());
		
		// set as current sound pack
		currentSoundPack = sp;
		
		// also, set the key panels soundpack
		keys.setSoundPack(sp);
	}
	
	/**
	 * Creates a new Color with an alpha value
	 * @param color The color to use
	 * @param alpha The alpha value the new color should have
	 * @return A new color with the specified alpha value
	 */
	public static Color alphaColor(Color color, float alpha){
		return new Color(color.getRed(),color.getGreen(),color.getBlue(),Math.round(255*alpha));
	}
	
}
