package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author Ulrike Kocher, Nadine Goebertshan, Benedikt Ringlein, Patrik Pezelj
 * 
 *         This istance creates a pref file to save the Record Path
 *
 */
public class Preferences {

	private static Preferences intstance;
	private String recordFolder = "." + File.separator;

	// private constructor
	private Preferences() {
	}

	//getter for Instance
	public static Preferences getInstance() {
		if (intstance == null) {
			intstance = new Preferences();
		}

		return intstance;
	}
	
	//set Recordfolder
	public void setRecordFolder(String path) {
		this.recordFolder = path;
		System.out.println("New record Path: " + path);
	}
	
	// get location of the RecordFolder
	public String getRecordFolder() {
		return this.recordFolder;
	}
	
	// load Preferences if file exist 
	public void loadPreferences(String path) {
		File file = new File(path + File.separator + "iBo.pref");
		if (file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))){
				this.recordFolder = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	// save Preferences to iBo.pref file
	public void savePreferences(String path) {
		File file = new File(path + File.separator + "iBo.pref");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try (PrintWriter pw = new PrintWriter(file)) {
			pw.println(recordFolder);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
