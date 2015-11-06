package se.jolind.jtvtracker.data;

public class InfoFormat {

	private String shName, shUrl, shSummary;
	private String epName, epUrl, epSummary;
	private String seasonNr, episodeNr;
	private String timeZone, epZonedTime, epLocalTime, shZonedTime, shLocalTime, days, localdays;
	private int id;
	
	public InfoFormat(int season, int episode){
		
	}
	
	public String getShowName(){
		return shName;
	}
	
	public String getShowUrl(){
		return shUrl;
	}
}
