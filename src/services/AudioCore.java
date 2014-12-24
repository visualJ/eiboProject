package services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repository.AudioFx;
import repository.BeatListener;
import repository.KeyMapping;
import repository.SoundPack;
import repository.SoundSample;
import ddf.minim.AudioInput;
import ddf.minim.AudioOutput;
import ddf.minim.AudioRecorder;
import ddf.minim.Minim;
import ddf.minim.Recordable;
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
	private List<SoundSample> sheduledSounds = new ArrayList<SoundSample>();
	private int bpm = 60;
	private List<BeatListener> beatListeners = new ArrayList<BeatListener>();
	private Thread beatThread;
	private Runnable beatClock = new Runnable(){
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				
				// Tell all BeatListeners that a beat happened
				notifyBeatListenersBeat();
				
				// Play all sounds that are sheduled for the next beat
				playSheduledSounds();
				
				try {
					// Sleep, so that this thread is active once per beat
					Thread.sleep(Math.round(60000/bpm));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		}
	};
	
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
		
		// Start the beat tread
		beatThread = new Thread(beatClock);
		beatThread.start();
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
	 * Shedules a {@link SoundSample} to be played on the next beat.
	 * A SoundSample can only be sheduled once per beat.
	 * @param soundSample The {@link SoundSample} to be sheduled
	 */
	public void playSampleOnNextBeat(SoundSample soundSample){
		if(!sheduledSounds.contains(soundSample)){
			sheduledSounds.add(soundSample);
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
	 * Returns the {@link AudioFx} object for altering
	 * the sound effects
	 * @return the audio fx object
	 */
	public AudioFx getAudioFx(){
		return audioFx;
	}
	
	/**
	 * Adds a {@link BeatListener} to the AudioCore
	 * @param listener The listener to inform on every beat
	 */
	public void addBeatListener(BeatListener listener){
		beatListeners.add(listener);
	}
	
	/**
	 * Removes a {@link BeatListener} from the AudioCore
	 * @param listener The listener to remove
	 */
	public void removeBeatListener(BeatListener listener){
		beatListeners.remove(listener);
	}
	
	/**
	 * Resturns the volume of the current audio output
	 * @return The volume
	 */
	public float getOutputLevel(){
		return audioOutput.mix.level();
	}
	
	/**
	 * Inform all BeatListeners, that a beat happened
	 */
	private void notifyBeatListenersBeat(){
		for(BeatListener listener:beatListeners){
			listener.beat();
		}
	}
	
	/**
	 * Inform all BeatListeners, that the bpm changed
	 */
	private void notifyBeatListenersBpmChanged(){
		for(BeatListener listener:beatListeners){
			listener.bpmChanged(bpm);
		}
	}
	
	/**
	 * Creates a {@link FilePlayer} from a {@link SoundSample} and prepares
	 * it to be played by attaching it to the audioMixer
	 * @param soundSample The sound sample to create a file player from
	 * @return The file player
	 */
	private FilePlayer loadSoundSamplePlayer(SoundSample soundSample){
		System.out.println("load sound sample "+soundSample.getFileName());
		
		// Load the sound file and create a playable object
		FilePlayer player = new FilePlayer(minim.loadFileStream(soundSample.getFileName(), 1024, false));
		
		// Attach the player to the audio mixer, so that the sound
		// can be played
		player.pause();
		player.patch(audioMixer);
		
		return player;
	}
	
	/**
	 * Plays all sheduled sounds and clears the list
	 */
	private void playSheduledSounds(){
		
		// Play all sheduled sounds
		for(SoundSample sample:sheduledSounds){
			playSample(sample);
		}
		
		// Clear the seduled sounds list
		sheduledSounds.clear();
	}
	
	////////////////////////////////////////// Memberklassen \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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
		
		/**
		 * Returns whether the recorder is recording or not.
		 * @return True, if currently recording
		 */
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

	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
		notifyBeatListenersBpmChanged();
	}
}
