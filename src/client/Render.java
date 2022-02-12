package client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import classBase.List;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private Graphics2D g;
	private final int FRAME_WIDTH = 1000, FRAME_HEIGHT = (int) (FRAME_WIDTH * 0.75), FRAME_X = 0, FRAME_Y = 0;
	int q = 0;

	private JPanel mapPanel, infoPanel, shortcutPanel;

	public static void main(String[] args) {
		new Render();
	}

	public Render() {
		super("Country Conflict");
		setLocation(FRAME_X, FRAME_Y);
		addingComponents();

	}

	private void addingComponents() {

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// ending program on exit
				initializeEnd();
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				System.out.println("!");
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

		});

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/tmp.png"));
		} catch (Exception e) {
			System.err.println("!");
		}
		ImagePane pane = new ImagePane();
		pane.addImage("tmp", 0, 0, 0, 710, 500, 0);
		pane.addImage("tmp2", 100, 10, 1, 400, 350, 0);
		pane.addImage("tmp3", 150, 70, 2, 400, 350, 0);

		mapPanel = new JPanel();
		mapPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(e.getX() + ", " + e.getY() + "; " + q);
				pane.deleteImage("tmp3", 150 + (10 * q), 70, 2, 400, 350, 0);
				q++;
				pane.addImage("tmp3", 150 + (10 * q), 70, 2, 400, 350, 0);
			}
		});
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		GroupLayout mapPanelLayout = new GroupLayout(mapPanel);
		mapPanel.setLayout(mapPanelLayout);
		mapPanelLayout.setHorizontalGroup(mapPanelLayout.createSequentialGroup().addComponent(pane,
				GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
		mapPanelLayout.setVerticalGroup(mapPanelLayout.createParallelGroup().addComponent(pane,
				GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE));

		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		shortcutPanel = new JPanel();
		shortcutPanel.setBorder(BorderFactory.createTitledBorder("Shortcuts"));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(mapPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(shortcutPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap());

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(mapPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				.addGroup(layout
						.createSequentialGroup().addContainerGap().addComponent(shortcutPanel,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		setResizable(false);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setVisible(true);
	}

	private void initializeEnd() {
		System.err.println("Init exit!");
		System.exit(0);
	}
	
	private final class ImagePane extends JPanel {

		private List<ImageStorage> images = new List<>();
		private List<ImageParameterList> imageParameter = new List<>();
		private RenderTimer rT;
		
		public ImagePane() {
			rT = new RenderTimer(500);
			rT.run();
		}

		public void addImage(String name, int x, int y, int z, int width, int height, int rotation) {
			boolean imageIsInList = false, imageParameterIsInList = false;
			BufferedImage img;
			try {
				img = ImageIO.read(new File("images/" + name + ".png"));
			} catch (Exception e) {
				System.err.println("The image " + name + ".png does not exist and therefore cannot be drawn!");
				return;
			}
			images.toFirst();
			while (images.hasAccess() && !imageIsInList) {
				if (name.equals(images.getContent().name)) {
					images.getContent().increaseCounter();
					imageIsInList = true;
				}
				images.next();
			}
			if (!imageIsInList) {
				images.append(new ImageStorage(img, name));
				ImageParameterList list = new ImageParameterList(name);
				list.append(new Parameter(img, x, y, z, width, height, rotation));
				imageParameter.append(list);
				return;
			}

			imageParameter.toFirst();
			while (imageParameter.hasAccess()) {
				if (imageParameter.getContent().name.equals(name)) {
					ImageParameterList list = imageParameter.getContent();
					Parameter parameter = new Parameter(img, x, y, z, width, height, rotation);
					list.toFirst();
					imageParameterIsInList = false;
					while (list.hasAccess() && !imageParameterIsInList) {
						if (list.getContent().x == x && list.getContent().y == y && list.getContent().z == z
								&& list.getContent().width == width && list.getContent().height == height
								&& list.getContent().rotation == rotation) {
							System.out.println("!**");
							imageParameterIsInList = true;
							break;
						}
						list.next();
					}
					if (!imageParameterIsInList) {
						list.append(parameter);
					}
					break;
				}
				imageParameter.next();
			}
			repaint();
		}

		public void deleteImage(String name, int x, int y, int z, int width, int height, int rotation) {
			boolean imageIsInList = false;
			images.toFirst();
			while (images.hasAccess() && !imageIsInList) {
				if (name.equals(images.getContent().name)) {
					images.getContent().decreaseCounter();
					if (images.getContent().getCounter() < 1) {
						images.remove();
					}
					imageIsInList = true;
				}
				images.next();
			}
			if (!imageIsInList) {
				return;
			}

			imageParameter.toFirst();
			while (imageParameter.hasAccess()) {
				if (imageParameter.getContent().name.equals(name)) {
					ImageParameterList list = imageParameter.getContent();
					System.out.println("**!");
					list.toFirst();
					while (list.hasAccess()) {
						System.out.println("!");
						if (list.getContent().x == x && list.getContent().y == y && list.getContent().z == z
								&& list.getContent().width == width && list.getContent().height == height
								&& list.getContent().rotation == rotation) {
							list.remove();
							System.out.println("*!");
							repaint();
							return;
						}
						list.next();
					}
					return;
				}
				imageParameter.next();
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			boolean inserted;
			super.paintComponent(g);

			List<Parameter> sortedParameter = new List<>();
			imageParameter.toFirst();
			while (imageParameter.hasAccess()) {
				imageParameter.getContent().toFirst();
				while (imageParameter.getContent().hasAccess()) {
					sortedParameter.toFirst();
					if (sortedParameter.isEmpty()) {
						sortedParameter.append(imageParameter.getContent().getContent());
						break;
					} else {
						inserted = false;
						while (sortedParameter.hasAccess() && !inserted) {
							if (sortedParameter.getContent().z > imageParameter.getContent().getContent().z) {
								inserted = true;
								sortedParameter.insert(imageParameter.getContent().getContent());
								break;
							}
							sortedParameter.next();
						}
						if (!inserted) {
							sortedParameter.append(imageParameter.getContent().getContent());
						}
					}
					imageParameter.getContent().next();
				}
				imageParameter.next();
			}

			Graphics2D g2d = (Graphics2D) g.create();
			sortedParameter.toFirst();
			while (sortedParameter.hasAccess()) {
				Parameter parameter = sortedParameter.getContent();
				if (parameter.img != null) {
					g2d.drawImage(parameter.img, parameter.x, parameter.y, parameter.width, parameter.height, this);
				}
				sortedParameter.next();
			}
			g2d.dispose();
		}

		private final class RenderTimer implements Runnable {
			
			public final int renderTime;
			
			public RenderTimer(int renderTime) {
				this.renderTime = renderTime;
			}

			@Override
			public void run() {
				try {
					Thread.sleep(renderTime);
				} catch (Exception e) {
					System.err.println("Something wrong with the refreshing timer!");
				}
				repaint();
			}
			
		}
		
		private class ImageStorage {

			public final BufferedImage img;
			private int referenceCounter = 1;
			public final String name;

			public ImageStorage(BufferedImage img, String name) {
				this.img = img;
				this.name = name;
			}

			public void increaseCounter() {
				referenceCounter++;
			}

			public void decreaseCounter() {
				referenceCounter--;
			}

			public int getCounter() {
				return referenceCounter;
			}

		}

		private class ImageParameterList extends List<Parameter> {

			public final String name;

			public ImageParameterList(String name) {
				super();
				this.name = name;
			}

		}

		private class Parameter {

			public final int x, y, z, width, height, rotation;
			public final BufferedImage img;

			public Parameter(BufferedImage img, int x, int y, int z, int width, int height, int rotation) {
				this.img = img;
				this.x = x;
				this.y = y;
				this.z = z;
				this.width = width;
				this.height = height;
				this.rotation = rotation;
			}

		}

	}

}
