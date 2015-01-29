package presentation;

import java.awt.Color;

import repository.RecSampleModeListener;
import repository.RecordingSampleMode;

/**
 * A button, that changes the recording sample mode to DELETE
 * @author Benedikt Ringlein
 *
 */
public class RecSampleModeDeleteButton extends KeyButton {

	private static final long serialVersionUID = 1L;
	private KeyPanel keyPanel;
	private Color colorStandart = new Color(120,60,10);
	private Color colorActive = new Color(240,120,20);

	public RecSampleModeDeleteButton(String keyLabel, int keyCode, KeyPanel keyPanel) {
		super(keyLabel, keyCode);
		this.keyPanel = keyPanel;
		
		// Set background and icon
		setBackground(colorStandart);
		setBigIcon(UserInterface.recordingSampleDeleteIcon);
		
		// Tooltiptext setzen
		setToolTipText("Samplelöschmodus: Zifferntasten drücken, um Samples zu löschen");
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				if(RecSampleModeDeleteButton.this.keyPanel.getRecSampleMode() != RecordingSampleMode.DELETING){
					RecSampleModeDeleteButton.this.keyPanel.setRecSampleMode(RecordingSampleMode.DELETING);
				}else{
					RecSampleModeDeleteButton.this.keyPanel.setRecSampleMode(RecordingSampleMode.PLAYING);
				}
			}
		});
		
		RecSampleModeDeleteButton.this.keyPanel.addRecSampleModeListener(new RecSampleModeListener() {
			
			@Override
			public void recordingSampleModeChanged(RecordingSampleMode mode) {
				if(mode == RecordingSampleMode.DELETING){
					setBackground(colorActive);
				}else{
					setBackground(colorStandart);
				}
			}
		});
		
	}

}
