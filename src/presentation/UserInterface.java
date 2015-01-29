package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import repository.ActivationMode;
import repository.KeyMapping;
import repository.SoundPack;
import services.ActivationModeBehavior;
import services.AudioCore;
import services.EffectModeBehavior;
import services.SoundPackManager;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Map<ActivationMode, Image> ACTIVATION_MODE_ICONS = new HashMap<ActivationMode, Image>();
	
	public static Image recIcon;
	public static Image programmIconSmall;
	public static Image programmIconBig;
	public static Image recordingSampleIcon;
	public static Image recordingSampleRecordIcon;
	public static Image recordingSampleDeleteIcon;
	public static Image arrowUpIcon;
	public static Image arrowDonwIcon;
	public static Image settingsIcon;
	public static Image folderIcon;
	public static Image infoIcon;
	public static Image helpIcon;
	public static Image highpassIcon;
	public static Image lowpassIcon;
	public static Image delayIcon;
	
	private AudioCore audioCore;
	private SoundPackManager soundpackManager;
	private SoundPack currentSoundPack;
	private BackgroundPanel background;
	private MenuPanel upperPanel;
	private KeyPanel keys;

	/**
	 * Creates the user interface using the specified audio core and soundpack manager
	 * @param audioCore
	 * @param soundpackManager
	 */
	public UserInterface(AudioCore audioCore, SoundPackManager soundpackManager) {
		this.audioCore = audioCore;
		this.soundpackManager = soundpackManager;
	}

	/**
	 * Initializes the user interface and prepares everything for use.
	 */
	public void init(){
		
		//init the window
		setTitle("iBO");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 700);
		setMinimumSize(new Dimension(500, 500));
		
		try {
			// Set the system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		// Load icons
		loadIcons();
		
		// Set window icons
		List<Image> windowIcons = new ArrayList<Image>();
		windowIcons.add(programmIconSmall);
		windowIcons.add(programmIconBig);
		setIconImages(windowIcons);
		
		// Initialize the ActivationModeBehavior
		ActivationModeBehavior.init(audioCore);
		
		// Initialize the EffectModeBehavior
		EffectModeBehavior.init(audioCore);
		
		// Add the backgorund panel
		background = new BackgroundPanel(audioCore);
		background.setLayout(new GridBagLayout());
		add(background);
		
		// Create a constraints object to use for adding elements to the layout
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Add the keyboard
		keys = new KeyPanel(audioCore);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 1;
		background.add(keys, gbc);
		
		//upperPanel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.weightx = 1;
		upperPanel = new MenuPanel(soundpackManager, this);
		upperPanel.setVisible(true);
		background.add(upperPanel, gbc);
		
		// Make the frame visible
		setVisible(true);
	}
	
	/**
	 * Return the correspoding Image Icon
	 * @param mode The Activaiton Mode
	 * @return A Image
	 */
	public static Image getActivationModeImage(ActivationMode mode){
		return ACTIVATION_MODE_ICONS.get(mode);
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
	
	/**
	 *  loads the icons
	 * */
	private void loadIcons(){
		try {
			ACTIVATION_MODE_ICONS.put(ActivationMode.LOOP,ImageIO.read(UserInterface.class.getResourceAsStream("res/LOOP.png")));
			ACTIVATION_MODE_ICONS.put(ActivationMode.PLAY_ONCE,ImageIO.read(UserInterface.class.getResourceAsStream("res/PLAY_ONCE.png")));
			ACTIVATION_MODE_ICONS.put(ActivationMode.WHILE_TRIGGERED,ImageIO.read(UserInterface.class.getResourceAsStream("res/WHILE_TRIGGERED.png")));
			ACTIVATION_MODE_ICONS.put(ActivationMode.WHILE_TRIGGERED_ONCE,ImageIO.read(UserInterface.class.getResourceAsStream("res/WHILE_TRIGGERED_ONCE.png")));
			recIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/record.png"));
			programmIconSmall = ImageIO.read(UserInterface.class.getResourceAsStream("res/programmIconSmall.png"));
			programmIconBig = ImageIO.read(UserInterface.class.getResourceAsStream("res/programmIconBig.png"));
			recordingSampleIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/recordSample.png"));
			recordingSampleRecordIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/recordSampleRecord.png"));
			recordingSampleDeleteIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/recordSampleDelete.png"));
			arrowUpIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/arrowUp.png"));
			arrowDonwIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/arrow.png"));
			settingsIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Einstellungen.png"));
			folderIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Ordner.png"));
			infoIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Info.png"));
			helpIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Hilfe.png"));  
			highpassIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Highpass.png"));
			lowpassIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Lowpass.png"));
			delayIcon = ImageIO.read(UserInterface.class.getResourceAsStream("res/Delay.png"));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
}
