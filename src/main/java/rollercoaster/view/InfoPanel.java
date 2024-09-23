package rollercoaster.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rollercoaster.controller.Logic;
import rollercoaster.model.Maintainer;

public class InfoPanel extends JPanel {

	private final Logic logic;
	private final JLabel dayLabel;
	private final JLabel elapsedTimeLabel;
	private final JLabel moneyLabel;
	private final JLabel dailyIncomeLabel;
	private final JLabel currentGuestLabel;
	private final JLabel maintainerCountLabel;

	private final JLabel dayTextLabel;
	private final JLabel elapsedTimeTextLabel;
	private final JLabel moneyTextLabel;
	private final JLabel dailyIncomeTextLabel;
	private final JLabel currentGuestTextLabel;
	private final JLabel maintainerCountTextLabel;

	private final JButton openParkButton;
	private final JButton pauseButton;
	private final JButton gameSpeedChangeButton;
	private final JButton hireNewMaintainerButton;

	public InfoPanel(Logic logic) {
		this.logic = logic;
		setPreferredSize(new Dimension(100, 50));

		Font labelFont = new Font("Garamond", Font.BOLD, 16);
		Font dataFont = new Font("Garamond", Font.PLAIN, 16);

		dayTextLabel = createLabel("Current day: ", labelFont);
		dayLabel = createLabel("0", dataFont);

		elapsedTimeTextLabel = createLabel("Elapsed time: ", labelFont);
		elapsedTimeLabel = createLabel("00:00:00", dataFont);

		moneyTextLabel = createLabel("Money: ", labelFont);
		moneyLabel = createLabel("0$", dataFont);

		dailyIncomeTextLabel = createLabel("Daily income: ", labelFont);
		dailyIncomeLabel = createLabel("0$", dataFont);

		currentGuestTextLabel = createLabel("Guest: ", labelFont);
		currentGuestLabel = createLabel("0", dataFont);

		maintainerCountTextLabel = createLabel("Maintainers: ", labelFont);
		maintainerCountLabel = createLabel("0", dataFont);

		gameSpeedChangeButton = new JButton();
		gameSpeedChangeButton.addActionListener(new ChangeGameSpeedActionListener());
		setGameSpeedButton(gameSpeedChangeButton, logic.getGameSpeed() + 1);

		pauseButton = new JButton();
		pauseButton.addActionListener(new PauseActionListener());
		pauseButton.setText(logic.isRunning() ? "Pause" : "Start");

		openParkButton = new JButton();
		openParkButton.addActionListener(new OpenParkActionListener());
		openParkButton.setText("Open park");

		add(openParkButton);

		hireNewMaintainerButton = new JButton();
		hireNewMaintainerButton.addActionListener(new HireNewMaintainerListener());
		hireNewMaintainerButton.setText("Hire new maintainer");





//		add(pauseButton);
//		add(gameSpeedChangeButton);
//		add(dayTextLabel);
//		add(dayLabel);
//		add(new JLabel("    "));
//		add(elapsedTimeTextLabel);
//		add(elapsedTimeLabel);
		add(new JLabel("    "));
		add(moneyTextLabel);
		add(moneyLabel);
//		add(new JLabel("    "));
//		add(dailyIncomeTextLabel);
//		add(dailyIncomeLabel);
//		add(new JLabel("    "));
//		add(currentGuestTextLabel);
//		add(currentGuestLabel);

	}

	private JLabel createLabel(String text, Font font) {
		final JLabel label = new JLabel(text);
		label.setFont(font);
		return label;
	}

	public void tick() {
		dayLabel.setText("" + logic.getLastDay());
		elapsedTimeLabel.setText(formatDuration(logic.getElapsedTime()));
		moneyLabel.setText("$" + logic.getCurrentMoney());
		dailyIncomeLabel.setText("$" + logic.getLastPeriodIncome());
		currentGuestLabel.setText("" + logic.getGuestCount());
		maintainerCountLabel.setText(""+logic.getMaintainers().size());
	}

	private String formatDuration(long duration) {
		final long seconds = duration / 1_000_000_000;
		return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
	}

	private void setGameSpeedButton(JButton btn, int speed) {
		btn.setText("Change speed to " + speed + "x");
		btn.setActionCommand("" + speed);
	}

	private class PauseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final JButton btn = (JButton) e.getSource();
			logic.setRunning(!logic.isRunning());
			btn.setText(logic.isRunning() ? "Pause" : "Start");
		}

	}

	private class OpenParkActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final JButton btn = (JButton) e.getSource();
			logic.openPark(true);
			remove(btn);





			add(new JLabel("  "));
			add(elapsedTimeLabel, 0);
			add(elapsedTimeTextLabel, 0);
			add(new JLabel("  "), 0);
			add(dayLabel, 0);
			add(dayTextLabel, 0);
			add(gameSpeedChangeButton, 0);
			add(pauseButton, 0);

			add(new JLabel("  "));
			add(dailyIncomeTextLabel);
			add(dailyIncomeLabel);
			add(new JLabel("  "));
			add(currentGuestTextLabel);
			add(currentGuestLabel);
			add(new JLabel("  "));
			add(maintainerCountTextLabel);
			add(maintainerCountLabel);
			add(new JLabel("  "));
			add(hireNewMaintainerButton);






		}

	}

	private class ChangeGameSpeedActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final JButton btn = (JButton) e.getSource();
			int selectedGameSpeed = Integer.parseInt(btn.getActionCommand());
			logic.setGameSpeed(selectedGameSpeed);

			selectedGameSpeed = ((selectedGameSpeed) % 3) + 1;
			setGameSpeedButton(btn, selectedGameSpeed);
		}

	}

	private class HireNewMaintainerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			logic.getMaintainers().add(new Maintainer(logic.getBoard().getBoardElement(logic.getBoard().getN() / 2, 0).get()));
		}
	}

}