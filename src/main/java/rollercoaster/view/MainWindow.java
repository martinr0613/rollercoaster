package rollercoaster.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import rollercoaster.controller.Board;
import rollercoaster.controller.Logic;

public class MainWindow extends JFrame implements ActionListener {

	private Board board;
	private Logic logic;
	private GridContainer gridContainer;
	private InfoPanel infoPanel;
	private GuestList guestList;
	private int startingMoney = 1200;

	public MainWindow() {
		initUI();
	}

	public void tick() {
		if (logic == null || gridContainer == null || infoPanel == null || guestList == null) {
			return;
		}
		logic.tick();
		infoPanel.tick();
		guestList.tick();
		gridContainer.tick();

	}

	private void initUI() {
		setTitle("Roller Coaster");
		setSize(1280, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		getContentPane().add(new MainMenu(new StartGameActionListener(), new CustomGameActionListener()));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(null, "Biztosan ki akar lépni?", "Kilépés",
						JOptionPane.YES_NO_OPTION);
				if (i == 0)
					System.exit(0);
			}
		});
	}

	private void startGame() {
		int N = 11;
		int M = 15;
		board = new Board(N, M);
		logic = new Logic(N, M, board, startingMoney);
		getContentPane().removeAll();
		setLayout(new BorderLayout());
		gridContainer = new GridContainer(N, M, logic, board);
		getContentPane().add(gridContainer);

		guestList = new GuestList(logic);
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new FlowLayout());
		rightPane.setPreferredSize(new Dimension(300, 800));
		rightPane.add(new GameObjectSelector(board));
		rightPane.add(guestList);
		getContentPane().add(rightPane, BorderLayout.LINE_END);

		infoPanel = new InfoPanel(logic);
		getContentPane().add(infoPanel, BorderLayout.PAGE_END);
		revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// System.exit(0);
	}

	private class StartGameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// JButton gameButton = (JButton) event.getSource();
			startGame();
		}
	}

	private class CustomGameActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			int input = 1200;
			String inputString = JOptionPane.showInputDialog(null, "Adja meg a kezdőtőkét", startingMoney);

			do {
				if (inputString == null) {
					break;
				}
				if (isNumber(inputString)) {
					input = Integer.parseInt(inputString.trim());
				} else {
					JOptionPane.showMessageDialog(null, "Csak számokat adjon meg!");
					inputString = JOptionPane.showInputDialog(null, "Adja meg a kezdőtőkét", startingMoney);
					if (inputString == null) {
						break;
					}
				}
				if (input < 0) {
					JOptionPane.showMessageDialog(null, "Pozitív számot adjon meg!");
					inputString = JOptionPane.showInputDialog(null, "Adja meg a kezdőtőkét", startingMoney);
					if (inputString == null) {
						break;
					}
				}
			} while (!isNumber(inputString) || input < 0);
			startingMoney = input;
			if (inputString != null) {
				startGame();
			}
		}

		private boolean isNumber(String s) {
			return s.matches("^[0-9]+$");
		}
	}

}
