package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import se.jolind.jtvtracker.data.tvmaze.GetEpisode;
import se.jolind.jtvtracker.data.tvmaze.GetShow;

/*
 *  Show class. 
 *  ------------------------
 *  Saves show data and has references to the season class associated 
 *  with the current show.
 *  
 */

public class Show {

	private GetShow currShow;
	private String name, lang, url, genres, timeZone, time, date, network;
	private String[] imgArray;
	private int id, runtime, numberSeasons, latestEpisode, nextEpisode, updated;
	private Map<Integer, Season> seasons;
	private AirTime premTime;
	private boolean activeShow, seasonsScanned;

	public Show(int id) {

		try {
			currShow = new GetShow(id);
			numberSeasons = Integer.parseInt(currShow.getNumberOfSeasons());
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// External values
		name = currShow.getName();
		lang = currShow.getLang();
		genres = currShow.getGenres();
		imgArray = currShow.getImageUrl();
		id = currShow.getId();
		runtime = currShow.getRuntime();
		network = currShow.getNetwork();
		activeShow = currShow.getStatus();
		// Internal values
		time = currShow.getScheduleTime();
		date = currShow.getPremiereDate();
		timeZone = currShow.getTimeZone();
		premTime = new AirTime(time, date, timeZone);
		updated = currShow.getUpdatedTime();
		seasonsScanned = false;
	}

	public void printShow() {
		if (seasonsScanned) {
			for (int i = 1; i < numberSeasons; i++) {
				Season currSeason = seasons.get(i);
				currSeason.printSeasonEps();
			}
		} else {
			System.out.println("Säsongerna är inte scannade.");
		}
	}

	public boolean scanSeasons() {
		try {
			seasons = makeSeasons(currShow.getAllEpId());
			seasonsScanned = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seasonsScanned;
	}

	// GETTER & SETTERS
	/*
	 * public GetShow getCurrShow() { return currShow; }
	 * 
	 * public Map<Integer, Season> getSeasons() { return seasons; }
	 * 
	 */

	// SHOW INFORMATION

	public String getName() {
		return name;
	}

	public String getLang() {
		return lang;
	}

	public String getUrl() {
		return url;
	}

	public String getGenres() {
		return genres;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getImgMedium() {
		return imgArray[0];
	}

	public String getImgFull() {
		return imgArray[1];
	}

	public int getId() {
		return id;
	}

	public int getRuntime() {
		return runtime;
	}

	public String getNetwork() {
		return network;
	}
	
	public int getNumberSeasons() {
		return numberSeasons;
	}

	public int getLatestEpisode() {
		return latestEpisode;
	}

	public int getNextEpisode() {
		return nextEpisode;
	}

	public boolean isActiveShow() {
		return activeShow;
	}

	public boolean isSeasonsScanned() {
		return seasonsScanned;
	}
	
	// SHOW SEASON INFORMATION GETTERS
	
	public int getNumberOfEps(int seNumber){
		// Get number of episodes in a season;
		return 0;
	}
	
	public String[] getSeasonEpsNames(int seNumber){
		String[] episodeNames = { " ", " " };
		// Get all the names of the episodes in a season
		return episodeNames;
	}

	// SHOW EPISODE INFORMATION GETTERS
	
	public Episode getEpisode(int seNumber, int epNumber){
		return currentEpisode(seNumber, epNumber);
	}
	
	public String getEpisodeName(int seNumber, int epNumber) {
		// Get name of episode, takes season and episode number
		return "";
	}

	public String getEpisodeUrl(int seNumber, int epNumber) {
		// Get episode URL, takes season and episode number
		return "";
	}

	public String getEpisodeImg(int seNumber, int epNumber) {
		// Get episode Img url.
		return "";
	}

	public String printEpisode(int seasonNumber, int currEpisode) {
		Episode currEp = currentEpisode(seasonNumber, currEpisode);
		return currEp.toString();
	}

	// INTERNAL METHODS
	
	private Episode currentEpisode(int seNumber, int epNumber){
		Season choosenSeason = seasons.get(seNumber);
		Episode choosenEpisode = choosenSeason.getEpisode(epNumber);
		return choosenEpisode;
	}
	
	private Map<Integer, Season> makeSeasons(int[] allEps) throws IOException {

		Map<Integer, Season> seasonMap = new HashMap<>();
		Season currSeason = new Season();
		int currSeasonNumber = 1;

		for (int currId : allEps) {
			GetEpisode currEp = new GetEpisode(currId);
			int episodeSeason = currEp.getSeason();
			if (episodeSeason == currSeasonNumber) {
				currSeason.addEpisode(makeEpisode(currId, currEp));
				currSeason.setSeasonNumber(currSeasonNumber);
			} else {
				currSeason.setNumberOfEpisodes();
				seasonMap.put(currSeasonNumber, currSeason);
				currSeasonNumber = currEp.getSeason();
				currSeason = new Season();	
				currSeason.addEpisode(makeEpisode(currId, currEp));
				currSeason.setSeasonNumber(currSeasonNumber);
			}
		}
		return seasonMap;
	}
	
	private Episode makeEpisode(int epId, GetEpisode currEp){
		Episode ep = new Episode(epId, currEp.getName(), currEp.getNumber(), currEp.getSummary(), currEp.getsURL(), currEp.getImgUrl(),
				currEp.getAirDate(), timeZone, time);
		return ep;

	}
}
