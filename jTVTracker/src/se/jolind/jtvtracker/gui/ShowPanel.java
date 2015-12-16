package se.jolind.jtvtracker.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.jolind.jtvtracker.application.Application;
import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.gui.interfaces.IFavoriteEvent;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;
/*
 * The show panel
 */

public class ShowPanel extends JPanel {

	private JComboBox cmbxSeason;
	private JLabel lblShowInfo, lblShowRecap, lblShowPic;
	private JCheckBox chbxFavorite;
	private GridBagConstraints gc;
	private InfoFormat currInfo;
	private IShowChange infoListener;
	private IFavoriteEvent favListener;
	
	public ShowPanel() {

		// Set layout
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();

		infoListener = Controller.getListener();
		favListener = Controller.getListener();
		
		// Init components
		lblShowInfo = new JLabel(" ");
		lblShowInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblShowRecap = new JLabel(" ");
		chbxFavorite = new JCheckBox("Favorite", false);
		chbxFavorite.setFocusable(false);
		chbxFavorite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chbxFavorite.isSelected()){
					favListener.favoriteShowEvent();
				}
				if (!chbxFavorite.isSelected()){
					favListener.unfavoriteShowEvent();
				}
				
			}
		});

		// First row
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.gridwidth = 1;
		gc.insets = new Insets(10, 10, 10, 10);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(lblShowInfo, gc);
		
		// Second row
		gc.gridx = 0;
		gc.gridy = 2;
		gc.weightx = 0.9;
		gc.weighty = 1;
		gc.gridwidth = 2;
		gc.fill = GridBagConstraints.BOTH;
		add(lblShowRecap, gc);			

	}
	
	public void updateInfo(){
		/*
		 * The update panel method used from controller via view to 
		 * update information on panel.
		 */

		if (infoListener == null){
			infoListener = Controller.getListener();
		}
		
		currInfo = infoListener.getInformation();
		
		// Picture
		lblShowPic = new JLabel();
		lblShowPic.setIcon(currInfo.getShowImage());
		gc.gridx = 1;
		gc.gridy = 0;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.gridwidth = 1;
		add(lblShowPic, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		add(chbxFavorite, gc);
		
		// ShowInfo and summary
		lblShowInfo.setText(currInfo.getShowInfo());
		lblShowRecap.setText(currInfo.getShowSummary());
		chbxFavorite.setSelected(currInfo.isFavorite());
	}
}
