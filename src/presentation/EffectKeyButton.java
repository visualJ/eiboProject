package presentation;


import repository.EffectMode;
import services.AudioCore;
import services.EffectModeBehavior;

public class EffectKeyButton extends KeyButton{

	private static final long serialVersionUID = 1L;
	private AudioCore audioCore;
	private final EffectMode MODE;
	private EffectModeBehavior effectModeBehavior;
	
	public EffectKeyButton(String keyLabel, EffectMode mode, int keyCode, AudioCore audioCore) {
		super(keyLabel, keyCode);
		this.audioCore = audioCore;
		this.MODE = mode;
		this.effectModeBehavior = EffectModeBehavior.getInstance();
		
		switch(mode){
		case HIGHPASS:
			setBigIcon(UserInterface.highpassIcon);
			break;
		case LOWPASS:
			setBigIcon(UserInterface.lowpassIcon);
			break;
		case DELAY:
			setBigIcon(UserInterface.delayIcon);
			break;
		default:
			break;
		}
		
		setOnTrigger(new Runnable() {
			
			@Override
			public void run() {
				effectModeBehavior.trigger(MODE);
			}
		});
		
		
		
	}
	

}
