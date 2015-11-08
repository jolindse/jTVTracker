package se.jolind.jtvtracker.gui.interfaces;

import se.jolind.jtvtracker.data.InfoFormat;

public interface IShowChange {

	public void showChangedEvent(int id);
	public void seasonChangedEvent(int seasonNumber);
	public void episodeChangedEvent(int episodeNumber);
	public InfoFormat getInformation();
}
