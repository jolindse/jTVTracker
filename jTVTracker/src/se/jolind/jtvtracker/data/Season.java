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
	
	public Episode getEpisode(int number){
		return episodeList.get(number-1);
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
	
	public String getDuration(){
		// Calculate duration of season.
		return "";
	}

	public void setNumberOfEpisodes() {
		numberOfEpisodes = episodeList.size();
	}
	
	public void setSeasonNumber(int number){
		seasonNumber = number;
	}
	
	public void printSeasonEps(){
		System.out.println(episodeList);
		for (Episode currEp: episodeList){
			System.out.println(currEp);
		}
	}
	/*
	 * public void setSeasonStartDate(LocalDate seasonStartDate) {
	 * this.seasonStartDate = seasonStartDate; }
	 */

	public List<Episode> getEpisodeList() {
		return episodeList;
	}

}
