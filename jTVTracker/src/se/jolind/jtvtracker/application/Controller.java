package se.jolind.jtvtracker.application;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import se.jolind.jtvtracker.data.InfoFormat;
import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.data.tvmaze.TvmSearch;
import se.jolind.jtvtracker.gui.MainFrame;
import se.jolind.jtvtracker.gui.interfaces.IProgress;
import se.jolind.jtvtracker.gui.interfaces.ISearchRequest;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class Controller implements IShowChange, ISearchRequest, IProgress {

	private static Controller listener;
	private MainFrame view;
	private int id, seasonNumber, episodeNumber;
	private Show currShow;
	private InfoFormat currInfo;
	
	public Controller(MainFrame view) {
		this.view = view;
	}
	
	public void createListnerHandler(Controller handler){
		listener = handler;
	}
	
	public static Controller getListener(){
		return listener;
	}

	private void makeShow(int showId) {
		currShow = new Show(showId);
	}
	
	private void setSeasonNumber(int number){
		this.seasonNumber = number;
	}
	
	private void setEpisodeNumber(int number){
		this.episodeNumber = number;
	}

	// IShowChange interface methods
	
	@Override
	public void showChangedEvent(int showId) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				makeShow(showId);
				return null;
			}
		
			@Override
			public void done(){
				id = showId;
				currInfo = new InfoFormat(id, currShow);
				if (currInfo.hasSeasons()){
					setSeasonNumber(1);
					setEpisodeNumber(1);
					currInfo.setEpisode(seasonNumber, episodeNumber);
					view.updateInfo();
				}
				
			}
			
		};
		worker.execute();
	}

	@Override
	public void episodeChangedEvent(int episodeNumber) {
		if (currInfo.hasSeasons()){
				setEpisodeNumber(episodeNumber);
				currInfo.setEpisode(seasonNumber, episodeNumber);
				view.updateView();
			}
	}

	@Override
	public void seasonChangedEvent(int seasonNumber) {
		if (currInfo.hasSeasons()){
			if (seasonNumber < 1){
				setSeasonNumber(1);
			}
			setSeasonNumber(seasonNumber);
			currInfo.setEpisode(seasonNumber, 1);
		}
		view.updateView();
	}
	
	@Override
	public InfoFormat getInformation() {
		return currInfo;
	}
	
	// ISearchRequest interface methods
	
	@Override
	public void searchRequest(String searchString) {
		TvmSearch currSearch = new TvmSearch(searchString);
		view.newSearch(currSearch.getList());
	}
	
	// IProgress interface methods

	@Override
	public void initProgressBar(int startValue, int endValue) {
		view.initProgressBar(startValue, endValue);
	}

	@Override
	public void increaseProgressBar() {
		view.increaseProgressBar();
		
	}
	
}
