package presentation;

import java.awt.Color;

import repository.RecSampleModeListener;
import repository.RecordingSampleMode;

/**
 * A button, that changes the recording sample mode to RECORD
 * @author Benedikt Ringlein
 *
 */
public class RecSampleModeRecordButton extends KeyButton {

	private static final long serialVersionUID = 1L;
	private KeyPanel keyPanel;
	private Color colorStandart = new Color(85,120,10);
	private Color colorActive = new Color(170,240,20);

	public RecSampleModeRecordButton(String keyLabel, int keyCode, KeyPanel keyPanel) {
		super(keyLabel, keyCode);
		this.keyPanel = keyPanel;
		
		// Set background and icon
		setBackground(colorStandart);
		setBigIcon(UserInterface.recordingSampleRecordIcon);
		
		// Tooltip ftw
		setToolTipText("Sampleaufnahmemodus: Zifferntasten gedrückt halten zum aufnehmen eigener Samples.");
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				if(RecSampleModeRecordButton.this.keyPanel.getRecSampleMode() != RecordingSampleMode.RECORDING){
					RecSampleModeRecordButton.this.keyPanel.setRecSampleMode(RecordingSampleMode.RECORDING);
				}else{
					RecSampleModeRecordButton.this.keyPanel.setRecSampleMode(RecordingSampleMode.PLAYING);
				}
			}
		});
		
		RecSampleModeRecordButton.this.keyPanel.addRecSampleModeListener(new RecSampleModeListener() {
			
			@Override
			public void recordingSampleModeChanged(RecordingSampleMode mode) {
				if(mode == RecordingSampleMode.RECORDING){
					setBackground(colorActive);
				}else{
					setBackground(colorStandart);
				}
			}
		});
	}

}
