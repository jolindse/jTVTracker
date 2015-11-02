package se.jolind.jtvtracker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import se.jolind.jtvtracker.data.Show;

public class MainFrame extends JFrame {
	
	private Show currShow;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;
	private ShowPanel seasonPanel;
	private EpisodePanel episodePanel;
	
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
		
		seasonPanel = new ShowPanel();
		episodePanel = new EpisodePanel();
		
		topPanel = new TopPanel(this);
		bottomPanel = new BottomPanel();
		JTabbedPane contentPane = new JTabbedPane(JTabbedPane.TOP);
		
		contentPane.addTab("Serie", seasonPanel);
		contentPane.addTab("Avsnitt", episodePanel);
		
		
		add(topPanel,BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(contentPane, BorderLayout.CENTER);
		
		
		this.setVisible(true);
		
	}
	
	public void setShow(Show currShow){
		this.currShow = currShow;
		setInfo();
	}
	
	public void setEp(int currEp){
		this.currEp = currEp;
		episodePanel.setCurrentEp(currShow, currSeason, currEp);
		episodePanel.updateInfo();
	}
	
	public void setSeason(int currSeason){
		this.currSeason = currSeason;
		currEp=1;
	}
	
	public Show getShow(){
		return currShow;
	}
	
	
	private void setInfo(){
		String endyear = "";
		if (currShow.isActiveShow()){
			endyear = "-";
		} else {
			endyear = "-" + currShow.getEndYear();
		}
		topPanel.setShowName(currShow.getName(), currShow.getPremYear(), endyear, currShow.getNumberSeasons());
		seasonPanel.setCurrShow(currShow);
		seasonPanel.updateInfo();
	}
}
