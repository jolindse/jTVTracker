package se.jolind.jtvtracker.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Season {

	private int seasonNumber, numberOfEpisodes;
	private LocalDate seasonStartDate;
	private List<Episode> episodeList;
	
	public Season (){
		
	
	seasonNumber = 0;
	numberOfEpisodes = 0;
	seasonStartDate.now();
	episodeList = new ArrayList<>();
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

	public void setSeasonNumber(int seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public int getNumberOfEpisodes() {
		return numberOfEpisodes;
	}

	public void setNumberOfEpisodes(int numberOfEpisodes) {
		this.numberOfEpisodes = numberOfEpisodes;
	}

	public LocalDate getSeasonStartDate() {
		return seasonStartDate;
	}

	public void setSeasonStartDate(LocalDate seasonStartDate) {
		this.seasonStartDate = seasonStartDate;
	}

	public List<Episode> getEpisodeList() {
		return episodeList;
	}

	public void setEpisodeList(List<Episode> episodeList) {
		this.episodeList = episodeList;
	}
	
	
}
