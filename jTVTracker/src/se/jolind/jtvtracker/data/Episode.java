package se.jolind.jtvtracker.data;

import java.time.ZonedDateTime;

public class Episode {

	private int number;
	private String epDate, recap, url;
	private String[] imgArray;
	
	public Episode(int number, String recap, String url, String epDate){
		this.number = number;
		this.recap = recap;
		this.epDate = epDate;
		this.url = url;
	}
	
	public Episode(int number, String recap, String url, String epDate, String[] imgArray){
		this.number = number;
		this.recap = recap;
		this.epDate = epDate;
		this.url = url;
		this.imgArray = imgArray;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRecap() {
		return recap;
	}

	public void setRecap(String recap) {
		this.recap = recap;
	}
/*
	public ZonedDateTime getEpDate() {
		return epDate;
	}

	public void setEpDate(ZonedDateTime epDate) {
		this.epDate = epDate;
	}
*/

	@Override
	public String toString() {
		return "Nummer "+ number + "\nSändes: " + epDate + "\nSummering: " + recap + "\nUrl: " + url + "\n";
	}
	
}
