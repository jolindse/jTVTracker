package se.jolind.jtvtracker.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * Class to store information about seasons
 */
public class Season {

	private int seasonNumber, numberOfEpisodes;
	private Map<Integer, Episode> episodeList;

	public Season() {
		seasonNumber = 0;
		numberOfEpisodes = 0;
		episodeList = new HashMap<>();
	}

	public int getSeasonNumber() {
		/*
		 * Returns the number of the season
		 */
		return seasonNumber;
	}

	public Episode getEpisode(int number) {
		/*
		 * Returns a specific episode of the season
		 */
		return episodeList.get(number);
	}

	public int getNumberOfEpisodes() {
		/*
		 * Returns the total number of episodes in the season
		 */
		return episodeList.size();
	}

	public void addEpisode(Episode currEp) {
		/*
		 * Adds a episode to the season
		 */
		episodeList.put(currEp.getNumber(), currEp);
	}

	public void setNumberOfEpisodes() {
		/*
		 * Sets the number of episodes
		 */
		numberOfEpisodes = episodeList.size();
	}

	public void setSeasonNumber(int number) {
		/*
		 * Sets the season number
		 */
		seasonNumber = number;
	}

	public void printSeasonEps() {
		/*
		 * Prints all episodes of the season to console
		 */
		System.out.println(episodeList);
		for (int i = 1; i <= numberOfEpisodes; i++) {
			System.out.println(episodeList.get(i));
		}
	}

	public Map<Integer, Episode> getEpisodeList() {
		/*
		 * Get the season map
		 */
		return episodeList;
	}

}
