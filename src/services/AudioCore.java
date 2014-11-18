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
import ddf.minim.AudioRecorder;
import ddf.minim.Minim;
import ddf.minim.Recordable;
import ddf.minim.ugens.Delay;
import ddf.minim.ugens.FilePlayer;
import ddf.minim.ugens.Summer;

public class AudioCore {

	private Minim minim;
	private AudioOutput audioOutput;
	private AudioInput audioInput;
	private Summer audioMixer;
	private AudioFx audioFx;
	private AudioSampleRecorder inputRecorder;
	private AudioSampleRecorder outputRecorder;
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
		
		// Prepare the sample recorders
		outputRecorder = new AudioSampleRecorder(audioOutput);
		inputRecorder = new AudioSampleRecorder(audioInput);
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
			
			// Load the sound and add it to the sound map
			loadSoundSample(soundSample);
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
			
			// Unload and remove the sound sample
			unloadSoundSample(soundSample);
		}
	}
	
	/**
	 * Loads a single SoundSample and makes it available for playback
	 * @param soundSample
	 */
	public void loadSoundSample(SoundSample soundSample){
		if(!sounds.containsKey(soundSample)){
			
			// Load a sound file and put it in the sounds map
			sounds.put(soundSample, loadSoundSamplePlayer(soundSample));
		}
	}
	
	/**
	 * Unloads a {@link SoundSample} and removes it from the map
	 * of available sounds
	 * @param soundSample
	 */
	public void unloadSoundSample(SoundSample soundSample){
		if(sounds.containsKey(soundSample)){
			
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
	
	/**
	 * Stop a playing sample.
	 * If it is already playing, this will stop the
	 * sound and play it from the beginning
	 * @param soundSample
	 */
	public void stopSample(SoundSample soundSample){
		if(sounds.containsKey(soundSample)){
			sounds.get(soundSample).pause();
			sounds.get(soundSample).rewind();
		}else{
			System.out.println("Error: playSample() tried stopping sound sample "+soundSample.getFileName()+" when is was not loaded!");
		}
	}
	
	/**
	 * Starts recording the sound output. A new soundfile is created.
	 * @param fileName The name of the sound file to record to.
	 */
	public void startRecordingPerformance(String fileName){
		outputRecorder.beginRecord(fileName);
	}
	
	/**
	 * Stops a live performance recording and saves the file
	 */
	public SoundSample endRecordingPerformance(){
		return outputRecorder.endRecord();
	}
	
	/**
	 * Starts recording the mic. A new soundfile is created.
	 * @param fileName The name of the sound file to record to.
	 */
	public void startRecordingMic(String fileName){
		inputRecorder.beginRecord(fileName);
	}
	
	/**
	 * Stops a mic recording and saves the file
	 */
	public SoundSample endRecordingMic(){
		return inputRecorder.endRecord();
	}
	
	/**
	 * Creates a {@link FilePlayer} from a {@link SoundSample} and prepares
	 * it to be played by attaching it to the audioMixer
	 * @param soundSample The sound sample to create a file player from
	 * @return The file player
	 */
	private FilePlayer loadSoundSamplePlayer(SoundSample soundSample){
		
		// Load the sound file and create a playable object
		FilePlayer player = new FilePlayer(minim.loadFileStream(soundSample.getFileName(), 1024, false));
		
		// Attach the player to the audio mixer, so that the sound
		// can be played
		player.pause();
		player.patch(audioMixer);
		
		return player;
	}
	
	////////////////////////////////////////// Memberklassen \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	/**
	 * Processes audio with effects and is patched
	 * to the standart output
	 * @author Benedikt Ringlein
	 *
	 */
	private class AudioFx extends Delay{
		public AudioFx(AudioOutput audioOutput){
			setDelTime(1/audioOutput.sampleRate());
			patch(audioOutput);
		}
	}
	
	/**
	 * Records audio samples. This uses minims {@link AudioRecorder}, but is
	 * easier to use and creates {@link SoundSample}s
	 * @author Benedikt Ringlein
	 * 
	 */
	private class AudioSampleRecorder{
		private AudioRecorder recorder;
		private String fileName;
		private Recordable recsource;
		
		public AudioSampleRecorder(Recordable recsource){
			this.recsource = recsource;
		}
		
		/**
		 * Starts recording audio to a file
		 * @param fileName The name of the file to record to
		 */
		public void beginRecord(String fileName){
			if(recorder == null){
				recorder = minim.createRecorder(recsource, fileName);
				recorder.beginRecord();
				this.fileName = fileName;
			}else{
				System.out.println("Tried to start recording, when a recording was already running.");
			}
		}
		
		/**
		 * Stops recording audio.
		 * @return  The recorded SoundSample
		 */
		public SoundSample endRecord(){
			if(recorder != null && recorder.isRecording()){
				recorder.endRecord();
				recorder.save();
				return new SoundSample(fileName);
			}else{
				System.out.println("Error: endRecord() tried to end recording, when it was not started.");
				return null;
			}
		}
		
		public boolean isRecording(){
			if(recorder == null) return false;
			else return recorder.isRecording();
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
}
