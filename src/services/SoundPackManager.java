package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import repository.ActivationMode;
import repository.KeyMapping;
import repository.SoundPack;
import repository.SoundPackLoadingFailedException;
import repository.SoundSample;

/**
 * This manages soundpacks. It can load {@link SoundPack}s from disc and
 * give them to other parts of the application for further use.
 * @author Benedikt Ringlein
 *
 */
public class SoundPackManager {

	private static final String SOUNDPACK_FILE_EXTENTION = ".pck";
	private SoundpackFileParser parser = new SoundpackFileParser();
	
	public void init(){
		
	}
	
	/**
	 * Loads a {@link SoundPack}. On disk, a sound pack is a folder that contains audio files
	 * and a description file called {@code soundpack.ini}.
	 * If a sound file is missing, the corresponding key mapping is omitted.
	 * @param soundPackFolder The path to the sound pack folder
	 * @return The loaded sound pack
	 * @throws SoundPackLoadingFailedException if loading failed. (File not found or IO)
	 */
	public SoundPack loadSoundPack(String soundPackFolder) throws SoundPackLoadingFailedException{
		final SoundPack soundpack = new SoundPack();
		final String _soundPackFolder = soundPackFolder;
		parser.handler = new SPFParseHandler() {
			@Override
			public void readName(String name) {
				soundpack.setPackName(name);
			}
			@Override
			public void readCreator(String creator) {
				soundpack.setCreatorName(creator);
			}
			@Override
			public void readImage(String imageFile) {
				soundpack.setImageFile(_soundPackFolder+File.separator+imageFile);
			}
			@Override
			public void readBpm(int bpm) {
				soundpack.setBpm(bpm);
			}
			@Override
			public void readBarLength(int barLength) {
				soundpack.setBarLength(barLength);
			}
			@Override
			public void readKeyMapping(int keyCode, ActivationMode activationMode, String soundFile, String imageFile) {
				if(new File(_soundPackFolder+File.separator+soundFile).exists()){
					// Add keymapping, if the actual sound file exists
					soundpack.addKeyMapping(new KeyMapping(keyCode, activationMode, new SoundSample(_soundPackFolder+File.separator+soundFile), _soundPackFolder+File.separator+imageFile));
				}
			}
		};
		try {
			parser.parse(soundPackFolder+File.separator+"soundpack.ini");
		} catch (IOException e) {
			throw new SoundPackLoadingFailedException(soundPackFolder);
		}
		return soundpack;
	}
	
	/**
	 * Loads and returns all {@link SoundPack}s in the given directory
	 * @param dir The directory to load the soundpacks from
	 * @return An array of {@link SoundPack}s
	 */
	public SoundPack[] getSoundpacksInDirectory(String dir){
		File directory = new File(dir);
		SoundPack[] soundpacks;
		if(directory.isDirectory()){
			
			// Get all the soundpack file names
			String[] spNames = directory.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(SOUNDPACK_FILE_EXTENTION);
				}
			});
			soundpacks = new SoundPack[spNames.length];
			
			// Load every soundpack (this only loads information, 
			// not the actual heavy weight data, like sound or images)
			for(int i = 0; i < spNames.length; i++){
				try {
					soundpacks[i] = loadSoundPack(spNames[i]);
				} catch (SoundPackLoadingFailedException e) {
					e.printStackTrace();
				}
			}
			return soundpacks;
		}
		return new SoundPack[0];
	}
	
	///////////////////////////////// Integrated File Parser \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**
	 * A listener that listens to a {@link SoundpackFileParser}, if given to it.
	 * Gets calls with processed information from the file.
	 * @author Benedikt Ringlein
	 *
	 */
	private interface SPFParseHandler{
		public void readName(String name);
		public void readCreator(String creator);
		public void readImage(String imageFile);
		public void readBpm(int bpm);
		public void readBarLength(int barLength);
		public void readKeyMapping(int keyCode, ActivationMode activationMode, String soundFile, String imageFile);
	}
	
	/**
	 * This can read a soundpack description file and give information about
	 * the soundpack to a listener while parsing
	 * @author Benedikt Ringlein
	 *
	 */
	private class SoundpackFileParser{
		public SPFParseHandler handler;
		public void parse(String file) throws IOException{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while(null != (line = reader.readLine())){
				// Read the file line by line and determine what to do
				// by looking at the lines first two letters
				switch(line.substring(0, 2)){
				case "N ":
					// read a name
					handler.readName(line.substring(2).trim());
					break;
				case "C ":
					// read a creator
					handler.readCreator(line.substring(2).trim());
					break;
				case "I ":
					// read an image
					handler.readImage(line.substring(2).trim());
					break;
				case "B ":
					// read the bpm
					try {
						handler.readBpm(Integer.parseInt(line.substring(2).trim()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					break;
				case "L ":
					// read the bar length
					try {
						handler.readBarLength(Integer.parseInt(line.substring(2).trim()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					break;
				case "K ":
					// read a keymapping
					int keyCode;
					ActivationMode activationMode;
					String soundFile, imageFile;
					// split the comma separated values
					String splitline[] = line.substring(2).split(",");
					try {
						keyCode = Integer.parseInt(splitline[0].trim());
						activationMode = ActivationMode.valueOf(splitline[1].trim());
						soundFile = splitline[2].trim();
						imageFile = splitline.length>=4?splitline[3].trim():null;
						handler.readKeyMapping(keyCode, activationMode, soundFile, imageFile);
					} catch (Exception e) {
						e.printStackTrace();
						// Don't add the mapping, if an error occurred during parsing
					}
					break;
				default:
						break;
				}
			}
			reader.close();
		}
	}
}
