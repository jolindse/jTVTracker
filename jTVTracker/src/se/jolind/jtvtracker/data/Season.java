package se.jolind.jtvtracker.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Season {

	private int seasonNumber, numberOfEpisodes;
	private List<Episode> episodeList;

	public Season() {
		seasonNumber = 0;
		numberOfEpisodes = 0;
		episodeList = new ArrayList<>();
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

	public int getNumberOfEpisodes() {
		return episodeList.size();
	}

	public void addEpisode(Episode currEp) {
		if (!episodeList.contains(currEp)) {
			numberOfEpisodes++;
			episodeList.add(currEp);
		}
	}

	public void setNumberOfEpisodes(int numberOfEpisodes) {
		this.numberOfEpisodes = numberOfEpisodes;
	}

	/*
	 * public void setSeasonStartDate(LocalDate seasonStartDate) {
	 * this.seasonStartDate = seasonStartDate; }
	 */

	public List<Episode> getEpisodeList() {
		return episodeList;
	}

}
