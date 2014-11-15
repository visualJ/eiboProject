package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import repository.ActivationMode;
import repository.KeyMapping;
import repository.SoundSample;
import repository.SoundPack;
import repository.SoundPackLoadingFailedException;

/**
 * This manages soundpacks. It can load {@link SoundPack}s from disc and
 * give them to other parts of the application for further use.
 * @author Benedikt Ringlein
 *
 */
public class SoundPackManager {

	private SoundpackFileParser parser = new SoundpackFileParser();
	
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
		parser.listener = new SPFParseListener() {
			@Override
			public void readName(String name) {
				soundpack.setPackName(name);
			}
			@Override
			public void readCreator(String creator) {
				soundpack.setCreatorName(creator);
			}
			@Override
			public void readKeyMapping(int keyCode, ActivationMode activationMode, String soundFile) {
				if(new File(_soundPackFolder+File.separator+soundFile).exists()){
					soundpack.addKeyMapping(new KeyMapping(keyCode, activationMode, new SoundSample(_soundPackFolder+File.separator+soundFile)));
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
	
	///////////////////////////////// Integrated File Parser \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**
	 * A listener that listens to a {@link SoundpackFileParser}, if given to it.
	 * Gets calls with processed information from the file.
	 * @author Benedikt Ringlein
	 *
	 */
	private interface SPFParseListener{
		public void readName(String name);
		public void readCreator(String creator);
		public void readKeyMapping(int keyCode, ActivationMode activationMode, String soundFile);
	}
	
	/**
	 * This can read a soundpack description file and give information about
	 * the soundpack to a listener while parsing
	 * @author Benedikt Ringlein
	 *
	 */
	private class SoundpackFileParser{
		public SPFParseListener listener;
		public void parse(String file) throws IOException{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while(null != (line = reader.readLine())){
				// Read the file line by line and determine what to do
				// by looking at the lines first two letters
				switch(line.substring(0, 2)){
				case "N ":
					// read a name
					listener.readName(line.substring(2).trim());
					break;
				case "C ":
					// read a creator
					listener.readCreator(line.substring(2).trim());
					break;
				case "K ":
					// read a keymapping
					int keyCode;
					ActivationMode activationMode;
					String soundFile;
					// split the commy sperated values
					String splitline[] = line.substring(2).split(",");
					try {
						keyCode = Integer.parseInt(splitline[0].trim());
						activationMode = ActivationMode.valueOf(splitline[1].trim());
						soundFile = splitline[2].trim();
						listener.readKeyMapping(keyCode, activationMode, soundFile);
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
