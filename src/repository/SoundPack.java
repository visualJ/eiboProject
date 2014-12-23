package repository;

import java.util.ArrayList;
import java.util.List;

/**
 * A soundpack contains different {@link SoundSample}s and {@link KeyMapping}s.
 * It also contains information about its name and creator.
 * @author Benedikt Ringlein
 *
 */
public class SoundPack {

	private String packName = "";
	private String creatorName = "";
	private String imageFile = "";
	private int bpm = 60;
	
	private List <KeyMapping> keyMappings = new ArrayList<KeyMapping>();

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getPackName() {
		return packName;
	}
	
	public String getCreatorName() {
		return creatorName;
	}
	
	public List<KeyMapping> getKeyMappings() {
		return keyMappings;
	}
	
	/**
	 * Adds a KeyMapping to this soundpack. 
	 * @param keyMapping
	 */
	public void addKeyMapping(KeyMapping keyMapping){
		keyMappings.add(keyMapping);
	}
	
	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public SoundPack(){
		
	}

	public SoundPack(String packName, String creatorName) {
		this.packName = packName;
		this.creatorName = creatorName;
	}
}
