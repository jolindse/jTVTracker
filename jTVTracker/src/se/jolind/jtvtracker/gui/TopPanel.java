package se.jolind.jtvtracker.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import se.jolind.jtvtracker.application.Application;
import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class TopPanel extends JPanel {

	private JLabel lblShowName;
	private JLabel lblShowPremYear;
	private JLabel lblLogo;
	private JComboBox cmbSeasons;
	private JComboBox cmbEpisodes;

	private InfoFormat currInfo;
	private IShowChange infoListener;

	public TopPanel() {
		setPreferredSize(new Dimension(400, 60));
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		infoListener = Application.getListener();

		lblShowName = new JLabel(" ");
		lblShowName.setFont(new Font("Serif", Font.BOLD, 20));
		lblLogo = new JLabel("JTvTracker");
		lblShowPremYear = new JLabel(" ");

		cmbSeasons = new JComboBox<String>();
		cmbSeasons.setVisible(false);

		cmbEpisodes = new JComboBox<String>();
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
		currInfo = infoListener.getInformation();

		lblShowName.setText(currInfo.getShowName());
		lblShowPremYear.setText(makeYears());
	}

	private String makeYears() {
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
		cmbSeasons.removeAllItems();
		cmbSeasons.addItem(currInfo.populateSeasons());
		cmbSeasons.setVisible(true);
		cmbSeasons.setSelectedIndex(0);

		updateEpisode();
		cmbSeasons.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				seasonBoxChanged(cmbSeasons.getSelectedIndex() + 1);
			}
		});

	}

	private void updateEpisode() {
		cmbEpisodes.removeAllItems();
		cmbEpisodes.addItem(currInfo.populateEpisodes());
		cmbEpisodes.setVisible(true);
		cmbEpisodes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				episodeBoxChanged(cmbEpisodes.getSelectedIndex() + 1);

			}
		});

	}

	private void seasonBoxChanged(int number) {
		cmbEpisodes.removeAllItems();
		infoListener.seasonChangedEvent(number);
		updateEpisode();

	}

	private void episodeBoxChanged(int number) {
		infoListener.episodeChangedEvent(number);
	}

}
