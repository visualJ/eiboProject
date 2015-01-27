package services;

import repository.EffectMode;
import repository.NotInitializedException;

public class EffectModeBehavior {

		private static EffectModeBehavior instance = null;
		
		private AudioCore audioCore;
		
		
		public static EffectModeBehavior getInstance(){
			if(instance == null){
				throw new NotInitializedException("EffectModeBehavior");
			}
			return instance;
		}
		
		/**
		 */
		public static void init(AudioCore audioCore){
			instance = new EffectModeBehavior();
		}
	
		
		public static void trigger(EffectMode mode){
			instance.audioCore.getAudioFx().setHighPass(false);
			instance.audioCore.getAudioFx().setLowPass(false);
			instance.audioCore.getAudioFx().setDelTime(0.0f);
			switch (mode) {
			case HIGHPASS:
				instance.audioCore.getAudioFx().setHighPass(true);
				System.out.println("highpass: funktioniert");
				break;
			case LOWPASS:
				instance.audioCore.getAudioFx().setLowPass(true);
				System.out.println("lowpass: funktioniert");
				break;
			case DELAY:
				instance.audioCore.getAudioFx().setDelTime(2.0f);
				System.out.println("delay: funktioniert");
				break;
			default:
				break;
			}
		}
		
}
