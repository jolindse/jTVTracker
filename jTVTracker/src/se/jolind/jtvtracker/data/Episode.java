package se.jolind.jtvtracker.data;

import java.time.ZonedDateTime;

public class Episode {

	private int number;
	private String recap, url;
	private ZonedDateTime epDate;
	
	public Episode(int number, String recap, ZonedDateTime epDate){
		this.number = number;
		this.recap = recap;
		this.epDate = epDate;
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

	public ZonedDateTime getEpDate() {
		return epDate;
	}

	public void setEpDate(ZonedDateTime epDate) {
		this.epDate = epDate;
	}

	
}
