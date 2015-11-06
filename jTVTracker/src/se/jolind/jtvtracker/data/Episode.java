package se.jolind.jtvtracker.data;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

import javax.imageio.ImageIO;

public class Episode {

	private int number, id;
	private String epDate, recap, url, timeZone, name;
	private String[] imgArray;
	private boolean timeInfo;
	private AirTime time;

	private String NEWLINE = "<BR>";
	private String BOLD = "<B>";
	private String ENDBOLD = "</B>";
	private String ITALIC = "<I>";
	private String ENDITALIC = "</I>";

	public Episode(int id, String name, int number, String recap, String url, String[] imgArray, String epDate,
			String timeZone, String zClock, boolean makeAirTime) {
		this.name = name;
		this.id = id;
		this.number = number;
		this.recap = recap;
		this.epDate = epDate;
		this.url = url;
		this.imgArray = imgArray;
		this.timeZone = timeZone;
		timeInfo = makeAirTime;
		if (timeInfo) {
			time = new AirTime(zClock, epDate, timeZone);
		}
	}
	
	public Episode(){
		this.id = 0;
		this.name = "No information";
		this.number = 1;
		this.recap = "No information.";
		this.epDate = "0000-00-00";
		this.url = "No information";
		this.imgArray = new String[] { "resources/noImage.png","resources/noImage.png" };
		this.timeZone = "No information";
		timeInfo = false;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getRecap() {
		return "<HTML>" + recap + "</HTML>";
	}

	public String getImgMediumUrl() {
		return imgArray[0];
	}

	public String getImgFullUrl() {
		return imgArray[1];
	}

	public String getEndYear() {
		if (timeInfo) {
			return time.getYear();
		}
		return epDate.substring(0, 4);
	}

	public String getLocalTime() {
		if (timeInfo) {
			return time.getLocalTimeAsString();
		}
		return "No information";
	}

	public String getLocalDate() {
		if (timeInfo) {
			return time.getLocalDateAsString();
		}
		return "No information";
	}

	public String getZonedTime() {
		if (timeInfo) {
			return time.getZonedTimeAsString();
		}
		return "No information";
	}

	public String getZonedDate() {
		return epDate;
	}

	public AirTime getAirTime() {
		return time;
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

	public String getBasicInfo() {
		String lblReturn = "<HTML>" + BOLD + getName() + ENDBOLD + NEWLINE + NEWLINE + "Aired: " + NEWLINE + BOLD
				+ epDate + " " + getZonedTime() + " " + timeZone + ENDBOLD + NEWLINE + BOLD + getLocalDate() + " "
				+ getLocalTime() + " (Local time)" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	}

	@Override
	public String toString() {
		return "Namn: " + name + "\nNummer " + number + "\nSändes:\n " + time.getZonedDateAsString() + " "
				+ time.getZonedTimeAsString() + " " + timeZone + "\nLokal tid: " + time.getLocalTimeAsString() + " "
				+ time.getLocalDateAsString() + "\nSummering: " + recap + "\nUrl: " + url + "\nId: " + id
				+ "\nBild urler: " + getImgMediumUrl() + " " + getImgFullUrl();
	}

}
