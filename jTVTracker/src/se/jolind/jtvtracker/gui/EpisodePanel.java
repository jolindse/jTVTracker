package se.jolind.jtvtracker.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.jolind.jtvtracker.application.Application;
import se.jolind.jtvtracker.data.Episode;
import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class EpisodePanel extends JPanel {
	
	private JLabel lblEpInfo, lblEpRecap, lblEpPic;
	private InfoFormat currInfo;
	private int seasonNumber, epNumber;
	private GridBagConstraints gc;
	private IShowChange infoListener;
	
	
	public EpisodePanel(){
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		
		infoListener = Application.getListener();
	
		lblEpInfo = new JLabel(" ");
		lblEpInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblEpRecap = new JLabel(" Summary ");
		lblEpPic = new JLabel("");

		
		// First row
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridwidth = 1;
		gc.insets = new Insets(10, 10, 10, 10);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(lblEpInfo, gc);
		
		// Second row
		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 0.9;
		gc.weighty = 1;
		gc.gridwidth = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(lblEpRecap, gc);
	}
	
	public void updateInfo(){
		
		currInfo = infoListener.getInformation();
		lblEpPic.setIcon(currInfo.getEpisodeImage());
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.gridwidth = 1;
		add(lblEpPic, gc);
		
		lblEpInfo.setText(currInfo.getEpisodeInfo());
		lblEpRecap.setText(currInfo.getEpisodeSummary());
	}
}
