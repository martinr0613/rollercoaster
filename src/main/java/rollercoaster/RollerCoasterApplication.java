package rollercoaster;

import javax.swing.SwingUtilities;

import rollercoaster.view.MainWindow;

public class RollerCoasterApplication {

	public static void main(String[] args) throws InterruptedException {
		MainWindow window = new MainWindow();
		SwingUtilities.invokeLater(() -> {
			window.setVisible(true);
		});
		while (true) {
			window.tick();
			window.revalidate();
			window.repaint();
			Thread.sleep(10);
		}
	}

}
