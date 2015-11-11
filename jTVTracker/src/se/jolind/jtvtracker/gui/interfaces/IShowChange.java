package se.jolind.jtvtracker.gui.interfaces;

import se.jolind.jtvtracker.data.InfoFormat;

public interface IShowChange {
	/*
	 * Interface to controll and return information about selected show.
	 */

	public void showChangedEvent(int id);
	public void seasonChangedEvent(int seasonNumber);
	public void episodeChangedEvent(int episodeNumber);
	public InfoFormat getInformation();
}
