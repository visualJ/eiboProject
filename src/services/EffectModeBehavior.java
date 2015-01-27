package services;

import repository.EffectMode;
import repository.NotInitializedException;

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
				System.out.println(audioCore.getAudioFx().getDelTime());
				if(audioCore.getAudioFx().getDelTime() <= 0f){
					audioCore.getAudioFx().setDelTime(2f);
					System.out.println("sollte 2 sein");
				}else{
					audioCore.getAudioFx().setDelTime(0f);
					System.out.println("sollte 0 sein");
				}
				//audioCore.getAudioFx().setDelTime(((audioCore.getAudioFx().getDelTime()<=0f)?2.0f:0f));
				System.out.println("delay: funktioniert");
				break;
			default:
				break;
			}
		}
		
}
