package services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import repository.KeyMapping;
import repository.SoundPack;
import repository.SoundSample;
import ddf.minim.AudioInput;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.FilePlayer;
import ddf.minim.ugens.Summer;

public class AudioCore {

	private Minim minim;
	private AudioOutput audioOutput;
	private AudioInput audioInput;
	private Summer audioMixer;
	private AudioFx audioFx;
	private Map<SoundSample, FilePlayer> sounds;
	
	public void init(){
		// Prepare the minim player
		minim = new Minim(this);
		
		// Get the speaker output
		audioOutput = minim.getLineOut();
		
		// Get the microphone input
		audioInput = minim.getLineIn();
		
		// Create the audioFx module, which processes sounds
		// and delivers them to the audioOutput
		audioFx = new AudioFx(audioOutput);
		
		// Get the summer ready, which collects all the sounds
		// and puts them into the audioFx module
		audioMixer = new Summer();
		audioMixer.patch(audioFx);
		
		// Prepare the list of sounds, that can be played
		sounds = new HashMap<SoundSample, FilePlayer>();
	}
	
	/**
	 * Loads all Sounds in a pack.
	 * This has to be called before any sound sample from the pack can be played.
	 * @param soundPack The sound pack that contains the sound samples
	 */
	public void loadSoundPackSamples(SoundPack soundPack){
		for(KeyMapping keyMapping:soundPack.getKeyMappings()){
			
			// Get the sound sample
			SoundSample soundSample = keyMapping.getSoundSample();
			
			// Load the sound and create a file player
			FilePlayer player = loadSoundSamplePlayer(soundSample);
			
			// Attatch the player to the audio mixer, so that the sound
			// can be played
			player.pause();
			player.patch(audioMixer);
			
			// Add the player to the list of sounds
			sounds.put(soundSample, player);
		}
	}
	
	/**
	 * Unloads and removes all Sounds in a pack from the list of sounds.
	 * @param soundPack The sound pack to remove
	 */
	public void unloadSoundPackSamples(SoundPack soundPack){
		for(KeyMapping keyMapping:soundPack.getKeyMappings()){
			
			// Get the sound sample
			SoundSample soundSample = keyMapping.getSoundSample();
			
			// Get the corresponding file player
			FilePlayer player = sounds.get(soundSample);
			
			// close the player. This stops playback.
			player.close();
			player.unpatch(audioMixer);
			
			// remove the player from the list of sounds
			sounds.remove(soundSample);
		}
	}
	
	/**
	 * Plays a previously loaded sound sample.
	 * If it is already playing, this will stop the
	 * sound and play it from the beginning
	 * @param soundSample
	 */
	public void playSample(SoundSample soundSample){
		if(sounds.containsKey(soundSample)){
			sounds.get(soundSample).rewind();
			sounds.get(soundSample).play();
		}else{
			System.out.println("Error: playSample() tried playing sound sample "+soundSample.getFileName()+" when is was not loaded!");
		}
	}
	
	
	//////////////////////////////// Minim needs the following methods \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**
	 * Used by minim
	 * Return a path
	 * @param fileName
	 * @return
	 */
	public String sketchPath(String fileName) {
		return fileName;
	}

	/**
	 * Used by minim
	 * Creates a file input stream
	 * @param fileName
	 * @return
	 */
	public InputStream createInput(String fileName) {
		try {
			return new FileInputStream(fileName);
		} catch (Exception ex) {}
		return null;
	}
	
	/**
	 * Creates a {@link FilePlayer} from a {@link SoundSample}
	 * @param soundSample The sound smaple to create a file player from
	 * @return The file player
	 */
	private FilePlayer loadSoundSamplePlayer(SoundSample soundSample){
		return new FilePlayer(minim.loadFileStream(soundSample.getFileName(), 1024, false));
	}

	private class AudioFx extends Delay{
		public AudioFx(AudioOutput audioOutput){
			setDelTime(1/audioOutput.sampleRate());
			patch(audioOutput);
		}
	}
}
