package services;

import repository.EffectMode;

public class EffectModeBehavior {

		private static EffectModeBehavior instance = null;
		
		private static AudioCore audioCore;
		
		public static EffectModeBehavior getInstance(){
			if(instance == null){
				instance = new EffectModeBehavior();
			}
			return instance;
		}
		
		/**
		 */
		public static void init(AudioCore audioCore){
			EffectModeBehavior.audioCore = audioCore;
		}
	
		
		public void trigger(EffectMode mode){
			switch (mode) {
			case HIGHPASS:
				audioCore.getAudioFx().setHighPass(!audioCore.getAudioFx().isHighPass());
				System.out.println("highpass: funktioniert");
				break;
			case LOWPASS:
				audioCore.getAudioFx().setLowPass(!audioCore.getAudioFx().isLowPass());
				System.out.println("lowpass: funktioniert");
				break;
			case DELAY:
				System.out.println(audioCore.getAudioFx().isDelay());
				System.out.println(audioCore.getAudioFx().getDelTime());
				audioCore.getAudioFx().setDelTime(((audioCore.getAudioFx().isDelay())?0.0f:2.0f));
				System.out.println("--");
				System.out.println(audioCore.getAudioFx().isDelay());
				System.out.println(audioCore.getAudioFx().getDelTime());
				break;
			default:
				break;
			}
		}
		
}
