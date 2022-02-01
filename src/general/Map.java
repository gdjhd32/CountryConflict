package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import classBase.List;
import wg.threads.ConsoleOutputTool;

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
	private ConsoleOutputTool out;

	public Map() {
		out = new ConsoleOutputTool("Map");

	}

	public void loadMap(String name) {
		File file = new File(name);
		if (!verifyMap(file))
			return;

		String loadedContent = "";
		try {
			loadedContent = new String(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			out.println("Something went terribly wrong.");
			e.printStackTrace();
		}
		JSONObject json = new JSONObject(loadedContent);

	}

	private boolean verifyMap(File file) {
		if (!file.exists())
			return false;
		if (!file.getName().substring(file.getName().length() - 5).equalsIgnoreCase(".json"))
			return false;

		try {
			String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
			new JSONObject(content);
		} catch (IOException e) {
			out.println("Failed to create the JSON object and/or reading the file at: " + file.getAbsolutePath());
			return false;
		}

		// verifying with server?
		// When yes, then keep in mind, that this class is also used by the server.

		return true;
	}

}
