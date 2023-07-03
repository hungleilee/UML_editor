package UMLeditor;

import javax.swing.SwingUtilities;

public class Launcher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Menu menu = new Menu();
			menu.setVisible(true);
		});
	}

}
