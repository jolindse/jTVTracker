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

import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.data.tvmaze.TvmEpisode;
import se.jolind.jtvtracker.data.tvmaze.TvmShow;
import se.jolind.jtvtracker.gui.interfaces.IProgress;

/*
 *  The show class
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
	private boolean activeShow, hasSeasons, hasTime, hasNext, favoriteShow; 
	private IProgress progressListener;
	
	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	public Show(int id) {

		progressListener = Controller.getListener();

		currShow = new TvmShow(id);

		// Set boolean checks
		hasSeasons = checkSeasons();
		hasTime = currShow.getTimeInfo();
		activeShow = currShow.getStatus();
		hasNext = checkNext();
		
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
	
	// STATUS BOOLEANS

	public boolean isSeasons(){
		/*
		 * Returns true if a show has season information
		 */
		return hasSeasons;
	}

	public boolean isActiveShow() {
		/*
		 * Returns true if a show is active and not canceled
		 */
		return activeShow;
	}

	public boolean hasNextEpisode() {
		/*
		 * Returns true if the show contains information on the next episode to be aired.
		 */
		return hasNext;
	}
	
	public void setFavoriteShow(boolean value){
		/*
		 * Sets the favorite boolean
		 */
		this.favoriteShow = value;
	}
	
	// SHOW INFORMATION

	public String getName() {
		/*
		 * Returns the show name
		 */
		return name;
	}

	public String getPremYear() {
		/*
		 * Returns the premiere year of the show
		 */
		if (hasTime){
		return premTime.getYear();
		}
		return date.substring(0,4);	}

	public String getLang() {
		/*
		 * Returns the language of the show
		 */
		return lang;
	}
	
	public String getStatusString(){
		/*
		 * Returns the status of the show as string
		 */
		return status;
	}

	public String getUrl() {
		/*
		 * Returns the URL of the show
		 */
		return url;
	}

	public String getGenres() {
		/*
		 * Returns the genres 
		 */
		return genres;
	}

	public String getSummary() {
		/*
		 * Returns the show summary
		 */
		return "<HTML>" + summary + "</HTML>";
	}

	public int getId() {
		/*
		 * Returns the show id
		 */
		return id;
	}

	public String getRuntime() {
		/*
		 * Returns the runtime of the show
		 */
		return runtime;
	}

	public String getNetwork() {
		/*
		 * Returns the shows airing network
		 */
		return network;
	}

	public int getNumberSeasons() {
		/*
		 * Returns the number of seasons of the show
		 */
		return numberSeasons;
	}
	
	public ImageIcon getMediumImg() {
		/*
		 * Returns the show image
		 */
		return showImg;
	}
	
	public Episode getNextEp() {
		/*
		 * Returns the next episode
		 */
		return currShow.getNextEp();
	}
	
	public boolean checkNext() {
		/*
		 * Checks if the show has a next episode information
		 */
		boolean nextEpisode = true; 
		if (currShow.getNextEpUrl().equals("No information")){
			nextEpisode = false;
		}
		return nextEpisode;
	}

	// TIME GETTERS
	
	public String getTimeZone() {
		/*
		 * Returns the timezone of the show
		 */
		if(hasTime){
		return timeZone;
		}
		return "No information";
	}


	public String getOrigTime() {
		/*
		 * Returns the original time of show
		 */
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
		/*
		 * Returns the local time of the show
		 */
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
		/*
		 * Returns the endyear of the show
		 */
		if (hasSeasons){
		int lastSeason = getNumberSeasons();
		int lastEpisode = getNumberOfEps(lastSeason);
		Episode lastEp = currentEpisode(lastSeason, lastEpisode);
		return lastEp.getEndYear();
		}
		return "No information";
	}

	private ImageIcon createIcon() {
		/*
		 * Makes an ImageIcon from an image
		 */
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
				try {
					mediumImg = ImageIO.read(new File("resources/noImage.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		ImageIcon icon = new ImageIcon(mediumImg);
		return icon;
	}

	// SHOW SEASON INFORMATION GETTERS
	
	public int getNumberOfEps(int seNumber) {
		/*
		 * Returns the number of episodes in a season 
		 */
		Season currSeason = seasons.get(seNumber);
		return currSeason.getNumberOfEpisodes();
	}
/*
	public String getBasicInfo() {
		String lblReturn = "<HTML>" + BOLD + ITALIC + getGenres() + ENDBOLD + ENDITALIC + NEWLINE + NEWLINE + "Network:"
				+ NEWLINE + BOLD + getNetwork() + ENDBOLD + NEWLINE + NEWLINE + "Language: " + NEWLINE + BOLD
				+ getLang() + ENDBOLD + NEWLINE + "Status:" + NEWLINE + BOLD + getStatusString() + ENDBOLD + NEWLINE
				+ "Air times:" + NEWLINE + BOLD + getOrigTime() + ENDBOLD + " (" + getTimeZone() + ")" + NEWLINE + BOLD
				+ getLocalTime() + ENDBOLD + " (Local time)" + NEWLINE + NEWLINE + "Runtime:" + NEWLINE + BOLD
				+ getRuntime() + " minutes" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	}
	*/
	
	// SHOW EPISODE INFORMATION GETTERS

	public Episode getEpisode(int seNumber, int epNumber) {
		/*
		 * Returns the selected episode
		 */
		return currentEpisode(seNumber, epNumber);
	}

	public String getEpisodeName(int seNumber, int epNumber) {
		/*
		 * Returns the name of the selected episode
		 */
		Season chSeason = seasons.get(seNumber);
		Episode chEp = chSeason.getEpisode(epNumber);
		return chEp.getName();
	}

	public String printEpisode(int seasonNumber, int currEpisode) {
		/*
		 * Returns the selected episode for output to console
		 */
		Episode currEp = currentEpisode(seasonNumber, currEpisode);
		return currEp.toString();
	}

	// INTERNAL METHODS

	private String getImgMediumUrl() {
		/*
		 * Returns the medium img url
		 */
		return imgArray[0];
	}

	private String getImgFullUrl() {
		/*
		 * Returns the full img url
		 */
		return imgArray[1];
	}

	private Episode currentEpisode(int seNumber, int epNumber) {
		/*
		 * Returns the selected episode
		 */
		Season choosenSeason = seasons.get(seNumber);
		if (epNumber < 1){
			epNumber = 1;
		}
		Episode choosenEpisode = choosenSeason.getEpisode(epNumber);
		return choosenEpisode;
	}

	private boolean checkSeasons() {
		/*
		 * Checks if show has seasons
		 */
		boolean seasonsNotNull = false;
		if (currShow.getNumberOfSeasons() > 0) {
			seasonsNotNull = true;
		}
		return seasonsNotNull;
	}

	private Map<Integer, Season> makeSeasons(int[] allEps) throws IOException {
		/*
		 * Makes the seasons of the show
		 */
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
				// System.out.println("Dummy säsong på g. Säsong: "+i);
				Episode dummyEp = new Episode();
				Season dummySeason = new Season();
				dummySeason.addEpisode(dummyEp);
				seasonMap.put(i, dummySeason);
			}
		}
		
		return seasonMap;
	}

	private Episode makeEpisode(int epId, TvmEpisode currEp) {
		/*
		 * Makes an episode
		 */
		Episode ep = new Episode(epId, currEp.getName(), currEp.getNumber(), currEp.getSummary(), currEp.getsURL(),
				currEp.getImgUrl(), currEp.getAirDate(), timeZone, time, hasTime);
		return ep;

	}
	
	
	
	private void scanSeasons() {
		/*
		 * Makes all seasons
		 */
		try {
			seasons = makeSeasons(currShow.getAllEpId());
			// seasonsScanned = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
