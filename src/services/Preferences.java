package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Preferences {

	private static Preferences intstance;
	private String recordFolder = "." + File.separator;

	private Preferences() {

	}

	public static Preferences getInstance() {
		if (intstance == null) {
			intstance = new Preferences();
		}

		return intstance;
	}

	public void setRecordFolder(String path) {
		this.recordFolder = path;
		System.out.println("New record Path: " + path);
	}

	public String getRecordFolder() {
		return this.recordFolder;
	}
	
	public void loadPreferences(String path){
		File file = new File(path + File.separator +  "iBo.pref");
	
		if(file.exists()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				this.recordFolder = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		}

		
		
	}
	
	public void savePreferences(String path){
		File file = new File(path + File.separator +  "iBo.pref");
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		 
		try(PrintWriter pw = new PrintWriter(file)){
			pw.println(recordFolder);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
