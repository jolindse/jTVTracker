package se.jolind.jtvtracker.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Season {

	private int seasonNumber, numberOfEpisodes;
	private Map<Integer, Episode> episodeList;

	public Season() {
		seasonNumber = 0;
		numberOfEpisodes = 0;
		episodeList = new HashMap<>();
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

	public Episode getEpisode(int number) {
		return episodeList.get(number);
	}

	public int getNumberOfEpisodes() {
		return episodeList.size();
	}

	public void addEpisode(Episode currEp) {
		episodeList.put(currEp.getNumber(), currEp);
	}

	public String getDuration() {
		// Calculate duration of season.
		return "";
	}

	public void setNumberOfEpisodes() {
		numberOfEpisodes = episodeList.size();
	}

	public void setSeasonNumber(int number) {
		seasonNumber = number;
	}

	public void printSeasonEps() {
		System.out.println(episodeList);
		for (int i = 1; i <= numberOfEpisodes; i++) {
			System.out.println(episodeList.get(i));
		}
	}

	public Map<Integer, Episode> getEpisodeList() {
		return episodeList;
	}

}
