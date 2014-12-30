package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import repository.KeyMapping;
import repository.SoundPack;
import services.ActivationModeBehavior;
import services.AudioCore;

/**
 * This panel imitates an onscreen keysboard using keyButtons.
 * @author Benedikt Ringlein
 *
 */
public class KeyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, KeyButton> buttons = new HashMap<Integer, KeyButton>();
	private AudioCore audioCore;

	public KeyPanel(AudioCore audioCore){
		this.audioCore = audioCore;
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		// Add the keys
		addButton("Q", 81, 0, 0, 1, 1);
		addButton("W", 87, 1, 0, 1, 1);
		addButton("E", 69, 2, 0, 1, 1);
		addButton("R", 82, 3, 0, 1, 1);
		addButton("T", 84, 4, 0, 1, 1);
		addButton("Z", 90, 5, 0, 1, 1);
		addButton("U", 85, 6, 0, 1, 1);
		addButton("I", 73, 7, 0, 1, 1);
		addButton("O", 79, 8, 0, 1, 1);
		addButton("P", 80, 9, 0, 1, 1);
		addButton("Ü", 16777468, 10, 0, 1, 1);
		addButton("*", 521, 11, 0, 1, 1);
		addButton("", 10, 12, 0, 1, 2);
		
		addButton("A", 65, 0, 1, 1, 1);
		addButton("S", 83, 1, 1, 1, 1);
		addButton("D", 68, 2, 1, 1, 1);
		addButton("F", 70, 3, 1, 1, 1);
		addButton("G", 71, 4, 1, 1, 1);
		addButton("H", 72, 5, 1, 1, 1);
		addButton("J", 74, 6, 1, 1, 1);
		addButton("K", 75, 7, 1, 1, 1);
		addButton("L", 76, 8, 1, 1, 1);
		addButton("Ö", 16777430, 9, 1, 1, 1);
		addButton("Ä", 16777412, 10, 1, 1, 1);
		addButton("'", 520, 11, 1, 1, 1);
		
		addButton(">", 153, 0, 2, 1, 1);
		addButton("Y", 89, 1, 2, 1, 1);
		addButton("X", 88, 2, 2, 1, 1);
		addButton("C", 67, 3, 2, 1, 1);
		addButton("V", 86, 4, 2, 1, 1);
		addButton("B", 66, 5, 2, 1, 1);
		addButton("N", 78, 6, 2, 1, 1);
		addButton("M", 77, 7, 2, 1, 1);
		addButton(";", 44, 8, 2, 1, 1);
		addButton(":", 46, 9, 2, 1, 1);
		addButton("_", 45, 10, 2, 1, 1);
		addButton("^", 16, 11, 2, 2, 1);
	}
	
	/**
	 * Set the sound pack to associate with the key buttons
	 * @param soundPack The soundpack that conatins the key mappings to 
	 * use on the buttons
	 */
	public void setSoundPack(SoundPack soundPack){
		
		// Reset all the buttons first
		for(KeyButton button:buttons.values()){
			button.setKeyMapping(null);
			getActionMap().clear();
			getInputMap(WHEN_IN_FOCUSED_WINDOW).clear();
		}
		
		// Set all key mappings to corresponding key buttons
		for(KeyMapping mapping:soundPack.getKeyMappings()){
			if(buttons.containsKey(mapping.getKeyCode())){
				buttons.get(mapping.getKeyCode()).setKeyMapping(mapping);
				addKeyMapping(mapping);
			}
		}
	}
	
	/**
	 * Adds a KeyMapping to the action and input maps,
	 * so that they actually react to the keyboard
	 * @param mapping The KeyMapping to insert
	 */
	private void addKeyMapping(KeyMapping mapping){
		final KeyMapping _mapping = mapping;
		
		// Create Actions for pressing and realeasing a key
		final Action pressed = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ActivationModeBehavior.getInstance().trigger(_mapping.getActivationMode(), _mapping.getSoundSample());
				getActionMap().remove(_mapping.getKeyCode()+"p"); // dont retrigger, when held down
			}
		};
		final Action released = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ActivationModeBehavior.getInstance().untrigger(_mapping.getActivationMode(), _mapping.getSoundSample());
				getActionMap().put(_mapping.getKeyCode()+"p", pressed); // register again, when released
			}
		};
		
		// Put the action in the action map
		getActionMap().put(mapping.getKeyCode()+"p", pressed);
		getActionMap().put(mapping.getKeyCode()+"r", released);
		
		// register key strokes for the actions
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(mapping.getKeyCode(), 0, false), mapping.getKeyCode()+"p");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(mapping.getKeyCode(), 0, true), mapping.getKeyCode()+"r");
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
	private void addButton(String label, int keyCode, int x, int y, int width, int height){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		KeyButton button = new KeyButton(label, audioCore);
		buttons.put(keyCode, button);
		add(button, gbc);
	}
	
}
