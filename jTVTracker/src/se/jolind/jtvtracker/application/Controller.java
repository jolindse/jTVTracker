package se.jolind.jtvtracker.application;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import se.jolind.jtvtracker.data.Show;
import se.jolind.jtvtracker.data.tvmaze.TvmSearch;
import se.jolind.jtvtracker.gui.MainFrame;
import se.jolind.jtvtracker.gui.interfaces.IProgress;
import se.jolind.jtvtracker.gui.interfaces.ISearchRequest;
import se.jolind.jtvtracker.gui.interfaces.IShowChange;

public class Controller implements IShowChange, ISearchRequest, IProgress {

	private MainFrame view;
	private int id;
	private Show currShow;

	public Controller(MainFrame view) {

		this.view = view;
//		view.setListener(this);
	

	}
/*
	public void requestShow() {
		id = Integer.parseInt(JOptionPane.showInputDialog("ShowID"));
		currShow = new Show(id);
		view.setShow(currShow);
}
*/


	private void makeShow(int showId) {
		currShow = new Show(showId);
	}

	// To be executed when theres a new show chosen.
	
	@Override
	public void ShowChangedEvent(int showId) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				makeShow(showId);
				return null;
			}
		
			@Override
			public void done(){
				view.setShow(currShow);
			}
			
		};
		worker.execute();
		// makeShow(showId);
	}

	// To be executed when theres a search commited
	
	@Override
	public void searchRequest(String searchString) {
		TvmSearch currSearch = new TvmSearch(searchString);
		view.newSearch(currSearch.getList());
	}
	
	// Progress bar methods.

	@Override
	public void initProgressBar(int startValue, int endValue) {
		view.initProgressBar(startValue, endValue);
	}

	@Override
	public void increaseProgressBar() {
		view.increaseProgressBar();
		
	}
}
