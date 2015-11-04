package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.data.tvmaze.TvmShortShow;

public class MainFrame extends JFrame {

	private Show currShow;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;
	private ShowPanel showPanel;
	private EpisodePanel episodePanel;
	private SearchPanel searchPanel;
	private JTabbedPane contentPane;
	private static Controller controller;

	private int currSeason, currEp;

	public MainFrame() {
		super("jTVTracker v0.01a");
		this.setSize(new Dimension(600, 750));
		this.setLayout(new BorderLayout());

		// Set native platform look And feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		topPanel = new TopPanel(this);
		bottomPanel = new BottomPanel();
		searchPanel = new SearchPanel();

		contentPane = new JTabbedPane(JTabbedPane.TOP);

		createTabs();

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(contentPane, BorderLayout.CENTER);

		this.setVisible(true);

	}

	// TABS MANAGMENT

	private void createTabs() {
		contentPane.addTab("Search", searchPanel);
		if (currShow != null) {
			showPanel = new ShowPanel();
			contentPane.addTab("Series", showPanel);
			if (currShow.isSeasons())
				episodePanel = new EpisodePanel();
			contentPane.addTab("Episode", episodePanel);
		}
	}

	public void setShow(Show currShow) {
		this.currShow = currShow;
		contentPane.removeAll();
		createTabs();
		setInfo();
		contentPane.setSelectedIndex(1);
	}

	public void setEp(int currEp) {
		this.currEp = currEp;
		episodePanel.setCurrentEp(currShow, currSeason, currEp);
		episodePanel.updateInfo();
		contentPane.setSelectedIndex(2);
	}

	public void setSeason(int currSeason) {
		this.currSeason = currSeason;
		currEp = 1;
	}

	public Show getShow() {
		return currShow;
	}

	public void newSearch(List<TvmShortShow> currResults) {
		searchPanel = new SearchPanel(currResults);
		contentPane.removeTabAt(0);
		contentPane.insertTab("Search", null, searchPanel, "", 0);
		contentPane.setSelectedIndex(0);
	}

	private void setInfo() {
		String endyear = "";

		if (currShow.isActiveShow()) {
			endyear = "-";
		} else {
			endyear = "-" + currShow.getEndYear();
		}

		if (currShow.isSeasons()) {
			topPanel.setShowNameFull(currShow.getName(), currShow.getPremYear(), endyear, currShow.getNumberSeasons());
		} else {
			topPanel.setShowName(currShow.getName(), currShow.getPremYear(), endyear);
		}
		showPanel.setCurrShow(currShow);
		showPanel.updateInfo();
	}

	public void setListener(Controller controller) {
		this.controller = controller;
	}

	public static Controller getListener() {
		return controller;
	}
}
