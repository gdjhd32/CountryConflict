package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import classBase.List;

public class Map {
	
	
	/**
	 * Temporary method, used for testing this class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Map();
	}
	
	private List<Event> events;
	private File log;
	private FileWriter logWriter;
	
	public Map() {
		log = new File("log.txt");
		if(!log.exists()) {
			try {
				log.createNewFile();
				logWriter = new FileWriter(log); 
				logWriter.write("Created new log file, because no log file was found.\n");
				logWriter.flush();
			} catch (IOException e) {
				System.out.println("The log file was not found and the following other error occurred: ");
				e.printStackTrace();
			}
		} else {
			try {
				logWriter = new FileWriter(log);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void loadMap(String name) {
		File jSon = new File(name);
		if(verifyMap(jSon)) {
			
		}
	}
	
	private boolean verifyMap(File file) {
		if(!file.exists())
			return false;
		if (!file.getName().substring(file.getName().length() - 5).equalsIgnoreCase(".json")) 
			return false;
		
		try {
			String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
			new JSONObject(content);
		} catch (IOException e) {
			try {
				logWriter.write("Failed to create the JSON object and/or reading the file at: " + file.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		
		
		//verifying with server?
		//When yes, then keep in mind, that this class is also used by the server.
		
		return true;
	}
	
	public File getLog() {
		return log;
	}
}
