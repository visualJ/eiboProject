package presentation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import repository.ActivationMode;
import repository.KeyMapping;
import repository.SoundPack;
import services.ActivationModeBehavior;
import services.AudioCore;
import services.SoundPackManager;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Map<ActivationMode, Image> ICONS = new HashMap<ActivationMode, Image>();
	
	private AudioCore audioCore;
	private SoundPackManager soundpackManager;
	private SoundPack currentSoundPack;
	private BackgroundPanel background;
	private KeyPanel keys;
	

	
	private final int windowWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private final int windowHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public UserInterface(AudioCore audioCore, SoundPackManager soundpackManager) {
		this.audioCore = audioCore;
		this.soundpackManager = soundpackManager;
	}

	/**
	 * Initializes the user interface and prepares everything for use.
	 */
	public void init(){
		
		
		setTitle("eiboProject");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowWidth, windowHeight);
		setUndecorated(true);
		

		
		// Load icons
		try {
			ICONS.put(ActivationMode.LOOP,ImageIO.read(UserInterface.class.getResourceAsStream("LOOP.png")));
			ICONS.put(ActivationMode.PLAY_ONCE,ImageIO.read(UserInterface.class.getResourceAsStream("PLAY_ONCE.png")));
			ICONS.put(ActivationMode.WHILE_TRIGGERED,ImageIO.read(UserInterface.class.getResourceAsStream("WHILE_TRIGGERED.png")));
			ICONS.put(ActivationMode.WHILE_TRIGGERED_ONCE,ImageIO.read(UserInterface.class.getResourceAsStream("WHILE_TRIGGERED_ONCE.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Initialize the ActivationModeBehavior
		ActivationModeBehavior.init(audioCore);
		
		background = new BackgroundPanel(audioCore);
		background.setLayout(new GridBagLayout());
		add(background);
		
		keys = new KeyPanel(audioCore);
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
	 * Return the correspoding Image Icon
	 * @param mode The Activaiton Mode
	 * @return A Image
	 */
	public static Image getActivationModeImage(ActivationMode mode){
		return ICONS.get(mode);
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
