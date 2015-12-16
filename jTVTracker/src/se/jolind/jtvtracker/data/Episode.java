package se.jolind.jtvtracker.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * Class to store episode information
 */

public class Episode {

	private int number, id;
	private String epDate, recap, url, timeZone, name;
	private String[] imgArray;
	private ImageIcon episodeImg;
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
		episodeImg = makeMediumImg();
	}

	public Episode(int id, String name, int number, String recap) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.recap = recap;
		this.url = "http://api.tvmaze.com/episodes/" + id;
		}

	public Episode() {
		// Dummy episode constructor used when no information is available
		// except the existance of
		// an episode.
		this.id = 0;
		this.name = "No information";
		this.number = 1;
		this.recap = "No information.";
		this.epDate = "0000-00-00";
		this.url = "No information";
		this.imgArray = new String[] { "resources/noImage.png", "resources/noImage.png" };
		this.timeZone = "No information";
		timeInfo = false;
	}

	public int getId() {
		return id;
	}

	public int getNumber() {
		/*
		 * Returns the number of the episode.
		 */
		return number;
	}

	public String getName() {
		/*
		 * Returns the name of the episode.
		 */
		return name;
	}

	public String getSummary() {
		/*
		 * Returns the episode summary.
		 */
		return "<HTML>" + recap + "</HTML>";
	}

	public String getImgMediumUrl() {
		/*
		 * Returns the URL of the medium sized image
		 */
		return imgArray[0];
	}

	public String getImgFullUrl() {
		/*
		 * Returns the URL of the full sized image
		 */
		return imgArray[1];
	}

	public String getEndYear() {
		/*
		 * Returns the end year of the show
		 */

		if (timeInfo) {
			return time.getYear();
		}
		return epDate.substring(0, 4);
	}

	public String getLocalTime() {
		/*
		 * Returns the local time as string
		 */
		if (timeInfo) {
			return time.getLocalTimeAsString();
		}
		return "No information";
	}

	public String getLocalDate() {
		/*
		 * Returns the local date as string
		 */
		if (timeInfo) {
			return time.getLocalDateAsString();
		}
		return "No information";
	}

	public String getZonedTime() {
		/*
		 * Returns the original time as string
		 */
		if (timeInfo) {
			return time.getZonedTimeAsString();
		}
		return "No information";
	}

	public String getZonedDate() {
		/*
		 * Returns the original date as string
		 */
		return epDate;
	}

	public AirTime getAirTime() {
		/*
		 * Returns the AirTime object of the episode
		 */
		return time;
	}

	public void setAirTime(AirTime currAirTime) {
		timeInfo = true;
		time = currAirTime;
		epDate = time.getZonedDateAsString();
	}

	public ImageIcon getMediumImg() {
		return episodeImg;
	}

	public BufferedImage getBufferedImage() {
		Image currImg = episodeImg.getImage();
		BufferedImage bufferedImg = (BufferedImage) currImg;
		return bufferedImg;
	}

	public void setImage(String imgPath) {
		BufferedImage mediumImg = null;
		try {
			mediumImg = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		episodeImg = new ImageIcon(mediumImg);
	}

	private ImageIcon makeMediumImg() {
		/*
		 * Returns a ImageIcon of the medium image of the episode
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
				try {
					mediumImg = ImageIO.read(new File("resources/noImage.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return new ImageIcon(mediumImg);
	}

	public String getBasicInfo() {
		/*
		 * Returns a HTML formatted string of information about the episode.
		 */
		String lblReturn = "<HTML>" + BOLD + getName() + ENDBOLD + NEWLINE + NEWLINE + "Aired: " + NEWLINE + BOLD
				+ epDate + " " + getZonedTime() + " " + timeZone + ENDBOLD + NEWLINE + BOLD + getLocalDate() + " "
				+ getLocalTime() + " (Local time)" + ENDBOLD + NEWLINE + "</HTML>";
		return lblReturn;
	}

	@Override
	public String toString() {

		return "(" + id + ",'" + getName().replaceAll("'", "") + "'," + getNumber() + ",'"
				+ getSummary().replaceAll("'", "") + "'," + getAirTime().getLongInstant() + ")";
	}

}
