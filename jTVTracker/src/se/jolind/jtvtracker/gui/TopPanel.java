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

import se.jolind.jtvtracker.data.Show;

public class TopPanel extends JPanel {

	private MainFrame view;
	private JLabel lblShowName;
	private JLabel lblShowPremYear;
	private JLabel lblLogo;
	private JComboBox cmbSeasons;
	private JComboBox cmbEpisodes;

	public TopPanel(MainFrame view) {
		setPreferredSize(new Dimension(400, 60));
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		this.view = view;

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

	public void setShowNameFull(String name, String year, String endYear, int numberOfSeasons) {
		lblShowName.setText(name);
		lblShowPremYear.setText("(" + year + endYear + ")");

		updateSeasons(numberOfSeasons);

	}

	public void setShowName(String name, String year, String endYear) {
		lblShowName.setText(name);
		if (year.equals("No i")) {
			lblShowPremYear.setText("No information");
		} else {
			lblShowPremYear.setText("(" + year + endYear + ")");
		}
		cmbSeasons.setVisible(false);
		cmbEpisodes.setVisible(false);
	}

	private void updateSeasons(int numberOfSeasons) {

		cmbSeasons.removeAllItems();

		for (int i = 1; i <= numberOfSeasons; i++) {
			cmbSeasons.addItem("Season " + i);
		}

		cmbSeasons.setVisible(true);
		cmbSeasons.setSelectedIndex(0);
		updateEpisode(1);
		cmbSeasons.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				seasonBoxChanged(cmbSeasons.getSelectedIndex() + 1);
			}
		});

		cmbEpisodes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				episodeBoxChanged(cmbEpisodes.getSelectedIndex() + 1);

			}
		});
	}

	private void updateEpisode(int currSeason) {
		cmbEpisodes.removeAllItems();
		Show currShow = view.getShow();
		int numberOfEpisodes = currShow.getNumberOfEps(currSeason);

		for (int i = 1; i <= numberOfEpisodes; i++) {
			cmbEpisodes.addItem(i + " " + currShow.getEpisodeName(currSeason, i));
		}
		cmbEpisodes.setVisible(true);
	}

	private void seasonBoxChanged(int chSeason) {
		if (chSeason == 0) {
			chSeason = 1;
		}
		cmbEpisodes.removeAllItems();
		updateEpisode(chSeason);
		view.setSeason(chSeason);
	}

	private void episodeBoxChanged(int chEp) {
		if (chEp == 0){
		 chEp = 1;
		} else {
		view.setEp(chEp);
		}
	}

}
