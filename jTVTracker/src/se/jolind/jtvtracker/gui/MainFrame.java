package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

	public MainFrame() {
		super("jTVTracker v0.01a");
		this.setSize(new Dimension(400, 500));
		this.setLayout(new BorderLayout());
		
		// Set native platform look And feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setVisible(true);
		
	}
}
