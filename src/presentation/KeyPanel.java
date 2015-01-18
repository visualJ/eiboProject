package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import repository.KeyMapping;
import repository.RecSampleModeListener;
import repository.RecordingSampleMode;
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
	private KeyButton samplerecordingButton;
	private KeyButton sampledeletingButton;
	private AudioCore audioCore;
	private RecordingSampleMode recSampleMode = RecordingSampleMode.PLAYING;
	private List<RecSampleModeListener> recSampleModeListeners = new ArrayList<RecSampleModeListener>();

	public KeyPanel(AudioCore audioCore){
		this.audioCore = audioCore;
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		// Add the recording sample keys
		for(int i=0; i<10;i++){
			addRecordingSampleKeyButton(String.valueOf((i+1)%10), KeyEvent.VK_0 + (i+1)%10, i, 0);
		}
		
		// Add buttons for recording/deleting samples
		samplerecordingButton = new RecSampleModeRecordButton("ß", KeyEvent.getExtendedKeyCodeForChar('ß'), this);
		sampledeletingButton = new RecSampleModeDeleteButton("RÜCK", KeyEvent.VK_BACK_SPACE, this);
		addButton(10, 0, 1, 1, samplerecordingButton);
		addButton(11, 0, 2, 1, sampledeletingButton);
		
		// Add the sample keys
		addSampleKeyButton("Q", KeyEvent.VK_Q, 0, 1, 1, 1);
		addSampleKeyButton("W", KeyEvent.VK_W, 1, 1, 1, 1);
		addSampleKeyButton("E", KeyEvent.VK_E, 2, 1, 1, 1);
		addSampleKeyButton("R", KeyEvent.VK_R, 3, 1, 1, 1);
		addSampleKeyButton("T", KeyEvent.VK_T, 4, 1, 1, 1);
		addSampleKeyButton("Z", KeyEvent.VK_Z, 5, 1, 1, 1);
		addSampleKeyButton("U", KeyEvent.VK_U, 6, 1, 1, 1);
		addSampleKeyButton("I", KeyEvent.VK_I, 7, 1, 1, 1);
		addSampleKeyButton("O", KeyEvent.VK_O, 8, 1, 1, 1);
		addSampleKeyButton("P", KeyEvent.VK_P, 9, 1, 1, 1);
		addSampleKeyButton("Ü", KeyEvent.getExtendedKeyCodeForChar('Ü'), 10, 1, 1, 1);
		addSampleKeyButton("+", KeyEvent.getExtendedKeyCodeForChar('+'), 11, 1, 1, 1);
		addSampleKeyButton("┘", KeyEvent.VK_ENTER, 12, 1, 1, 2);
		
		addSampleKeyButton("A", KeyEvent.VK_A, 0, 2, 1, 1);
		addSampleKeyButton("S", KeyEvent.VK_S, 1, 2, 1, 1);
		addSampleKeyButton("D", KeyEvent.VK_D, 2, 2, 1, 1);
		addSampleKeyButton("F", KeyEvent.VK_F, 3, 2, 1, 1);
		addSampleKeyButton("G", KeyEvent.VK_G, 4, 2, 1, 1);
		addSampleKeyButton("H", KeyEvent.VK_H, 5, 2, 1, 1);
		addSampleKeyButton("J", KeyEvent.VK_J, 6, 2, 1, 1);
		addSampleKeyButton("K", KeyEvent.VK_K, 7, 2, 1, 1);
		addSampleKeyButton("L", KeyEvent.VK_L, 8, 2, 1, 1);
		addSampleKeyButton("Ö", KeyEvent.getExtendedKeyCodeForChar('Ö'), 9, 2, 1, 1);
		addSampleKeyButton("Ä", KeyEvent.getExtendedKeyCodeForChar('Ä'), 10, 2, 1, 1);
		addSampleKeyButton("#", KeyEvent.getExtendedKeyCodeForChar('#'), 11, 2, 1, 1);
		
		addSampleKeyButton("<", KeyEvent.VK_LESS, 0, 3, 1, 1);
		addSampleKeyButton("Y", KeyEvent.VK_Y, 1, 3, 1, 1);
		addSampleKeyButton("X", KeyEvent.VK_X, 2, 3, 1, 1);
		addSampleKeyButton("C", KeyEvent.VK_C, 3, 3, 1, 1);
		addSampleKeyButton("V", KeyEvent.VK_V, 4, 3, 1, 1);
		addSampleKeyButton("B", KeyEvent.VK_B, 5, 3, 1, 1);
		addSampleKeyButton("N", KeyEvent.VK_N, 6, 3, 1, 1);
		addSampleKeyButton("M", KeyEvent.VK_M, 7, 3, 1, 1);
		addSampleKeyButton(",", KeyEvent.VK_COMMA, 8, 3, 1, 1);
		addSampleKeyButton(".", KeyEvent.VK_PERIOD, 9, 3, 1, 1);
		addSampleKeyButton("-", KeyEvent.VK_MINUS, 10, 3, 1, 1);
		addSampleKeyButton("UMSCHALT", KeyEvent.VK_SHIFT, 11, 3, 2, 1);
		
		// Add special keys
		recordingButton = new RecordingButton("LEERTASTE", KeyEvent.VK_SPACE, this.audioCore);
		addButton(3, 4, 5, 1, recordingButton);
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
	
	public RecordingSampleMode getRecSampleMode() {
		return recSampleMode;
	}

	public void setRecSampleMode(RecordingSampleMode recSampleMode) {
		this.recSampleMode = recSampleMode;
		notifyRecSampleListeners(recSampleMode);
	}
	
	public void addRecSampleModeListener(RecSampleModeListener listener){
		recSampleModeListeners.add(listener);
	}
	
	public void removeRecModeListener(RecSampleModeListener listener){
		recSampleModeListeners.remove(listener);
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
	 * Adds a recording sample key button
	 * @param label The buttons label
	 * @param keyCode The keycode of the corresponding button
	 * @param x The grid x position
	 * @param y The grid y positon
	 */
	private void addRecordingSampleKeyButton(String label, int keyCode, int x, int y){
		RecordingSampleKeyButton button = new RecordingSampleKeyButton(label, keyCode, audioCore, this);
		addButton(x, y, 1, 1, button);
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
	
	/**
	 * Notify listeners that that recording sample mode changed
	 * @param mode
	 */
	private void notifyRecSampleListeners(RecordingSampleMode mode){
		for(RecSampleModeListener listener:recSampleModeListeners){
			listener.recordingSampleModeChanged(mode);
		}
	}
	
}
