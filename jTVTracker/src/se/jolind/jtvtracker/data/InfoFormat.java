package se.jolind.jtvtracker.data;

import javax.swing.ImageIcon;

public class InfoFormat {

	// Information variables
	private String shName, shSummary, genres, language, network, shStatus, runtime;
	private String epName, epSummary;
	private int numberOfSeasons;
	private String timeZone, epZonedTime, epLocalTime, shZonedTime, shLocalTime, days, localdays, premYear, endYear;
	private ImageIcon shImage, epImage;
	
	boolean activeShow, episodes;

	// Internal variables
	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	private int showId, epNumber, seasonNumber;
	
	private Show currShow;
	private Episode currEp;
	private Season currSeason;
	
	
	public InfoFormat(int showId, Show currShow){
		this.currShow = currShow;
		this.showId = showId;
		makeShowInfo();
	}
	
	// METHODS USED FROM CONTROLLER
	
	public boolean setEpisode(int season, int episode){
		boolean operationOk = false;
		seasonNumber = season;
		epNumber = episode;
		currEp = currShow.getEpisode(season, episode);
		makeEpisodeInfo();
		return operationOk;
	}
	
	// INTERNAL METHODS
	
	private void makeShowInfo(){
		activeShow = currShow.isActiveShow();
		episodes = currShow.isSeasons();
		
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
		
		// Time info
		premYear = currShow.getPremYear();
		endYear = currShow.getEndYear();
		shZonedTime = currShow.getOrigTime();
		shLocalTime = currShow.getLocalTime();
		timeZone = currShow.getTimeZone();
	}
	
	private void makeEpisodeInfo(){
		// Basic info
		epName = currEp.getName();
		epSummary = currEp.getSummary();
		
		epImage = currEp.getMediumImg();
		
		// Time info
		epZonedTime = currEp.getZonedTime() + " " + currEp.getZonedDate();
		epLocalTime = currEp.getLocalTime() + " " + currEp.getLocalDate();
	}
	
	// SHOW GETTERS
	
	public boolean isActiveShow(){
		return activeShow;
	}
	
	public boolean hasSeasons(){
		return episodes;
	}
		
	public String getShowName(){
		return shName;
	}
	
	public ImageIcon getShowImage(){
		return shImage;
	}
	
	public String getShowSummary(){
		return shSummary;
	}
	
	public String getStartYear(){
		return premYear;
	}
	
	public String getEndYear(){
		return endYear;
	}
	
	// EPISODE GETTERS
	
	public int getEpisodeNumber(){
		return epNumber;
	}
	
	public ImageIcon getEpisodeImage(){
		return epImage;
	}
	
	public String getEpisodeSummary(){
		return epSummary;
	}
	
	// FORMATTING METHODS
	
	public String getShowInfo() {
		String lblReturn = "<HTML>" + BOLD + ITALIC + genres + ENDBOLD + ENDITALIC + NEWLINE + NEWLINE + "Network:"
				+ NEWLINE + BOLD + network + ENDBOLD + NEWLINE + NEWLINE + "Language: " + NEWLINE + BOLD
				+ language + ENDBOLD + NEWLINE + "Status:" + NEWLINE + BOLD + shStatus + ENDBOLD + NEWLINE
				+ "Air times:" + NEWLINE + BOLD + shZonedTime + ENDBOLD + " (" + timeZone + ")" + NEWLINE + BOLD
				+ shLocalTime + ENDBOLD + " (Local time)" + NEWLINE + NEWLINE + "Runtime:" + NEWLINE + BOLD
				+ runtime + " minutes" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	
	}
	
	public String getEpisodeInfo() {
		String lblReturn = "<HTML>" + BOLD + epName + ENDBOLD + NEWLINE + NEWLINE + "Aired: " + NEWLINE + BOLD
				+ epZonedTime + timeZone + ENDBOLD + NEWLINE 
				+ BOLD + epLocalTime + " (Local time)" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	}
	
	public String[] populateSeasons() {
		if (numberOfSeasons != 0){
			String[] seasonsArray = new String[numberOfSeasons];
			for (int i = 1; i <= numberOfSeasons; i++){
				seasonsArray[i-1] = "Season " + i;	
			}
			return seasonsArray;
		} 
		String[] emptyArray = { "Empty" };
		return emptyArray;
	}
	
	public String[] populateEpisodes() {
		
		int numberOfEpisodes = currShow.getNumberOfEps(seasonNumber);
		String[] episodeArray = new String[numberOfEpisodes];
		for (int i = 1; i <= numberOfEpisodes; i++){
			String episodeName = currShow.getEpisodeName(seasonNumber, i);
			if (episodeName.equals("")){
				episodeName = "No information";
			}
			episodeArray[i-1] = String.format("%2d %s", i, episodeName);
		}
		if (episodeArray.length > 0){
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
