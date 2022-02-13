package client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
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

	private final int FRAME_WIDTH = 1000, FRAME_HEIGHT = (int) (FRAME_WIDTH * 0.75), FRAME_X = 0, FRAME_Y = 0;

	private int right = 0, down = 0, rotation = 0; // testing purpose variables

	private int refreshTime = 0;

	private JPanel mapPanel, infoPanel, shortcutPanel;

	public static void main(String[] args) {
		new Render(100);
	}

	public Render(int refreshTime) {
		super("Country Conflict");
		setLocation(FRAME_X, FRAME_Y);
		this.refreshTime = refreshTime;
		addingComponents();
	}

	public void changeRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
		try {
			ImagePane iP = (ImagePane) mapPanel.getComponent(0);
			iP.changeRefreshTime(refreshTime);
		} catch (Exception e) {
			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
		}
	}

	public void renderMapImage(String name, int x, int y, int z, int width, int height, int rotation) {
		try {
			ImagePane iP = (ImagePane) mapPanel.getComponent(0);
			iP.addImage(name, x, y, z, width, height, rotation);
		} catch (Exception e) {
			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
		}
	}

	public void deleteMapImage(String name, int x, int y, int z, int width, int height, int rotation) {
		try {
			ImagePane iP = (ImagePane) mapPanel.getComponent(0);
			iP.deleteImage(name, x, y, z, width, height, rotation);
		} catch (Exception e) {
			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
		}
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

			}

		});

		JPanel mapSkeletonPanel = new JPanel();

		ImagePane mapImagePane = new ImagePane(refreshTime);
		mapImagePane.setBounds(5, 15, 710, 500);
		mapImagePane.addImage("tmp", 0, 0, 0, 710, 500, 0);
		mapImagePane.addImage("tmp2", 100, 10, 1, 400, 350, 0);
		mapImagePane.addImage("tmp3", 150, 70, 2, 400, 350, 0);

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
				mapImagePane.deleteImage("tmp3", 150 + (5 * right), 70 + (5 * down), 2, 400, 350, rotation);
				if (e.getButton() == 1) {
					right++;
				} else if (e.getButton() == 2) {
					rotation += 10;
				} else if (e.getButton() == 3) {
					down++;
				}
				mapImagePane.addImage("tmp3", 150 + (5 * right), 70 + (5 * down), 2, 400, 350, rotation);
			}
		});

		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		GroupLayout mapPanelLayout = new GroupLayout(mapPanel);
		mapPanel.setLayout(mapPanelLayout);
		mapPanel.add(mapImagePane);
		mapPanelLayout.setHorizontalGroup(mapPanelLayout.createSequentialGroup().addComponent(mapSkeletonPanel,
				GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
		mapPanelLayout.setVerticalGroup(mapPanelLayout.createParallelGroup().addComponent(mapSkeletonPanel,
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

		public ImagePane(int refreshTime) {
			rT = new RenderTimer(refreshTime);
			rT.start();
		}

		public void changeRefreshTime(int refreshTime) {
			rT.changeRefreshTime(refreshTime);
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
				Parameter param = new Parameter(img, x, y, z, width, height, rotation);
				list.append(param);
				imageParameter.append(list);
				repaint();
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
						imageParameter.toFirst();
						while (imageParameter.hasAccess()) {
							if (imageParameter.getContent().name.equals(name)) {
								imageParameter.remove();
								break;
							}
							imageParameter.next();
						}
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
					list.toFirst();
					while (list.hasAccess()) {
						if (list.getContent().x == x && list.getContent().y == y && list.getContent().z == z
								&& list.getContent().width == width && list.getContent().height == height
								&& list.getContent().rotation == rotation) {
							list.remove();
							return;
						}
						list.next();
					}
					repaint();
					return;
				}
				imageParameter.next();
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			boolean inserted;

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

			// for smoother rendering
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			AffineTransform old = null; // used to store the original rotation value
			sortedParameter.toFirst();
			while (sortedParameter.hasAccess()) {
				Parameter parameter = sortedParameter.getContent();
				if (parameter.img != null) {
					old = g2d.getTransform();
					g2d.rotate(Math.toRadians(parameter.rotation), parameter.x + (parameter.width / 2),
							parameter.y + (parameter.height / 2));
					g2d.drawImage(parameter.img, parameter.x, parameter.y, parameter.width, parameter.height, this);
					g2d.setTransform(old);
				}
				sortedParameter.next();
			}
			g2d.dispose();
		}

		private final class RenderTimer extends Thread {

			public int refreshTime;

			public RenderTimer(int refreshTime) {
				this.refreshTime = refreshTime;
			}

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(refreshTime);
					} catch (Exception e) {
						System.err.println("Something wrong with the refreshing timer!");
					}
					repaint();
				}
			}

			public void changeRefreshTime(int refreshTime) {
				this.refreshTime = refreshTime;
			}

		}

		private class ImageStorage {

			@SuppressWarnings("unused")
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
