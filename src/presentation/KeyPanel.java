package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import repository.KeyMapping;
import repository.SoundPack;
import services.AudioCore;

/**
 * This panel imitates an onscreen keysboard using keyButtons.
 * @author Benedikt Ringlein
 *
 */
public class KeyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, SampleKeyButton> buttons = new HashMap<Integer, SampleKeyButton>();
	private KeyButton recordingButton;
	private AudioCore audioCore;

	public KeyPanel(AudioCore audioCore){
		this.audioCore = audioCore;
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		// Add the keys
		addSampleKeyButton("Q", 81, 0, 0, 1, 1);
		addSampleKeyButton("W", 87, 1, 0, 1, 1);
		addSampleKeyButton("E", 69, 2, 0, 1, 1);
		addSampleKeyButton("R", 82, 3, 0, 1, 1);
		addSampleKeyButton("T", 84, 4, 0, 1, 1);
		addSampleKeyButton("Z", 90, 5, 0, 1, 1);
		addSampleKeyButton("U", 85, 6, 0, 1, 1);
		addSampleKeyButton("I", 73, 7, 0, 1, 1);
		addSampleKeyButton("O", 79, 8, 0, 1, 1);
		addSampleKeyButton("P", 80, 9, 0, 1, 1);
		addSampleKeyButton("Ü", 16777468, 10, 0, 1, 1);
		addSampleKeyButton("*", 521, 11, 0, 1, 1);
		addSampleKeyButton("", 10, 12, 0, 1, 2);
		
		addSampleKeyButton("A", 65, 0, 1, 1, 1);
		addSampleKeyButton("S", 83, 1, 1, 1, 1);
		addSampleKeyButton("D", 68, 2, 1, 1, 1);
		addSampleKeyButton("F", 70, 3, 1, 1, 1);
		addSampleKeyButton("G", 71, 4, 1, 1, 1);
		addSampleKeyButton("H", 72, 5, 1, 1, 1);
		addSampleKeyButton("J", 74, 6, 1, 1, 1);
		addSampleKeyButton("K", 75, 7, 1, 1, 1);
		addSampleKeyButton("L", 76, 8, 1, 1, 1);
		addSampleKeyButton("Ö", 16777430, 9, 1, 1, 1);
		addSampleKeyButton("Ä", 16777412, 10, 1, 1, 1);
		addSampleKeyButton("'", 520, 11, 1, 1, 1);
		
		addSampleKeyButton(">", 153, 0, 2, 1, 1);
		addSampleKeyButton("Y", 89, 1, 2, 1, 1);
		addSampleKeyButton("X", 88, 2, 2, 1, 1);
		addSampleKeyButton("C", 67, 3, 2, 1, 1);
		addSampleKeyButton("V", 86, 4, 2, 1, 1);
		addSampleKeyButton("B", 66, 5, 2, 1, 1);
		addSampleKeyButton("N", 78, 6, 2, 1, 1);
		addSampleKeyButton("M", 77, 7, 2, 1, 1);
		addSampleKeyButton(";", 44, 8, 2, 1, 1);
		addSampleKeyButton(":", 46, 9, 2, 1, 1);
		addSampleKeyButton("_", 45, 10, 2, 1, 1);
		addSampleKeyButton("^", 16, 11, 2, 2, 1);
		
		// Add special keys
		recordingButton = new RecordingButton("SPACE", 32, this.audioCore);
		addButton(3, 3, 5, 1, recordingButton);
	}
	
	/**
	 * Set the sound pack to associate with the key buttons
	 * @param soundPack The soundpack that conatins the key mappings to 
	 * use on the buttons
	 */
	public void setSoundPack(SoundPack soundPack){
		
		// Reset all the buttons first
		for(SampleKeyButton button:buttons.values()){
			button.setKeyMapping(null);
		}
		
		// Set all key mappings to corresponding key buttons
		for(KeyMapping mapping:soundPack.getKeyMappings()){
			if(buttons.containsKey(mapping.getKeyCode())){
				buttons.get(mapping.getKeyCode()).setKeyMapping(mapping);
			}
		}
	}
	
	/**
	 * Adds a key button to the panel
	 * @param label The buttons label
	 * @param keyCode The key code
	 * @param x Horizontal position in the grid
	 * @param y Vertical position in the grid
	 * @param width Width (in grid cells) of the key
	 * @param height Height (in grid cells) of the key
	 */
	private void addSampleKeyButton(String label, int keyCode, int x, int y, int width, int height){
		SampleKeyButton button = new SampleKeyButton(label, keyCode, audioCore);
		addButton(x, y, width, height, button);
		buttons.put(keyCode, button);
	}
	
	/**
	 * Adds a Button (or other {@link JComponent} to the panel)
	 * @param x Cell x position
	 * @param y Cell y positon
	 * @param width Button width in cells
	 * @param height Button height in cells
	 * @param button The JComponent to add
	 */
	private void addButton(int x, int y, int width, int height, JComponent button){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		add(button, gbc);
	}
	
}
