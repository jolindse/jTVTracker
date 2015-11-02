package se.jolind.jtvtracker.data;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import se.jolind.jtvtracker.data.tvmaze.TvmEpisode;
import se.jolind.jtvtracker.data.tvmaze.TvmShow;
import se.jolind.jtvtracker.data.AirTime;

/*
 *  Show class. 
 *  ------------------------
 *  Saves show data and has references to the season class associated 
 *  with the current show.
 *  
 */

public class Show {

	private TvmShow currShow;
	private String name, lang, url, genres, timeZone, time, date, network, summary;
	private String[] imgArray, days;
	private int id, runtime, numberSeasons, latestEpisode, nextEpisode, updated;
	private Map<Integer, Season> seasons;
	private AirTime premTime;
	private boolean activeShow, seasonsScanned;

	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	public Show(int id) {

		try {
			currShow = new TvmShow(id);
			numberSeasons = currShow.getNumberOfSeasons();
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
		days = currShow.getScheduleDays();
		summary = currShow.getSummary();
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

	// SHOW INFORMATION

	public String getName() {
		return name;
	}

	public String getPremYear() {
		return premTime.getYear();
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

	public String getOrigTime() {
		String day = "";
		if (days.length > 1) {
			for (String currDay : days) {
				day += currDay + " ";
			}
		} else {
			day = days[0] + " ";
		}
		day += currShow.getScheduleTime();
		return day;
	}

	public String getLocalTime() {
		int latestSeason = getNumberSeasons();
		int latestEpisode = getNumberOfEps(latestSeason);
		Episode currEp = getEpisode(latestSeason, latestEpisode);
		AirTime currTime = currEp.getAirTime();
		String day = currTime.getLocalDay() + " " + currTime.getLocalTimeAsString();
		return day;
	}

	public String getSummary() {
		return "<HTML>" + summary + "</HTML>";
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
	
	public boolean isActiveShow() {
		return activeShow;
	}

	public boolean isSeasonsScanned() {
		return seasonsScanned;
	}
	
	public String getEndYear() {
		int lastSeason = getNumberSeasons();
		int lastEpisode = getNumberOfEps(lastSeason);
		Episode lastEp = currentEpisode(lastSeason, lastEpisode);
		return lastEp.getEndYear();
	}

	public Image getMediumImg() {
		URL imgUrl;
		Image mediumImg = null;
		if (getImgMediumUrl().equals("resources/noImage.png")) {
			try {
				mediumImg = ImageIO.read(new File(getImgMediumUrl()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				imgUrl = new URL(getImgMediumUrl());
				mediumImg = ImageIO.read(imgUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mediumImg;
	}

	// SHOW SEASON INFORMATION GETTERS

	public int getNumberOfEps(int seNumber) {
		Season currSeason = seasons.get(seNumber);
		return currSeason.getNumberOfEpisodes();
	}

	public String getBasicInfo() {
		String lblReturn = "<HTML>" + BOLD + ITALIC + getGenres() + ENDBOLD + ENDITALIC + NEWLINE + NEWLINE + "Network:"
				+ NEWLINE + BOLD + getNetwork() + ENDBOLD + NEWLINE + NEWLINE + "Language: " + NEWLINE + BOLD
				+ getLang() + ENDBOLD + NEWLINE + "Status:" + NEWLINE + BOLD + getStatus() + ENDBOLD + NEWLINE
				+ "Air times:" + NEWLINE + BOLD + getOrigTime() + ENDBOLD + " (" + getTimeZone() + ")" + NEWLINE + BOLD
				+ getLocalTime() + ENDBOLD + " (Local time)" + NEWLINE + NEWLINE + "Runtime:" + NEWLINE + BOLD
				+ getRuntime() + " minutes" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	}

	// SHOW EPISODE INFORMATION GETTERS

	public Episode getEpisode(int seNumber, int epNumber) {
		return currentEpisode(seNumber, epNumber);
	}

	public String getEpisodeName(int seNumber, int epNumber) {
		Season chSeason = seasons.get(seNumber);
		Episode chEp = chSeason.getEpisode(epNumber);
		return chEp.getName();
	}

	public String printEpisode(int seasonNumber, int currEpisode) {
		Episode currEp = currentEpisode(seasonNumber, currEpisode);
		return currEp.toString();
	}

	// INTERNAL METHODS

	private String getImgMediumUrl() {
		return imgArray[0];
	}

	private String getImgFullUrl() {
		return imgArray[1];
	}

	private Episode currentEpisode(int seNumber, int epNumber) {
		Season choosenSeason = seasons.get(seNumber);
		Episode choosenEpisode = choosenSeason.getEpisode(epNumber);
		return choosenEpisode;
	}

	private Map<Integer, Season> makeSeasons(int[] allEps) throws IOException {

		Map<Integer, Season> seasonMap = new HashMap<>();
		Season currSeason = new Season();
		int currSeasonNumber = 1;
		for (int currId : allEps) {
			TvmEpisode currEp = new TvmEpisode(currId);
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
		seasonMap.put(currSeasonNumber, currSeason);
		numberSeasons = currSeasonNumber;
		return seasonMap;
	}

	private Episode makeEpisode(int epId, TvmEpisode currEp) {
		Episode ep = new Episode(epId, currEp.getName(), currEp.getNumber(), currEp.getSummary(), currEp.getsURL(),
				currEp.getImgUrl(), currEp.getAirDate(), timeZone, time);
		return ep;

	}

	private String getStatus() {
		String statusStr = "";
		if (activeShow) {
			statusStr = "Running";
		} else {
			statusStr = "Canceled";
		}
		return statusStr;
	}
}
