package se.jolind.jtvtracker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class BottomPanel extends JPanel {

	private JButton btnExit;
	private JProgressBar progressBar;
	private int progressValue;
	
	public BottomPanel(){
		
		setPreferredSize(new Dimension(400,40));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		progressValue = 0;
		
		btnExit = new JButton("Exit");

		// setBackground(Color.BLUE);
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(progressValue);

		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
	
	add(progressBar);
	add(Box.createHorizontalGlue());
	add(Box.createRigidArea(new Dimension(50,40)));
	add(Box.createHorizontalGlue());
	add(btnExit);
	}
	
	public void setProgressValues(int min, int max){
		progressBar.setMinimum(min);
		progressBar.setMaximum(max);
		progressBar.setStringPainted(true);
		progressValue = 0;
	}
	
	public void increaseProgress(){
		progressValue++;
		progressBar.setValue(progressValue);
		progressBar.repaint();
	}
}
