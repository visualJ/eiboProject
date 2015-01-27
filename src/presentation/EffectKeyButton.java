package presentation;


import repository.EffectMode;
import services.AudioCore;
import services.EffectModeBehavior;

public class EffectKeyButton extends KeyButton{

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private final EffectMode MODE;
	
	public EffectKeyButton(String keyLabel, EffectMode mode, int keyCode, AudioCore audioCore) {
		super(keyLabel, keyCode);
		this.audioCore = audioCore;
		this.MODE = mode;
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				EffectModeBehavior.trigger(MODE);
			}
		});
		
		
		
	}
	

}
