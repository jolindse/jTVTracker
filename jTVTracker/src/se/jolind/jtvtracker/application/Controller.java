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

/*
 *  The controller class wich acts as listener for all interfaces
 */

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
		/*
		 * Gets reference to the controller from the application.
		 */
		listener = handler;
	}
	
	public static Controller getListener(){
		/*
		 * Give all classes the possibility to get a reference
		 * to the controller to act as a listener.
		 */
		return listener;
	}

	private void makeShow(int showId) {
		/*
		 * Internal class to make a Show instance.
		 */
		currShow = new Show(showId);
	}
	
	private void setSeasonNumber(int number){
		/*
		 * Sets the current season number
		 */
		this.seasonNumber = number;
	}
	
	private void setEpisodeNumber(int number){
		/*
		 * Sets the current episode number
		 */
		this.episodeNumber = number;
	}

	// IShowChange INTERFACE METHODS
	
	@Override
	public void showChangedEvent(int showId) {
		/*
		 * The method to make a new show and feed it to the gui.
		 * The process is spawned to a own thread to swingworker to 
		 * be able to update the progressbar in realtime and make the
		 * gui responsive while operation is being completed.
		 */
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
					view.newShow();
				}
				
			}
			
		};
		worker.execute();
	}

	@Override
	public void episodeChangedEvent(int episodeNumber) {
		/*
		 *  Method to change episode and update gui accordingly.
		 */
		if (currInfo.hasSeasons()){
				setEpisodeNumber(episodeNumber);
				currInfo.setEpisode(seasonNumber, episodeNumber);
				view.updateView();
			}
	}

	@Override
	public void seasonChangedEvent(int seasonNumber) {
		/*
		 *  Method to change season and update gui accordingly.
		 */
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
		/*
		 * Getter for the gui information class based on current show, season and episode.
		 */
		return currInfo;
	}
	
	// ISearchRequest INTERFACE METHODS
	
	@Override
	public void searchRequest(String searchString) {
		/*
		 * Method to perform a search and feed results to gui. Executed in 
		 * own thread to allow progressbar updates.
		 */
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
			
			@Override
			protected Void doInBackground() throws Exception {
				TvmSearch currSearch = new TvmSearch(searchString);
				view.newSearch(currSearch.getList());
				return null;
			}
		
			@Override
			public void done(){
				// NO ACTION
				}
				
			
			
		};
		worker.execute();
	}
	
	// IProgress INTERFACE METHODS
	
	

	@Override
	public void initProgressBar(int startValue, int endValue) {
		/*
		 * Method to init progressbar and feed the values.
		 */
		view.initProgressBar(startValue, endValue);
	}

	@Override
	public void increaseProgressBar() {
		/*
		 * Method to increase the progress on progressbar.
		 */
		view.increaseProgressBar();
		
	}
	
}
