package presentation;

import services.AudioCore;

public class EffectKeyButton extends KeyButton{

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	
	public EffectKeyButton(String keyLabel, int keyCode, AudioCore audioCore) {
		super(keyLabel, keyCode);
		this.audioCore = audioCore;
		
		
	}
	

}
