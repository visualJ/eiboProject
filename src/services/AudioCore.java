package services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import repository.AudioFx;
import repository.BeatListener;
import repository.SampleListener;
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
	private List<SoundSample> loopedSounds = new ArrayList<SoundSample>();
	private Timer stopSampleTimer = new Timer();
	private int bpm = 60;
	private int beatInBar = 0;
	private int barLength = 4;
	private List<BeatListener> beatListeners = new ArrayList<BeatListener>();
	private List<SampleListener> sampleListeners = new ArrayList<SampleListener>();
	private Map<SoundSample, TimerTask> stopSampleNotificationTimers = new HashMap<SoundSample, TimerTask>();
	private Thread beatThread;
	private Runnable beatClock = new Runnable(){
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				
				// Tell all BeatListeners that a beat happened
				notifyBeatListenersBeat();
				
				// Play all sounds that are sheduled for the next beat
				playSheduledSounds();
				
				// Play all looped sounds on first beat in the bar
				if(beatInBar == 0){
					playLoopedSounds();
				}
				
				// Move to the next beat in the bar
				beatInBar = (beatInBar+1)%barLength;
				
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
		playSample(soundSample, false);
	}

	/**
	 * Plays a previously loaded sound sample.
	 * If it is already playing, this will stop the
	 * sound and play it from the beginning
	 * @param soundSample
	 */
	public void playSample(SoundSample soundSample, boolean loop){
		if(sounds.containsKey(soundSample)){
			sounds.get(soundSample).rewind();
			if(loop){
				sounds.get(soundSample).loop();
			}else{
				sounds.get(soundSample).play();
				sheduleStopSampleNotificaiton(soundSample);
				
			}
			notifySampleListenerPlayedSample(soundSample);
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
			notifySampleListenerStoppedSample(soundSample);
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
			notifySampleListenerSheduledSample(soundSample);
		}
	}
	
	/**
	 * Starts playing a SoundSample in a loop.
	 * It begins on the next beat and every repetition starts on a beat.
	 */
	public void playLoop(SoundSample soundSample){
		if(!loopedSounds.contains(soundSample)){
			loopedSounds.add(soundSample);
			notifySampleListenerSheduledSample(soundSample);
		}
	}
	
	/**
	 * Stops a loop. The loops still plays until its end.
	 */
	public void stopLoop(SoundSample soundSample){
		if(loopedSounds.contains(soundSample)){
			loopedSounds.remove(soundSample);
			sheduleStopSampleNotificaiton(soundSample);
			notifySampleListenerStoppedLoop(soundSample);
		}
	}
	
	/**
	 * Tests, if a sample is currently being played as a loop (not just looped playback!)
	 * @param soundSample The sample to test
	 * @return True, if the sample is being played a a loop currently
	 */
	public boolean isLoopPlaying(SoundSample soundSample){
		return loopedSounds.contains(soundSample);
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
	 * Returns, whether the performance is being recorded currently
	 * @return True, if a recording is running
	 */
	public boolean isPerformanceRecording(){
		return outputRecorder.isRecording();
	}
	
	/**
	 * Starts recording the mic. A new sound file is created.
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
	 * Returns, whether the microphone is being recorded currently
	 * @return True, if a recording is running
	 */
	public boolean isMicRecording(){
		return inputRecorder.isRecording();
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
	 * Adds a {@link SampleListener} to the AudioCore
	 * @param listener The listener to add
	 */
	public void addSampleListener(SampleListener listener){
		sampleListeners.add(listener);
	}
	
	public void removeSampleListener(SampleListener listener){
		sampleListeners.remove(listener);
	}
	
	/**
	 * Resturns the volume of the current audio output
	 * @return The volume
	 */
	public float getOutputLevel(){
		return audioOutput.mix.level();
	}
	
	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
		notifyBeatListenersBpmChanged();
	}

	public int getBarLength() {
		return barLength;
	}

	public void setBarLength(int barLength) {
		this.barLength = barLength;
	}

	/**
	 * Inform all BeatListeners, that a beat happened
	 */
	private void notifyBeatListenersBeat(){
		for(BeatListener listener:beatListeners){
			listener.beat(beatInBar);
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
	 * Notify all SampleListeners, that a sample has been sheduled
	 * @param sample The sample, that has been sheduled
	 */
	private void notifySampleListenerSheduledSample(SoundSample sample){
		for(SampleListener listener:sampleListeners){
			if(listener.getSample().equals(sample)){
				listener.sheduledSample();
			}
		}
	}
	
	/**
	 * Notify all SampleListeners, that a sample has been started playing
	 * @param sample The sample, that is playing
	 */
	private void notifySampleListenerPlayedSample(SoundSample sample){
		for(SampleListener listener:sampleListeners){
			if(listener.getSample().equals(sample)){
				listener.playedSample();
			}
		}
	}
	
	/**
	 * Notify all SampleListeners, that a sample has been stopped
	 * @param sample The sample, that has been stopped
	 */
	private void notifySampleListenerStoppedSample(SoundSample sample){
		if(stopSampleNotificationTimers.containsKey(sample)){
			stopSampleNotificationTimers.get(sample).cancel();
			stopSampleNotificationTimers.remove(sample);
		}
		for(SampleListener listener:sampleListeners){
			if(listener.getSample().equals(sample)){
				listener.stoppedSample();
			}
		}
	}
	
	/**
	 * Notify all SampleListeners, that a loop has been stopped
	 * @param sample The sample, that is playing as a loop
	 */
	private void notifySampleListenerStoppedLoop(SoundSample sample){
		for(SampleListener listener:sampleListeners){
			if(listener.getSample().equals(sample)){
				listener.stoppedLoop();
			}
		}
	}
	
	/**
	 * Start the timer for a StoppedSmaple notification
	 * @param soundSample The sound sample that will be stopped later
	 */
	private void sheduleStopSampleNotificaiton(SoundSample soundSample){
		final SoundSample _soundSample = soundSample;
		FilePlayer player = sounds.get(soundSample);
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				notifySampleListenerStoppedSample(_soundSample);
				
			}
		};
		stopSampleNotificationTimers.put(soundSample, task);
		stopSampleTimer.schedule(task, player.length() - player.position());
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
			notifySampleListenerPlayedSample(sample);
		}
		
		// Clear the seduled sounds list
		sheduledSounds.clear();
	}
	
	/**
	 * Plays all the looped sounds
	 */
	private void playLoopedSounds(){
		
		// Play all looped sounds
		for(int i = 0; i < loopedSounds.size(); i++){
			FilePlayer player = sounds.get(loopedSounds.get(i));
			// Only play sample, if the sample is not playing or almost finished
			if(player.length()-player.position() < 50 || !player.isPlaying()){
				playSample(loopedSounds.get(i));
				notifySampleListenerPlayedSample(loopedSounds.get(i));
			}
		}
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
				recorder = null;
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
}
