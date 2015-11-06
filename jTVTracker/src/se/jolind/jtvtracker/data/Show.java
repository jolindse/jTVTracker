package se.jolind.jtvtracker.data;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import se.jolind.jtvtracker.data.tvmaze.TvmEpisode;
import se.jolind.jtvtracker.data.tvmaze.TvmShow;
import se.jolind.jtvtracker.gui.MainFrame;
import se.jolind.jtvtracker.gui.interfaces.IProgress;

/*
 *  Show class. 
 *  ------------------------
 *  Saves show data and has references to the season class associated 
 *  with the current show.
 *  
 */

public class Show {

	private TvmShow currShow;
	private String name, lang, url, genres, network, summary, runtime, status;
	private String time, date, timeZone;
	private String[] days, imgArray;
	private int id, numberSeasons, latestEpisode, nextEpisode, updated;
	private Map<Integer, Season> seasons;
	private AirTime premTime;
	private ImageIcon showImg;
	private boolean activeShow, hasSeasons, hasTime; // seasonsScanned,
	private IProgress progressListener;
	
	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	public Show(int id) {

		progressListener = MainFrame.getListener();

		currShow = new TvmShow(id);

		// Set boolean checks
		hasSeasons = checkSeasons();
		hasTime = currShow.getTimeInfo();
		activeShow = currShow.getStatus();
		
		// Internal values
		time = currShow.getScheduleTime();
		date = currShow.getPremiereDate();
		timeZone = currShow.getTimeZone();
		
		
		
		if (hasTime){
		premTime = new AirTime(time, date, timeZone);
		}
		
		imgArray = currShow.getImageUrl();
		
		
		// External values
		id = currShow.getId();
		name = currShow.getName();
		lang = currShow.getLang();
		genres = currShow.getGenres();
		updated = currShow.getUpdated();
		runtime = currShow.getRuntime();
		network = currShow.getNetwork();
		days = currShow.getScheduleDays();
		summary = currShow.getSummary();
		status = currShow.getStatusString();
		showImg = createIcon();
		

		scanSeasons();
		
	}
	
	/*
	private void scanSeasons() {
		try {
			seasons = makeSeasons(currShow.getAllEpId());
			// seasonsScanned = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return seasonsScanned;
	}
	*/
	
	// STATUS BOOLEANS

	public boolean isSeasons(){
		return hasSeasons;
	}

	public boolean isActiveShow() {
		return activeShow;
	}
	
	// SHOW INFORMATION

	public String getName() {
		return name;
	}

	public String getPremYear() {
		if (hasTime){
		return premTime.getYear();
		}
		return date.substring(0,4);	}

	public String getLang() {
		return lang;
	}
	
	public String getStatusString(){
		return status;
	}

	public String getUrl() {
		return url;
	}

	public String getGenres() {
		return genres;
	}

	public String getSummary() {
		return "<HTML>" + summary + "</HTML>";
	}

	public int getId() {
		return id;
	}

	public String getRuntime() {
		return runtime;
	}

	public String getNetwork() {
		return network;
	}

	public int getNumberSeasons() {
		return numberSeasons;
	}
	
	public ImageIcon getMediumImg() {
		return showImg;
	}

	// TIME GETTERS
	
	public String getTimeZone() {
		if(hasTime){
		return timeZone;
		}
		return "No information";
	}


	public String getOrigTime() {
		if (hasTime){
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
		return "No information";
	}

	public String getLocalTime() {
		if (hasTime && hasSeasons){
		int latestSeason = getNumberSeasons();
		int latestEpisode = getNumberOfEps(latestSeason);
		Episode currEp = getEpisode(latestSeason, latestEpisode);
		AirTime currTime = currEp.getAirTime();
		String day = currTime.getLocalDay() + " " + currTime.getLocalTimeAsString();
		return day;
		}
		return "No information";
	}
	
	public String getEndYear() {
		if (hasSeasons){
		int lastSeason = getNumberSeasons();
		int lastEpisode = getNumberOfEps(lastSeason);
		Episode lastEp = currentEpisode(lastSeason, lastEpisode);
		return lastEp.getEndYear();
		}
		return "No information";
	}

	private ImageIcon createIcon() {
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
		ImageIcon icon = new ImageIcon(mediumImg);
		return icon;
	}

	// PRINT METHOD FOR TESTING
	
	public void printShow() {
		for (int i = 1; i < numberSeasons; i++) {
			Season currSeason = seasons.get(i);
			currSeason.printSeasonEps();
		}
	}

	// SHOW SEASON INFORMATION GETTERS
	
	public int getNumberOfEps(int seNumber) {
		Season currSeason = seasons.get(seNumber);
		return currSeason.getNumberOfEpisodes();
	}

	public String getBasicInfo() {
		String lblReturn = "<HTML>" + BOLD + ITALIC + getGenres() + ENDBOLD + ENDITALIC + NEWLINE + NEWLINE + "Network:"
				+ NEWLINE + BOLD + getNetwork() + ENDBOLD + NEWLINE + NEWLINE + "Language: " + NEWLINE + BOLD
				+ getLang() + ENDBOLD + NEWLINE + "Status:" + NEWLINE + BOLD + getStatusString() + ENDBOLD + NEWLINE
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
		if (epNumber < 1){
			epNumber = 1;
		}
		Episode choosenEpisode = choosenSeason.getEpisode(epNumber);
		return choosenEpisode;
	}

	private boolean checkSeasons() {
		boolean seasonsNotNull = false;
		if (currShow.getNumberOfSeasons() > 0) {
			seasonsNotNull = true;
		}
		return seasonsNotNull;
	}

	private Map<Integer, Season> makeSeasons(int[] allEps) throws IOException {
		
		progressListener.initProgressBar(0, allEps.length);
		
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
			progressListener.increaseProgressBar();
		}
		seasonMap.put(currSeasonNumber, currSeason);
		numberSeasons = currSeasonNumber;

		/*
		 *  Check for empty season information and fill with dummys.
		 */
		for (int i = 1; i <= numberSeasons; i++){
			if (!seasonMap.containsKey(i)){
				System.out.println("Dummy säsong på g. Säsong: "+i);
				Episode dummyEp = new Episode();
				Season dummySeason = new Season();
				dummySeason.addEpisode(dummyEp);
				seasonMap.put(i, dummySeason);
			}
		}
		
		return seasonMap;
	}

	private Episode makeEpisode(int epId, TvmEpisode currEp) {
		Episode ep = new Episode(epId, currEp.getName(), currEp.getNumber(), currEp.getSummary(), currEp.getsURL(),
				currEp.getImgUrl(), currEp.getAirDate(), timeZone, time, hasTime);
		return ep;

	}
	
	
	
	private void scanSeasons() {
		try {
			seasons = makeSeasons(currShow.getAllEpId());
			// seasonsScanned = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return seasonsScanned;
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
