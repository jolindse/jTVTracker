package se.jolind.jtvtracker.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

/*
 * The top panel
 */

public class TopPanel extends JPanel {

	private JLabel lblShowName;
	private JLabel lblShowPremYear;
	private JLabel lblLogo;
	private GridBagConstraints gc;
	private JComboBox<String> cmbSeasons;
	private JComboBox<String> cmbEpisodes;

	private InfoFormat currInfo;
	private IShowChange infoListener;

	public TopPanel() {
		setPreferredSize(new Dimension(400, 60));
		setLayout(new GridBagLayout());
		gc = new GridBagConstraints();

		infoListener = Controller.getListener();

		lblShowName = new JLabel(" ");
		lblShowName.setFont(new Font("Serif", Font.BOLD, 20));
		lblLogo = new JLabel("JTvTracker");
		lblShowPremYear = new JLabel(" ");

		cmbSeasons = new JComboBox<>();
		cmbEpisodes = new JComboBox<>();
		cmbSeasons.setVisible(false);
		cmbEpisodes.setVisible(false);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 10, 0, 10);
		gc.anchor = GridBagConstraints.WEST;
		add(lblShowName, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.anchor = GridBagConstraints.EAST;
		add(cmbSeasons, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 10, 0, 10);
		gc.anchor = GridBagConstraints.WEST;
		add(lblShowPremYear, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 10, 0, 10);
		gc.anchor = GridBagConstraints.EAST;
		add(cmbEpisodes, gc);

		}

	public void updateInfo() {
		/*
		 * Method used to update info called from controller via view.
		 */
		if (infoListener == null) {
			infoListener = Controller.getListener();
		}

		currInfo = infoListener.getInformation();

		lblShowName.setText(currInfo.getShowName());
		lblShowPremYear.setText(makeYears());
		if (currInfo.hasSeasons()) {
			updateSeasons();
			updateEpisode();
		}
	}

	private String makeYears() {
		/*
		 * Formats and displays the show year information.
		 */
		String startYear = currInfo.getStartYear();
		String endYear = currInfo.getEndYear();

		if (currInfo.isActiveShow()) {
			return "(" + startYear + "-)";
		}

		if (startYear.equals("No information")) {
			return startYear;
		}

		return "(" + startYear + "-" + endYear + ")";
	}

	private void updateSeasons() {
		/*
		 * Internal method to update the seasons combobox according to 
		 * current show set in controller.
		 */
		currInfo = infoListener.getInformation();
		
		cmbSeasons.setModel(new DefaultComboBoxModel<>(currInfo.populateSeasons()));
		
		cmbSeasons.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				seasonBoxChanged(cmbSeasons.getSelectedIndex());
				
			}
		});
		cmbSeasons.setVisible(true);
		updateEpisode();
	}

	private void updateEpisode() {
		/*
		 * Method to update episode combobox according to show set in controller.
		 */
		currInfo = infoListener.getInformation();
		cmbEpisodes.setModel(new DefaultComboBoxModel<>(currInfo.populateEpisodes()));

		cmbEpisodes.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				episodeBoxChanged(cmbEpisodes.getSelectedIndex());
			}
		});
		
		cmbEpisodes.setVisible(true);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weightx = 0.1;
		gc.weighty = 0.1;
		gc.insets = new Insets(0, 10, 0, 10);
		gc.anchor = GridBagConstraints.EAST;
		add(cmbEpisodes, gc);
	}

	private void seasonBoxChanged(int number) {
		/*
		 * Method called by the combobox listener to set season in controller.
		 */
		if (number != 0){
		infoListener.seasonChangedEvent(number+1);
		updateEpisode();
		}
	}

	private void episodeBoxChanged(int number) {
		/*
		 * Method called by the combobox listener to set episode in controller.
		 */
		if (number != 0) {
			infoListener.episodeChangedEvent(number+1);
		}
	}

}
