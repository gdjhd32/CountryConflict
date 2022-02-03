package classBase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class GraphVisualizer extends Frame {

	Graph graph;
	BufferedImage img;
	Graphics g;

	/**
	 * A class to visualize the given graph in a frame.
	 * 
	 * @param frameTitle       The title of the frame.
	 * @param width            The width of the frame.
	 * @param height           The height of the frame.
	 * @param graph            The graph that should be displayed.
	 * @param order            A 2d-array of strings, where the indices, which
	 *                         contain the vertex's name, (times dx for the
	 *                         x-coordinate and times dy for the y-coordinate)
	 *                         resemble the position of the vertex
	 * @param dx               The distance between the vertices in x direction
	 * @param dy               The distance between the vertices in y direction
	 * @param drawVertexCircle True -> Vertex = Name + Circle; False -> Vertex =
	 *                         Name
	 * 
	 * @author Croyd
	 */
	public GraphVisualizer(String frameTitle, int width, int height, Graph graph, String[][] order, int dx, int dy,
			boolean drawVertexCircle) {
		super(frameTitle);
		this.graph = graph;

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		this.addWindowListener(null);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				zeichne(order, dx, dy, drawVertexCircle);
			}
		});

		setSize(width, height);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);

		setResizable(true);
		setVisible(true);
	}

	/**
	 * A class to visualize the given graph in a frame. The parameter 'positions'
	 * contains the x- and y-coordinates of the vertex with the index i in the list
	 * that is returned by graph.getVertices() -> positions[i][0] to get x and
	 * positions[i][1] to get y of the vertex with the index i
	 * 
	 * 
	 * @param frameTitle       The title of the frame.
	 * @param width            The width of the frame.
	 * @param height           The height of the frame.
	 * @param graph            The graph that should be displayed.
	 * @param positions        An 2d-array that contains the x- and y-coordinate of
	 *                         the vertices. Dimension: [...][2] !
	 * @param drawVertexCircle True -> Vertex = Name + Circle; False -> Vertex =
	 *                         Name
	 * 
	 * @author Croyd
	 */
	public GraphVisualizer(String frameTitle, int width, int height, Graph graph, int[][] positions,
			boolean drawVertexCircle) {
		super(frameTitle);
		this.graph = graph;

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				zeichne(positions, drawVertexCircle);
			}
		});

		setSize(width, height);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);

		setResizable(true);
		setVisible(true);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, 25, this);
	}

	/**
	 * Method to draw the graph with a 2d-string-array.
	 */
	private void zeichne(String[][] order, int dx, int dy, boolean drawVertexCircle) {
		if (graph == null)
			return;

		Dimension d = getSize();
		img = new BufferedImage(d.width, d.height - 25, BufferedImage.TYPE_INT_RGB);
		g = img.getGraphics();
		g.setColor(new Color(200, 200, 200));
		loesche(g, d.width, d.height);

		List<Vertex> vertices = graph.getVertices();

		int verticesCount = countVertices(graph);

		int[][] vertexPosition = new int[verticesCount][2]; // array to store the vertices' coordinates
		int vertexPositionIndex = 0;

		String[] vertexIDs = new String[verticesCount]; // array to store the vertices' id order
		int vertexIDsIndex = 0;

		// 'simulating' the calculating of the vertices' position to get their
		// coordinates so the lines can be drawn properly
		vertices.toFirst();
		while (vertices.hasAccess()) {

			vertexIDs[vertexIDsIndex] = vertices.getContent().getID();

			for (int i = 0; i < order.length; i++) {
				for (int j = 0; j < order[0].length; j++) {
					if (vertices.getContent().getID().equals(order[i][j])) { // saving the calculated position in
																				// vertexPosition
						vertexPosition[vertexPositionIndex][0] = j * dx + 100;
						vertexPosition[vertexPositionIndex][1] = i * dy + 100;
					}
				}
			}

			vertexIDsIndex++;
			vertexPositionIndex++;

			vertices.next();
		}

		// drawing the connections
		vertices.toFirst();
		while (vertices.hasAccess()) {
			List<Vertex> neighbors = graph.getNeighbours(vertices.getContent());
			neighbors.toFirst();
			while (neighbors.hasAccess()) {
				int id1 = -1, id2 = -1;
				for (int i = 0; i < vertexIDs.length; i++) {
					if (vertices.getContent().getID().equals(vertexIDs[i])) { // first vertex's id
						id1 = i;
					}
					if (neighbors.getContent().getID().equals(vertexIDs[i])) { // second vertex's id
						id2 = i;
					}
				}

				// drawing a line between the coordinates of the first and second vertex
				g.setColor(new Color(0, 0, 0));
				g.drawLine(vertexPosition[id1][0] + 10, vertexPosition[id1][1] + 10, vertexPosition[id2][0] + 10,
						vertexPosition[id2][1] + 10);

				neighbors.next();
			}
			vertices.next();
		}

		// drawing the vertices with their id with help of vertexPosition and vertexIDs
		for (int i = 0; i < vertexPosition.length; i++) {
			drawVertex(vertexPosition[i][0], vertexPosition[i][1], vertexIDs[i], drawVertexCircle);
		}

		drawImage("src/Testing/tmp.png", 0, 0, 300, 100);

		this.repaint();
	}

	/**
	 * Method to draw the graph with a 2d-int-array.
	 */
	private void zeichne(int[][] positions, boolean drawVertexCircle) {
		if (graph == null)
			return;

		Dimension d = getSize();
		img = new BufferedImage(d.width, d.height - 25, BufferedImage.TYPE_INT_RGB);
		g = img.getGraphics();
		g.setColor(new Color(200, 200, 200));
		loesche(g, d.width, d.height);

		List<Vertex> vertices = graph.getVertices();

		int verticesCount = countVertices(graph);

		String[] vertexIDs = new String[verticesCount]; // array to store the vertices' id order
		int vertexIDsIndex = 0;

		// groundwork to draw the vertices and lines
		vertices.toFirst();
		while (vertices.hasAccess()) {

			vertexIDs[vertexIDsIndex] = vertices.getContent().getID();
			vertexIDsIndex++;

			vertices.next();
		}

		// drawing the connections
		vertices.toFirst();
		while (vertices.hasAccess()) {
			List<Vertex> neighbors = graph.getNeighbours(vertices.getContent());
			neighbors.toFirst();
			while (neighbors.hasAccess()) {
				int id1 = -1, id2 = -1;
				for (int i = 0; i < vertexIDs.length; i++) {
					if (vertices.getContent().getID().equals(vertexIDs[i])) {
						id1 = i;
					}
					if (neighbors.getContent().getID().equals(vertexIDs[i])) {
						id2 = i;
					}
				}

				g.setColor(new Color(0, 0, 0));
				g.drawLine(positions[id1][0] + 10, positions[id1][1] + 10, positions[id2][0] + 10,
						positions[id2][1] + 10);

				neighbors.next();
			}
			vertices.next();
		}

		// drawing the vertices with their id
		vertices.toFirst();
		for (int i = 0; i < vertexIDs.length; i++) {
			drawVertex(positions[i][0], positions[i][1], vertexIDs[i], drawVertexCircle);
		}

		this.repaint();
	}

	/**
	 * Clears the window.
	 */
	public void loesche(Graphics g, int x, int y) {
		g.setColor(new Color(220, 220, 230));
		g.fillRect(0, 0, x, y);
		g.setColor(new Color(0, 0, 250));
		g.setFont(new Font("Arial", Font.BOLD, 12));
	}

	/**
	 * Draws the vertex.
	 */
	private void drawVertex(int x, int y, String id, boolean drawCircle) {
		if (drawCircle) {
			g.setColor(new Color(0, 0, 0));
			g.setColor(new Color(255, 255, 0));
			g.fillArc(x, y, 20, 20, 0, 360);
			g.setColor(new Color(0, 0, 0));
			g.drawArc(x, y, 20, 20, 0, 360);
		}
		g.setColor(new Color(0, 100, 255));
		g.drawString(id, x + 5, y + 15);
	}

	/**
	 * Counts the Vertices of the graph, which is given through the parameter.
	 */
	private int countVertices(Graph graph) {
		int counter = 0;
		List<Vertex> list = graph.getVertices();
		list.toFirst();
		while (list.hasAccess()) {
			counter++;
			list.next();
		}
		return counter;
	}

	public void warte(int dur) {
		try {
			Thread.sleep(dur);
		} catch (Exception e) {

		} finally {

		}
	}

	private void drawImage(String path, int dx, int dy, int width, int height) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("Image could not be found.");
		}

		g.drawImage(img, dx, dy, width, height, null);
	}

}
