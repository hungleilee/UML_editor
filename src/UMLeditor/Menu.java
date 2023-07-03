package UMLeditor;

import UMLeditor.BasicObject.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Menu extends JFrame {
	private String currentMode;
	private Canvas canvas;

	private void showDialogForChangingObjectName(BasicObject selectedObject) {
		
		// Change Object Name
		JDialog dialog = new JDialog(this, "Change Object Name", true);
		dialog.setSize(300, 150);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());

		// 輸入名稱區域
		JTextArea textArea = new JTextArea(selectedObject.getName());
		JScrollPane scrollPane = new JScrollPane(textArea);
		dialog.add(scrollPane, BorderLayout.CENTER);

		//新增介面，包含OK和Cancel按鈕
		JPanel dialogButtonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		dialogButtonPanel.add(okButton);
		dialogButtonPanel.add(cancelButton);
		dialog.add(dialogButtonPanel, BorderLayout.SOUTH);

		//處理ok並關閉視窗
		okButton.addActionListener(e -> {
			selectedObject.setName(textArea.getText());
			canvas.repaint();
			dialog.dispose();
		});

		//處理cancel並關閉視窗
		cancelButton.addActionListener(e -> dialog.dispose());

		//
		dialog.setVisible(true);
	}

	public Menu() {
		setTitle("UML Editor");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Top menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		JMenuItem changeObjectName = new JMenuItem("Change Object Name");
		changeObjectName.addActionListener(e -> {
			BasicObject selectedObject = canvas.getSelectedObject();
			if (selectedObject != null) {
				showDialogForChangingObjectName(selectedObject);
			}
		});
		editMenu.add(changeObjectName);
		JMenuItem groupMenuItem = new JMenuItem("Group");
		JMenuItem ungroupMenuItem = new JMenuItem("UnGroup");
		editMenu.add(groupMenuItem);
		editMenu.add(ungroupMenuItem);
		groupMenuItem.addActionListener(e -> canvas.groupSelectedObjects());
		ungroupMenuItem.addActionListener(e -> canvas.ungroupSelectedObjects());

		setJMenuBar(menuBar);

		// Mode selection buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		String[] buttonLabels = { "Select", "Association Line", "Generalization Line", "Composition Line", "Class",
				"Use Case" };
		for (String label : buttonLabels) {
			JButton button = new JButton(label);
			button.addActionListener(e -> {
				currentMode = label;
				for (Component c : buttonPanel.getComponents()) {
					if (c instanceof JButton) {
						((JButton) c).setBackground(Color.WHITE);
					}
				}
				button.setBackground(Color.BLACK);
			});
			buttonPanel.add(button);
		}
		add(buttonPanel, BorderLayout.WEST);

		// Editing area
		//canvas = new Canvas();
		canvas = new Canvas(this);
		add(canvas, BorderLayout.CENTER);
	}
	
	public String getMode() {
        return currentMode;
    }



	
}