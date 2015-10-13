package se.jolind.jtvtracker.data;

import java.util.ArrayList;
import java.util.List;

/*
 *  Show class. 
 *  ------------------------
 *  Saves show data and has references to the season class associated 
 *  with the current show.
 *  
 */
public class Show {

	private String name, url, officialUrl;
	private int id, currSeason, latestEpisode, nextEpisode;
	private List <Season> seasons;
	private boolean activeShow;
	
	public Show() {
		seasons = new ArrayList<Season>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOfficialUrl() {
		return officialUrl;
	}

	public void setOfficialUrl(String officialUrl) {
		this.officialUrl = officialUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCurrSeason() {
		return currSeason;
	}

	public void setCurrSeason(int currSeason) {
		this.currSeason = currSeason;
	}

	public int getLatestEpisode() {
		return latestEpisode;
	}

	public void setLatestEpisode(int latestEpisode) {
		this.latestEpisode = latestEpisode;
	}

	public int getNextEpisode() {
		return nextEpisode;
	}

	public void setNextEpisode(int nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	public List<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public boolean isActiveShow() {
		return activeShow;
	}

	public void setActiveShow(boolean activeShow) {
		this.activeShow = activeShow;
	}
	
	
}
