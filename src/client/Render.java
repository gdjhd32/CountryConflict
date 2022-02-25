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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import classBase.List;
import general.Area;
import general.Map;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private final int FRAME_WIDTH = 1000, FRAME_HEIGHT = (int) (FRAME_WIDTH * 0.75), FRAME_X = 0, FRAME_Y = 0;
	private final int MAP_WIDTH = 800, MAP_HEIGHT = 500;

	private JPanel mapPanel, infoPanel, shortcutPanel;
	private Area[] areas;

	private JLabel[] names;

	private boolean canOpenInfoWindow;

	private final LayoutManager EMPTY_LAYOUT_MANAGER = new LayoutManager() {

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
	};

	public enum WindowComponent {
		MAP, INFO, SHORTCUT
	};

	public Render() {
		super("Country Conflict");
		setLocation(FRAME_X, FRAME_Y);
		addingComponents();
		canOpenInfoWindow = true;
	}

//	public void renderImage(String name, int x, int y, int z, int width, int height, int rotation,
//			WindowComponent location) {
//		try {
//			ImagePane iP = null;
//			switch (location) {
//			case Map:
//				iP = (ImagePane) mapPanel.getComponent(0);
//				break;
//			case Info:
//				iP = (ImagePane) infoPanel.getComponent(0);
//				break;
//			case Shortcut:
//				iP = (ImagePane) shortcutPanel.getComponent(0);
//				break;
//			default:
//				System.err.println("Invalid location!");
//				break;
//			}
//			iP.addImage(name, x, y, z, width, height, rotation);
//		} catch (Exception e) {
//			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
//		}
//	}
//
//	public void deleteImage(String name, int x, int y, int z, int width, int height, int rotation,
//			WindowComponent location) {
//		try {
//			ImagePane iP = null;
//			switch (location) {
//			case Map:
//				iP = (ImagePane) mapPanel.getComponent(0);
//				break;
//			case Info:
//				iP = (ImagePane) infoPanel.getComponent(0);
//				break;
//			case Shortcut:
//				iP = (ImagePane) shortcutPanel.getComponent(0);
//				break;
//			default:
//				System.err.println("Invalid location!");
//				break;
//			}
//			iP.deleteImage(name, x, y, z, width, height, rotation);
//		} catch (Exception e) {
//			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
//		}
//	}

	public void renderMap(Map map) {
		ImagePane iP;
		try {
			iP = (ImagePane) mapPanel.getComponent(0);
			iP.setBounds(0, 0, map.mapImageWidth, map.mapImageHeigth);
		} catch (Exception e) {
			System.err.println("Object mapPanel does not have a ImagePane object at index 0!");
			return;
		}
		iP.addImage(map.mapImageName, 0, 0, Integer.MIN_VALUE, map.mapImageWidth, map.mapImageHeigth, 0);
		areas = map.getAreas();
		names = new JLabel[areas.length];
		InformationPopUpWindow[] ipuws = new InformationPopUpWindow[areas.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = new JLabel(areas[i].NAME);
			Rectangle2D r = iP.getGraphics().getFont().getStringBounds(areas[i].NAME,
					((Graphics2D) iP.getGraphics()).getFontRenderContext());
			names[i].setBounds(areas[i].LABEL_X, areas[i].LABEL_Y, (int) r.getWidth() + areas[i].ADDITIONAL_LABEL_WIDTH,
					14);
			names[i].setForeground(new Color(0, 255, 0));
			iP.add(names[i]);

			ipuws[i] = new InformationPopUpWindow(areas[i], i);
			names[i].addMouseListener(new AreaMouseListener(names[i], ipuws[i]));
		}
		for (int i = 0; i < ipuws.length; i++) {
			mapPanel.add(ipuws[i]);
		}
		Component cTemp = mapPanel.getComponent(0);
		for (int i = 1; i < mapPanel.getComponentCount(); i++) {
			mapPanel.setComponentZOrder(mapPanel.getComponent(i), i - 1);
		}
		mapPanel.setComponentZOrder(cTemp, mapPanel.getComponentCount() - 1);
		configuartionOfDragableComponentsOfMap();
		mapPanel.repaint();
	}
	
	private void configuartionOfDragableComponentsOfMap() {
		for (int i = 0; i < mapPanel.getComponentCount(); i++) {
			mapPanel.getComponent(i).addMouseListener(new DragableWindowMouseListener(mapPanel.getComponent(i)));
			mapPanel.getComponent(i).addMouseMotionListener(new DragableWindowMouseControl(mapPanel, i));
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
		ImagePane mapImagePane = new ImagePane();
		mapImagePane.setLayout(EMPTY_LAYOUT_MANAGER);

		mapPanel = new JPanel();
		mapPanel.setLayout(EMPTY_LAYOUT_MANAGER);
		mapPanel.add(mapImagePane);

		// creating infoPanel
		JPanel infoSkeletonPanel = new JPanel();
		infoSkeletonPanel.setBounds(0, 0, MAP_WIDTH, 100);
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
		shortcutSkeletonPanel.setBounds(0, 0, 100, MAP_HEIGHT);
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
		layout.setHorizontalGroup(layout.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(mapPanel, MAP_WIDTH, MAP_WIDTH, MAP_WIDTH)
										.addComponent(infoPanel, infoSkeletonPanel.getWidth(),
												infoSkeletonPanel.getWidth(), infoSkeletonPanel.getWidth()))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(shortcutPanel, shortcutSkeletonPanel.getWidth(), shortcutSkeletonPanel.getWidth(),
						shortcutSkeletonPanel.getWidth())
				.addContainerGap());

		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(mapPanel, MAP_HEIGHT, MAP_HEIGHT, MAP_HEIGHT)
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

	private class DragableWindowMouseListener extends MouseAdapter {

		private Component c;
		
		public DragableWindowMouseListener(Component c) {
			this.c = c;
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				((DragableWindowMouseControl) c.getMouseMotionListeners()[0]).setMouseCoordinates(e.getXOnScreen(),
						e.getYOnScreen());
				((DragableWindowMouseControl) c.getMouseMotionListeners()[0]).setDragable(true);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				((DragableWindowMouseControl) c.getMouseMotionListeners()[0]).resetMouseCoordinates();
				((DragableWindowMouseControl) c.getMouseMotionListeners()[0]).setDragable(false);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}
	}

	private class DragableWindowMouseControl extends MouseMotionAdapter {

		private int x = 0, y = 0;
		private int mX = -1, mY = -1;
		private boolean dragable = false;
		private int index = 0;
		private JPanel parent;
		private boolean firstDragging;

		public DragableWindowMouseControl(JPanel parentJPanel, int index) {
			this.index = index;
			parent = parentJPanel;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (dragable) {
				Component c = null;
				try {
					c = parent.getComponent(index);
				} catch (Exception ex) {
					System.err.println("Object mapPanel does not have an object at " + index + ".");
				}
				if (mX != -1 && mY != -1) {
					if (!firstDragging) {
						x += e.getXOnScreen() - mX;
						y += e.getYOnScreen() - mY;
					} else {
						x = c.getX();
						y = c.getY();
						firstDragging = false;
					}
					if (c.getWidth() < MAP_WIDTH) {
						if (x < 0)
							x = 0;
						if (x > MAP_WIDTH - c.getWidth())
							x = MAP_WIDTH - c.getWidth();
					} else {
						if (x > 0)
							x = 0;
						if (x < MAP_WIDTH - c.getWidth())
							x = MAP_WIDTH - c.getWidth();
					}
					if (c.getWidth() < MAP_WIDTH) {
						if (y < 0)
							y = 0;
						if (y > MAP_HEIGHT - c.getWidth())
							y = MAP_HEIGHT - c.getWidth();
					} else {
						if (y > 0)
							y = 0;
						if (y < MAP_HEIGHT - c.getHeight())
							y = MAP_HEIGHT - c.getHeight();
					}
				}
				c.setBounds(x, y, c.getWidth(), c.getHeight());
				parent.repaint();
				mX = e.getXOnScreen();
				mY = e.getYOnScreen();
			}
		}

		public void setMouseCoordinates(int mX, int mY) {
			firstDragging = true;
			this.mX = mX;
			this.mY = mY;
		}

		public void resetMouseCoordinates() {
			mX = -1;
			mY = -1;
		}

		public void setDragable(boolean dragable) {
			this.dragable = dragable;
		}

	}

	private final class InformationPopUpWindow extends JPanel {

		private final Area AREA;
		private final int X, Y;
		private int width = 100, height = 100;

		public InformationPopUpWindow(Area area, int index) {
			super();
			AREA = area;
			X = (MAP_WIDTH - width) / 2;
			Y = (MAP_HEIGHT - height) / 2;
			setBounds(X, Y, width, height);
			setBorder(BorderFactory.createTitledBorder(AREA.NAME));
			setVisible(false);
			setEnabled(false);
		}

		public void open() {
			setVisible(true);
			setEnabled(true);
		}

		public void close() {
			setVisible(false);
			setEnabled(false);
			setBounds(X, Y, width, height);
		}

	}

	private final class AreaMouseListener implements MouseListener {

		private final JLabel AREA;
		private final InformationPopUpWindow IPUW;

		public AreaMouseListener(JLabel area, InformationPopUpWindow ipuw) {
			AREA = area;
			IPUW = ipuw;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (canOpenInfoWindow) {
				IPUW.open();
				canOpenInfoWindow = false;
			} else if (IPUW.isVisible()) {
				canOpenInfoWindow = true;
				IPUW.close();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			AREA.setForeground(new Color(255, 100, 0));
			mapPanel.repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			AREA.setForeground(new Color(0, 255, 0));
			mapPanel.repaint();
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
				if (name.equals(images.getContent().NAME)) {
					images.getContent().increaseCounter();
					imageIsInList = true;
				}
				images.next();
			}
			if (!imageIsInList) {
				images.append(new ImageStorage(name));
				ImageParameterList list = new ImageParameterList(name);
				Parameter param = new Parameter(img, x, y, z, width, height, rotation);
				list.append(param);
				imageParameter.append(list);
				repaint();
				return;
			}

			imageParameter.toFirst();
			while (imageParameter.hasAccess()) {
				if (imageParameter.getContent().NAME.equals(name)) {
					ImageParameterList list = imageParameter.getContent();
					Parameter parameter = new Parameter(img, x, y, z, width, height, rotation);
					list.toFirst();
					imageParameterIsInList = false;
					while (list.hasAccess() && !imageParameterIsInList) {
						if (list.getContent().X == x && list.getContent().Y == y && list.getContent().Z == z
								&& list.getContent().WIDTH == width && list.getContent().HEIGHT == height
								&& list.getContent().ROTATION == rotation) {
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
				if (name.equals(images.getContent().NAME)) {
					images.getContent().decreaseCounter();
					if (images.getContent().getCounter() < 1) {
						images.remove();
						imageParameter.toFirst();
						while (imageParameter.hasAccess()) {
							if (imageParameter.getContent().NAME.equals(name)) {
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
				if (imageParameter.getContent().NAME.equals(name)) {
					ImageParameterList list = imageParameter.getContent();
					list.toFirst();
					while (list.hasAccess()) {
						if (list.getContent().X == x && list.getContent().Y == y && list.getContent().Z == z
								&& list.getContent().WIDTH == width && list.getContent().HEIGHT == height
								&& list.getContent().ROTATION == rotation) {
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
							if (sortedParameter.getContent().Z > imageParameter.getContent().getContent().Z) {
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
				if (parameter.IMG != null) {
					old = g2d.getTransform();
					g2d.rotate(Math.toRadians(parameter.ROTATION), parameter.X + (parameter.WIDTH / 2),
							parameter.Y + (parameter.HEIGHT / 2));
					g2d.drawImage(parameter.IMG, parameter.X, parameter.Y, parameter.WIDTH, parameter.HEIGHT, this);
					g2d.setTransform(old);
				}
				sortedParameter.next();
			}
			g2d.dispose();
		}

		private class ImageStorage {

			private int referenceCounter = 1;
			public final String NAME;

			public ImageStorage(String name) {
				NAME = name;
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

			public final String NAME;

			public ImageParameterList(String name) {
				super();
				NAME = name;
			}

		}

		private class Parameter {

			public final int X, Y, Z, WIDTH, HEIGHT, ROTATION;
			public final BufferedImage IMG;

			public Parameter(BufferedImage img, int x, int y, int z, int width, int height, int rotation) {
				IMG = img;
				X = x;
				Y = y;
				Z = z;
				WIDTH = width;
				HEIGHT = height;
				ROTATION = rotation;
			}

		}

	}

}
