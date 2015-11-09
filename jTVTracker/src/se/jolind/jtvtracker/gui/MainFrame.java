package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import se.jolind.jtvtracker.application.Application;
import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.data.tvmaze.TvmShortShow;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class MainFrame extends JFrame {

	private InfoFormat currInfo;
	private IShowChange infoListener;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;
	private ShowPanel showPanel;
	private EpisodePanel episodePanel;
	private SearchPanel searchPanel;
	private JTabbedPane contentPane;
	
	private boolean showTab, episodeTab;
	

	public MainFrame() {
		super("jTVTracker v0.01a");
		this.setSize(new Dimension(600, 750));
		this.setLayout(new BorderLayout());
		infoListener = Controller.getListener();
		
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

		topPanel = new TopPanel();
		bottomPanel = new BottomPanel();
		searchPanel = new SearchPanel();
		showPanel = new ShowPanel();
		episodePanel = new EpisodePanel();

		contentPane = new JTabbedPane(JTabbedPane.TOP);
				
		contentPane.addTab("Search", searchPanel);

		showTab = false;
		episodeTab = false;

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(contentPane, BorderLayout.CENTER);

		
		this.setVisible(true);

	}
	
	// Update view
	
	public void updateInfo(){
		
		if (infoListener == null){
			infoListener = Controller.getListener();
		}
		
		currInfo = infoListener.getInformation();
		if (currInfo != null){
			createTabs();
			topPanel.updateInfo();
		}
	}

	// TABS MANAGMENT

	private void createTabs() {
		contentPane.removeAll();
		contentPane.addTab("Search", searchPanel);
		showPanel = new ShowPanel();
		showPanel.updateInfo();
		contentPane.addTab("Series", showPanel);
		showTab = true;
		if (currInfo.hasSeasons()) {
				episodePanel = new EpisodePanel();
				episodePanel.updateInfo();
				contentPane.addTab("Episode", episodePanel);
				episodeTab = true;
			} else {
				episodeTab = false;
			}
	}

	public void updateView() {
				
		if (showTab) {
			showPanel.updateInfo();
			contentPane.setSelectedIndex(1);
		}
		
		if (episodeTab) {
			episodePanel.updateInfo();
			contentPane.setSelectedIndex(2);
			
		}
		
	}
	
	public void newSearch(List<TvmShortShow> currResults) {
		searchPanel = new SearchPanel(currResults);
		contentPane.removeTabAt(0);
		contentPane.insertTab("Search", null, searchPanel, "", 0);
		contentPane.setSelectedIndex(0);
	}
	
	public void initProgressBar(int min, int max){
		bottomPanel.setProgressValues(min, max);
	}
	
	public void increaseProgressBar(){
		bottomPanel.increaseProgress();
	}

}
