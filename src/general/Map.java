package general;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import classBase.Graph;
import classBase.List;
import wg.threads.ConsoleOutputTool;

public class Map {

	private List<Event> events;
	private Graph graph;
	private ConsoleOutputTool out;

	public String mapImageName;
	public int mapImageWidth, mapImageHeigth;
	private Area[] areas;
	public int[][] graphMatrix;

	public Map(String name) {
		out = new ConsoleOutputTool("Map");
		readMapData(loadMap("maps/" + name + ".json"));
	}

	private void readMapData(JSONObject json) {
		if (json.isEmpty())
			return;
		JSONArray arr = json.getJSONArray("mapImage");
		mapImageName = arr.getString(0);
		mapImageWidth = arr.getInt(1);
		mapImageHeigth = arr.getInt(2);

		arr = json.getJSONArray("areas");
		JSONArray arr1;
		areas = new Area[arr.length()];
		for (int i = 0; i < areas.length; i++) {
			areas[i] = new Area(((JSONObject) arr.get(i)).getString("areaName"),
					((JSONObject) arr.get(i)).getInt("labelX"), ((JSONObject) arr.get(i)).getInt("labelY"),
					((JSONObject) arr.get(i)).getInt("additionalLabelWidth"));
			arr1 = ((JSONObject) arr.get(i)).getJSONArray("buildingSlots");
			for (int j = 0; j < arr1.length(); j++) {
				areas[i].addBuildingSlot(((JSONObject) arr1.get(j)).getInt("index"),
						((JSONObject) arr1.get(j)).getInt("x"), ((JSONObject) arr1.get(j)).getInt("y"));
			}
		}
	}
	
	public Area[] getAreas() {
		return areas;
	}

	private JSONObject loadMap(String name) {
		File file = new File(name);
		if (!verifyMap(file))
			return new JSONObject();

		String loadedContent = "";
		try {
			loadedContent = new String(Files.readAllBytes(Paths.get(file.toURI())));
		} catch (IOException e) {
			out.println("Something went terribly wrong.");
			e.printStackTrace();
		}

		return new JSONObject(loadedContent);
	}

	private boolean verifyMap(File file) {
		if (!file.getName().substring(file.getName().length() - 5).equalsIgnoreCase(".json"))
			return false;
		if (!file.exists()) {
			return false;
		}

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

	public Graph getGraph() {
		return graph;
	}

	public List<Event> getEventList() {
		return events;
	}

}
