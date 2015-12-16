package se.jolind.jtvtracker.data;

import javax.swing.ImageIcon;

public class InfoFormat {

	// Information variables
	private String shName, shSummary, genres, language, network, shStatus, runtime;
	private String epName, epSummary;
	private int numberOfSeasons;
	private String timeZone, epZonedTime, epLocalTime, shZonedTime, shLocalTime, days, localdays, premYear, endYear;
	private ImageIcon shImage, epImage;

	boolean activeShow, episodes, hasNext, favoriteShow;

	// Internal variables
	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	private int showId, epNumber, seasonNumber;

	private Show currShow;
	private Episode currEp, nextEp;
	private Season currSeason;

	public InfoFormat(int showId, Show currShow) {
		this.currShow = currShow;
		this.showId = showId;
		makeShowInfo();
	}

	// METHODS USED FROM CONTROLLER

	public void setEpisode(int season, int episode) {
		/*
		 * sets the current episode and season
		 */
		seasonNumber = season;
		epNumber = episode;
		currEp = currShow.getEpisode(season, episode);
		makeEpisodeInfo();
	}

	// INTERNAL METHODS

	private void makeShowInfo() {
		/*
		 * Populates the information of the current instance.
		 */
		activeShow = currShow.isActiveShow();
		episodes = currShow.isSeasons();
		hasNext = currShow.checkNext();

		// Basic info
		shName = currShow.getName();
		network = currShow.getNetwork();
		shSummary = currShow.getSummary();
		numberOfSeasons = currShow.getNumberSeasons();
		genres = currShow.getGenres();
		shStatus = currShow.getStatusString();
		language = currShow.getLang();
		runtime = currShow.getRuntime();
		shImage = currShow.getMediumImg();
		favoriteShow = currShow.getFavoriteShow();

		// Time info
		premYear = currShow.getPremYear();
		endYear = currShow.getEndYear();
		shZonedTime = currShow.getOrigTime();
		shLocalTime = currShow.getLocalTime();
		timeZone = currShow.getTimeZone();
	}

	private void makeEpisodeInfo() {
		/*
		 * Populates the episode information of the instance.
		 */
		
		// Basic info
		epName = currEp.getName();
		epSummary = currEp.getSummary();

		epImage = currEp.getMediumImg();

		// Time info
		epZonedTime = currEp.getZonedTime() + " " + currEp.getZonedDate();
		epLocalTime = currEp.getLocalTime() + " " + currEp.getLocalDate();
	}

	// SHOW GETTERS

	public boolean isActiveShow() {
		/*
		 * Returns the boolean true if the show is still on air.
		 */
		return activeShow;
	}

	public boolean hasSeasons() {
		/*
		 * Returns the boolean true if the show has seasons and episode information
		 */
		return episodes;
	}
	
	public boolean isFavorite() {
		return favoriteShow;
	}

	public String getShowName() {
		/*
		 * Returns the show name
		 */
		return shName;
	}

	public ImageIcon getShowImage() {
		/*
		 * Returns the ImageIcon of the show
		 */
		return shImage;
	}

	public String getShowSummary() {
		/*
		 * Returns the show summary
		 */
		return shSummary;
	}

	public String getStartYear() {
		/*
		 * Returns the premiere year of the show
		 */
		return premYear;
	}

	public String getEndYear() {
		/*
		 * Returns the end year of the show
		 */
		return endYear;
	}

	// EPISODE GETTERS

	public int getEpisodeNumber() {
		/*
		 * Returns the episode number
		 */
		return epNumber;
	}

	public ImageIcon getEpisodeImage() {
		/*
		 * Returns the ImageIcon of the episode
		 */
		return epImage;
	}

	public String getEpisodeSummary() {
		/*
		 * Returns the episode summary
		 */
		return epSummary;
	}

	private String getNextEpInfo() {
		/*
		 * Gets the HTML formatted information of the next episode.
		 */
		String lblReturn = "Next episode:" + NEWLINE;
		nextEp = currShow.getNextEp();
		lblReturn += nextEp.getNumber() + ". " + BOLD + nextEp.getName() + ENDBOLD + NEWLINE + "Air time:" + NEWLINE
				+ BOLD + nextEp.getZonedDate() + " " + nextEp.getZonedTime() + ENDBOLD + " (" + timeZone + ")" + NEWLINE
				+ BOLD + nextEp.getLocalDate() + " " + nextEp.getLocalTime() + ENDBOLD + " (Local time)";
		return lblReturn;
	}

	// FORMATTING METHODS

	public String getShowInfo() {
		/*
		 * Returns the HTML formatted show information.
		 */
		String lblReturn = "<HTML>" + BOLD + ITALIC + genres + ENDBOLD + ENDITALIC + NEWLINE + NEWLINE + "Network:"
				+ NEWLINE + BOLD + network + ENDBOLD + NEWLINE + NEWLINE + "Language: " + NEWLINE + BOLD + language
				+ ENDBOLD + NEWLINE + "Status:" + NEWLINE + BOLD + shStatus + ENDBOLD + NEWLINE + "Air times:" + NEWLINE
				+ BOLD + shZonedTime + ENDBOLD + " (" + timeZone + ")" + NEWLINE + BOLD + shLocalTime + ENDBOLD
				+ " (Local time)" + NEWLINE + "Runtime:" + NEWLINE + BOLD + runtime + " minutes" + ENDBOLD
				+ NEWLINE + NEWLINE;
		if (hasNext) {
			lblReturn += getNextEpInfo();
		}
		lblReturn += "</HTML>";
		return lblReturn;

	}

	public String getEpisodeInfo() {
		/*
		 * Returns the HTML formatted information of the episode
		 */
		String lblReturn = "<HTML>" + BOLD + epName + ENDBOLD + NEWLINE + NEWLINE + "Aired: " + NEWLINE + BOLD
				+ epZonedTime + " " + timeZone + ENDBOLD + NEWLINE + BOLD + epLocalTime + " (Local time)" + ENDBOLD + NEWLINE
				+ "</HTML>";
		return lblReturn;
	}

	public String[] populateSeasons() {
		/*
		 * Makes and returns a string-array of all seasons in a show
		 */
		if (numberOfSeasons != 0) {
			String[] seasonsArray = new String[numberOfSeasons];
			for (int i = 1; i <= numberOfSeasons; i++) {
				seasonsArray[i - 1] = "Season " + i;
			}
			return seasonsArray;
		}
		String[] emptyArray = { "Empty" };
		return emptyArray;
	}

	public String[] populateEpisodes() {
		/*
		 * Makes and returns a string-array of all episodes of a season.
		 */

		int numberOfEpisodes = currShow.getNumberOfEps(seasonNumber);
		String[] episodeArray = new String[numberOfEpisodes];
		for (int i = 1; i <= numberOfEpisodes; i++) {
			String episodeName = currShow.getEpisodeName(seasonNumber, i);
			if (episodeName.equals("")) {
				episodeName = "No information";
			}
			episodeArray[i - 1] = String.format("%2d %s", i, episodeName);
		}
		if (episodeArray.length > 0) {
			return episodeArray;
		}
		String[] emptyArray = { "Empty" };
		return emptyArray;
	}

	@Override
	public String toString() {
		return "InfoFormat [shName=" + shName + ", shSummary=" + shSummary + ", genres=" + genres + ", language="
				+ language + ", network=" + network + ", shStatus=" + shStatus + ", runtime=" + runtime + ", epName="
				+ epName + ", epSummary=" + epSummary + ", numberOfSeasons=" + numberOfSeasons + ", timeZone="
				+ timeZone + ", epZonedTime=" + epZonedTime + ", epLocalTime=" + epLocalTime + ", shZonedTime="
				+ shZonedTime + ", shLocalTime=" + shLocalTime + ", days=" + days + ", localdays=" + localdays
				+ ", premYear=" + premYear + ", endYear=" + endYear + ", shImage=" + shImage + ", epImage=" + epImage
				+ ", activeShow=" + activeShow + ", episodes=" + episodes + ", showId=" + showId;
	}

}
