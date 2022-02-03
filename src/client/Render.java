package client;

import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class Render extends JFrame {

	private Graphics g;
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

		TextArea ta = new TextArea("Hello World!");

		mapPanel = new JPanel();
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		GroupLayout mapPanelLayout = new GroupLayout(mapPanel);
		mapPanel.setLayout(mapPanelLayout);
		mapPanelLayout.setHorizontalGroup(mapPanelLayout.createSequentialGroup().addComponent(ta,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		mapPanelLayout.setVerticalGroup(mapPanelLayout.createSequentialGroup().addComponent(ta,
				GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));

		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		shortcutPanel = new JPanel();
		shortcutPanel.setBorder(BorderFactory.createTitledBorder("Shortcuts"));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup()
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

}
