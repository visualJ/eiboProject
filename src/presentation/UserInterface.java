package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
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
import services.Preferences;
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
	public static Image soundpackMenuIcon;
	
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
		upperPanel = new MenuPanel(soundpackManager, this, audioCore);
		upperPanel.setVisible(true);
		background.add(upperPanel, gbc);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				Preferences.getInstance().savePreferences("." + File.separator);
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
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
		
			ACTIVATION_MODE_ICONS.put(ActivationMode.LOOP,loadImage("res/LOOP.png"));
			ACTIVATION_MODE_ICONS.put(ActivationMode.PLAY_ONCE,loadImage("res/PLAY_ONCE.png"));
			ACTIVATION_MODE_ICONS.put(ActivationMode.WHILE_TRIGGERED,loadImage("res/WHILE_TRIGGERED.png"));
			ACTIVATION_MODE_ICONS.put(ActivationMode.WHILE_TRIGGERED_ONCE,loadImage("res/WHILE_TRIGGERED_ONCE.png"));
			recIcon = loadImage("res/record.png");
			programmIconSmall = loadImage("res/programmIconSmall.png");
			programmIconBig = loadImage("res/programmIconBig.png");
			recordingSampleIcon =loadImage("res/recordSample.png");
			recordingSampleRecordIcon = loadImage("res/recordSampleRecord.png");
			recordingSampleDeleteIcon = loadImage("res/recordSampleDelete.png");
			arrowUpIcon = loadImage("res/arrowUp.png");
			arrowDonwIcon = loadImage("res/arrow.png");
			settingsIcon =loadImage("res/Einstellungen.png");
			folderIcon = loadImage("res/Ordner.png");
			infoIcon = loadImage("res/Info.png");
			helpIcon = loadImage("res/Hilfe.png");  
			highpassIcon =loadImage("res/Highpass.png");
			lowpassIcon = loadImage("res/Lowpass.png");
			delayIcon = loadImage("res/Delay.png");
			soundpackMenuIcon = loadImage("res/Note.png");
		
	
	}
	private Image loadImage(String path){
		try {
			return ImageIO.read(UserInterface.class.getResourceAsStream(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
