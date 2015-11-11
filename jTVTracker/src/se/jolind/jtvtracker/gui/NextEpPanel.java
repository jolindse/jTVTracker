package se.jolind.jtvtracker.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NextEpPanel extends JPanel {

	private GridBagConstraints gc;
	
	private JLabel lblTEST;
	
	public NextEpPanel () {
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		
		lblTEST = new JLabel("TEST");
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.CENTER;
		add(lblTEST, gc);
		
	}
	
}
