package se.jolind.jtvtracker.data;

import java.time.ZonedDateTime;

public class Episode {

	private int number, id;
	private String epDate, recap, url, timeZone, name;
	private String[] imgArray;
	private AirTime time;

	public Episode(int id, String name, int number, String recap, String url, String[] imgArray, String epDate, String timeZone, String zClock){
		this.name = name;
		this.id = id;
		this.number = number;
		this.recap = recap;
		this.epDate = epDate;
		this.url = url;
		this.imgArray = imgArray;
		this.timeZone = timeZone;
		time = new AirTime(zClock, epDate, timeZone);
	}

	public int getNumber() {
		return number;
	}

	public String getRecap() {
		return recap;
	}
	
	public String getImgMedium() {
		return imgArray[0];
	}
	
	public String getImgFull() {
		return imgArray[1];
	}

	public String getLocalTime(){
		return time.getLocalTimeAsString();
	}
	
	public String getLocalDate(){
		return time.getLocalDateAsString();
	}
	
	public String getZonedTime(){
		return time.getZonedTimeAsString();
	}

	public String getZonedDate(){
		return epDate;
	}
	
	@Override
	public String toString() {
		return "Namn: " + name + "\nNummer "+ number + "\nSändes:\n " + time.getZonedDateAsString() + " " + time.getZonedTimeAsString() + " " + timeZone + 
				"\nLokal tid: " + time.getLocalTimeAsString() + " " + time.getLocalDateAsString() + "\nSummering: " + recap + "\nUrl: " + url + "\nId: " + id
				+ "\nBild urler: " + getImgMedium() + " " + getImgFull();
	}
	
}
