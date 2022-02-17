package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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

	private JPanel mapPanel, infoPanel, shortcutPanel;

	public static void main(String[] args) {
		new Render();
	}

	public Render() {
		super("Country Conflict");
		setLocation(FRAME_X, FRAME_Y);
		addingComponents();
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
			public void windowClosing(WindowEvent e) {
				// ending program on exit
				initializeEnd();
			}

		});

		// creating mapPanel
		JPanel mapSkeletonPanel = new JPanel();
		mapSkeletonPanel.setBounds(0, 0, 800, 500);

		ImagePane mapImagePane = new ImagePane();
		mapImagePane.setBounds(0, 0, 2000, 1000);
		mapImagePane.addImage("tmp", 0, 0, 0, 2000, 1000, 0);
		mapImagePane.addImage("tmp2", 100, 10, 1, 400, 350, 0);
		mapImagePane.addImage("tmp3", 150, 70, 2, 400, 350, 0);
		mapImagePane.setLayout(new LayoutManager() {

			@Override
			public void removeLayoutComponent(Component comp) {

			}

			@Override
			public Dimension preferredLayoutSize(Container parent) {
				return null;
			}

			@Override
			public Dimension minimumLayoutSize(Container parent) {
				return null;
			}

			@Override
			public void layoutContainer(Container parent) {

			}

			@Override
			public void addLayoutComponent(String name, Component comp) {

			}
		});

		mapPanel = new JPanel();
		mapPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					((MouseDraggingControl) mapPanel.getMouseMotionListeners()[0]).resetMouseCoordinates();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					((MouseDraggingControl) mapPanel.getMouseMotionListeners()[0]).setMouseCoordinates(e.getX(),
							e.getY());
				}
			}
		});

		mapPanel.addMouseMotionListener(new MouseDraggingControl());
		mapPanel.add(mapImagePane);
		GroupLayout mapPanelLayout = new GroupLayout(mapPanel);
		mapPanel.setLayout(mapPanelLayout);
		mapPanelLayout.setHorizontalGroup(mapPanelLayout.createSequentialGroup().addComponent(mapSkeletonPanel,
				mapSkeletonPanel.getWidth(), mapSkeletonPanel.getWidth(), mapSkeletonPanel.getWidth()));
		mapPanelLayout.setVerticalGroup(mapPanelLayout.createSequentialGroup().addComponent(mapSkeletonPanel,
				mapSkeletonPanel.getHeight(), mapSkeletonPanel.getHeight(), mapSkeletonPanel.getHeight()));

		// creating infoPanel
		JPanel infoSkeletonPanel = new JPanel();
		infoSkeletonPanel.setBounds(0, 0, mapSkeletonPanel.getWidth(), 100);
		infoSkeletonPanel.setBackground(new Color(0, 0, 0));

		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		GroupLayout infoPanelLayout = new GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createSequentialGroup().addComponent(infoSkeletonPanel,
				infoSkeletonPanel.getWidth(), infoSkeletonPanel.getWidth(), infoSkeletonPanel.getWidth()));
		infoPanelLayout.setVerticalGroup(infoPanelLayout.createSequentialGroup().addComponent(infoSkeletonPanel,
				infoSkeletonPanel.getHeight(), infoSkeletonPanel.getHeight(), infoSkeletonPanel.getHeight()));

		// creating shortcutPanel
		JPanel shortcutSkeletonPanel = new JPanel();
		shortcutSkeletonPanel.setBounds(0, 0, 100, mapSkeletonPanel.getHeight());
		shortcutSkeletonPanel.setBackground(new Color(0, 0, 0));

		shortcutPanel = new JPanel();
		shortcutPanel.setBorder(BorderFactory.createTitledBorder("Shortcuts"));
		GroupLayout shortcutPanelLayout = new GroupLayout(shortcutPanel);
		shortcutPanel.setLayout(shortcutPanelLayout);
		shortcutPanelLayout.setHorizontalGroup(shortcutPanelLayout.createSequentialGroup().addComponent(
				shortcutSkeletonPanel, shortcutSkeletonPanel.getWidth(), shortcutSkeletonPanel.getWidth(),
				shortcutSkeletonPanel.getWidth()));
		shortcutPanelLayout.setVerticalGroup(shortcutPanelLayout.createSequentialGroup().addComponent(
				shortcutSkeletonPanel, shortcutSkeletonPanel.getHeight(), shortcutSkeletonPanel.getHeight(),
				shortcutSkeletonPanel.getHeight()));

		// creating layout for all major components
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(mapPanel, mapSkeletonPanel.getWidth(), mapSkeletonPanel.getWidth(),
								mapSkeletonPanel.getWidth())
						.addComponent(infoPanel, infoSkeletonPanel.getWidth(), infoSkeletonPanel.getWidth(),
								infoSkeletonPanel.getWidth()))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(shortcutPanel, shortcutSkeletonPanel.getWidth(), shortcutSkeletonPanel.getWidth(),
						shortcutSkeletonPanel.getWidth())
				.addContainerGap());

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(mapPanel, mapSkeletonPanel.getHeight(), mapSkeletonPanel.getHeight(),
								mapSkeletonPanel.getHeight())
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(infoPanel, infoSkeletonPanel.getHeight(), infoSkeletonPanel.getHeight(),
								infoSkeletonPanel.getHeight())
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(shortcutPanel,
						shortcutSkeletonPanel.getHeight(), shortcutSkeletonPanel.getHeight(),
						shortcutSkeletonPanel.getHeight())));

		// settings for the JFrame
		setResizable(false);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setVisible(true);
		setBounds(200, 100, getWidth(), getHeight());
	}

	private void initializeEnd() {
		System.err.println("Init exit!");
		System.exit(0);
	}

	private class MouseDraggingControl implements MouseMotionListener {

		int x = 0, y = 0;
		int mX = -1, mY = -1;

		public MouseDraggingControl() {

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			ImagePane iP = null;
			JPanel jP = null;
			try {
				iP = (ImagePane) mapPanel.getComponent(0);
				jP = (JPanel) mapPanel.getComponent(1);
			} catch (Exception ex) {
				System.err.println("Object mapPanel does not have the right objects at 0 or 1.");
			}
			if (mX != -1 && mY != -1) {
				x += e.getX() - mX;
				y += e.getY() - mY;
				if (x > 0)
					x = 0;
				if (x < jP.getWidth() - iP.getWidth())
					x = jP.getWidth() - iP.getWidth();
				if (y > 0)
					y = 0;
				if (y < jP.getHeight() - iP.getHeight())
					y = jP.getHeight() - iP.getHeight();
			}
			iP.setBounds(x, y, iP.getWidth(), iP.getHeight());
			mX = e.getX();
			mY = e.getY();
		}

		public void setMouseCoordinates(int mX, int mY) {
			this.mX = mX;
			this.mY = mY;
		}

		public void resetMouseCoordinates() {
			mX = -1;
			mY = -1;
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

	}

	private final class ImagePane extends JPanel {

		private List<ImageStorage> images = new List<>();
		private List<ImageParameterList> imageParameter = new List<>();

		public ImagePane() {

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
