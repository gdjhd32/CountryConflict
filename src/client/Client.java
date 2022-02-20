package client;

import general.Map;

public class Client {
	
	private Map map;
	private Render render;
	
	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		map = new Map("Map1");
		render = new Render();
		render.renderMap(map);
	}
	
}
