package se.jolind.jtvtracker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomPanel extends JPanel {

	private JButton btnExit;
	
	public BottomPanel(){
		
		setPreferredSize(new Dimension(400,60));
		btnExit = new JButton("Exit");
		setBackground(Color.BLUE);

		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
	add(btnExit);
	}
}
