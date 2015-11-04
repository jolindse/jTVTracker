package se.jolind.jtvtracker.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.jolind.jtvtracker.data.Episode;
import se.jolind.jtvtracker.data.Show;

public class EpisodePanel extends JPanel {
	
	private JLabel lblEpInfo, lblEpRecap, lblEpPic;
	private Show currShow;
	private int seasonNumber, epNumber;
	private GridBagConstraints gc;
	
	public EpisodePanel(){
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();
		
	
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

	public void setCurrentEp(Show currShow, int seasonNumber, int epNumber){
		this.currShow = currShow;
		this.seasonNumber = seasonNumber;
		this.epNumber = epNumber;
	}
	
	public void updateInfo(){
		if (seasonNumber == 0){
			seasonNumber = 1;
		}
		if (epNumber == 0){
			epNumber = 1;
		}
		Episode chEp = currShow.getEpisode(seasonNumber, epNumber);
		ImageIcon currEpImage = new ImageIcon(chEp.getMediumImg());
		
		
		lblEpPic.setIcon(currEpImage);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.gridwidth = 1;
		add(lblEpPic, gc);
		
		lblEpInfo.setText(chEp.getBasicInfo());
		lblEpRecap.setText(chEp.getRecap());
	}
}
