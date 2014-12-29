package repository;

import services.AudioCore;

/**
 * A SoundSample, represented by a file name. This does not include
 * actual playable data. The {@link AudioCore} can load sound samples and play them back.
 * @author Benedikt Ringlein
 *
 */
public class SoundSample {

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	/**
	 * Creates a sound sample.
	 * @param fileName
	 * @param player
	 */
	public SoundSample(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String toString() {
		return "SoundSample: " + fileName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SoundSample)) return false;
		SoundSample sample = (SoundSample) obj;
		return sample.getFileName().equals(getFileName());
	}
	
	@Override
	public int hashCode() {
		return fileName.hashCode();
	}
	
}
