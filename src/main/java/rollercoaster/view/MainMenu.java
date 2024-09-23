package rollercoaster.view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

	private final Image background;

	public MainMenu(ActionListener startGameActionListener, ActionListener customGameListener) {
		final int MAINWINDOW_WIDTH = 1280;
		final int MAINWINDOW_HEIGHT = 800;
		final int BUTTON_WIDTH = 200;
		final int BUTTON_HEIGHT = 50;

		this.setBackground(Color.darkGray);
		this.setLayout(null);
		this.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(1, 0, 1)));
		this.setBounds(1000, 0, 120, 720);
		this.setPreferredSize(new Dimension(120, 720));
		try {
			background = ImageIO
					.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("MenuBackground.png")));
		} catch (Exception e) {
			throw new AssertionError("Missing MenuBackground image");
		}

		int size = 50;
		final JButton btn = new JButton("Játék indítása");
		btn.addActionListener(startGameActionListener);
		btn.setBounds(MAINWINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, MAINWINDOW_HEIGHT / 3 - BUTTON_HEIGHT / 2, BUTTON_WIDTH,
				BUTTON_HEIGHT);

		final JButton btn2 = new JButton("Egyedi játék indítása");
		btn2.addActionListener(customGameListener);
		btn2.setBounds(MAINWINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, (MAINWINDOW_HEIGHT * 2) / 3 - BUTTON_HEIGHT / 2,
				BUTTON_WIDTH, BUTTON_HEIGHT);

		add(btn);
		add(btn2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);

	}
}
