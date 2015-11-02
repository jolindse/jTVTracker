package se.jolind.jtvtracker.data.tvmaze;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * 
 * Wrapper class for show information from tvmaze.com
 * --------------------------------------------------
 * Parses json results using google gson library. 
 * 
 */

public class TvmShow {

	private String showBaseUrl = "http://api.tvmaze.com/shows/";
	private JsonObject rootObject;
	private Gson converter;
	private boolean isWebSeries = false;

	public TvmShow(int id) throws IOException {
		String sURL = showBaseUrl + Integer.toString(id);
		URL url = new URL(sURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		rootObject = root.getAsJsonObject();
		// Check if its a web series
		if (rootObject.get("network").isJsonNull()) {
			isWebSeries = true;
		}
	}

	// INFORMATION EXTRACTION METHODS

	public int getId() {
		return rootObject.get("id").getAsInt();
	}

	public int getRuntime() {
		return rootObject.get("runtime").getAsInt();
	}

	public String getName() {
		return rootObject.get("name").getAsString();
	}

	public String getType() {
		return rootObject.get("type").getAsString();
	}

	public String getLang() {
		return rootObject.get("language").getAsString();
	}

	public String getSummary() {
		return rootObject.get("summary").getAsString();
	}

	public String getPremiereDate() {
		return rootObject.get("premiered").getAsString();
	}

	public String getPreEpUrl() {
		JsonObject links = rootObject.getAsJsonObject("_links");
		JsonObject previous = links.getAsJsonObject("previousepisode");
		return previous.get("href").getAsString();
	}

	public String getNextEpUrl() {
		JsonObject links = rootObject.getAsJsonObject("_links");
		String strReturn = "No information";
		try {
			JsonObject previous = links.getAsJsonObject("nextepisode");
			strReturn = previous.get("href").getAsString();
		} catch (NullPointerException e) {
		}
		return strReturn;
	}

	public String getGenres() {
		String strReturn = "";
		JsonArray jsGenre = rootObject.getAsJsonArray("genres");
		String[] genreList = convJsonArray(jsGenre);
		if (genreList.length > 1) {
			for (String currGen : genreList) {
				strReturn += currGen + ", ";
			}
			strReturn = strReturn.substring(0, strReturn.length() - 2);
		}
		if (genreList.length == 1) {
			strReturn = genreList[0];
		}
		if (genreList.length == 0) {
			strReturn = "None";
		}
		return strReturn;
	}

	public boolean getStatus() {
		boolean runningShow = false;
		String showString = rootObject.get("status").getAsString();
		if (showString.equalsIgnoreCase("Running")) {
			runningShow = true;
		}
		return runningShow;
	}

	public String[] getScheduleDays() {
		JsonObject jsSchedule = rootObject.getAsJsonObject("schedule");
		JsonArray jsDays = jsSchedule.getAsJsonArray("days");
		String[] days = convJsonArray(jsDays);
		return days;
	}

	public String getScheduleTime() {
		String airTime = "";
		JsonObject jsSchedule = rootObject.getAsJsonObject("schedule");
		airTime = jsSchedule.get("time").getAsString();
		return airTime;
	}

	public String getTimeZone() {
		String timeZone = "";
		if (isWebSeries) {
			JsonObject network = rootObject.getAsJsonObject("webChannel");
			JsonObject localInfo = network.getAsJsonObject("country");
			timeZone = localInfo.get("timezone").getAsString();
		} else {
			JsonObject network = rootObject.getAsJsonObject("network");
			JsonObject localInfo = network.getAsJsonObject("country");
			timeZone = localInfo.get("timezone").getAsString();
		}
		return timeZone;
	}

	public String getNetwork() {
		String networkName = "";
		if (isWebSeries) {
			JsonObject webChannel = rootObject.getAsJsonObject("webChannel");
			networkName = webChannel.get("name").getAsString();
		} else {
			JsonObject network = rootObject.getAsJsonObject("network");
			networkName = network.get("name").getAsString();
		}
		return networkName;
	}

	public String[] getImageUrl() {
		String[] imageArray = new String[2];
		if (rootObject.get("image").isJsonNull()) {
			imageArray[0] = "resources/noImage.png";
			imageArray[1] = "resources/noImage.png";
		} else {
			JsonObject image = rootObject.getAsJsonObject("image");
			imageArray[0] = image.get("medium").getAsString();
			imageArray[1] = image.get("original").getAsString();
		}
		return imageArray;
	}

	public int getUpdatedTime() {
		int updateTime = rootObject.get("updated").getAsInt();
		return updateTime;
	}

	public int[] getAllEpId() throws IOException {
		/*
		 * Method to get all episode IDs for a series and return them as a
		 * String-array. Gets a new Json from tvmaze wich contains all episode
		 * info.
		 */

		String currId = Integer.toString(getId());
		String epURL = "http://api.tvmaze.com/shows/" + currId + "/episodes";
		URL epUrl = new URL(epURL);
		HttpURLConnection request;

		request = (HttpURLConnection) epUrl.openConnection();
		request.connect();
		JsonParser parser = new JsonParser();
		JsonArray epRoot = parser.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray();

		int[] episodeArray = new int[epRoot.size()];

		for (int i = 0; i < epRoot.size(); i++) {
			JsonObject currEp = epRoot.get(i).getAsJsonObject();
			JsonObject links = currEp.getAsJsonObject("_links");
			JsonObject currUrl = links.getAsJsonObject("self");
			episodeArray[i] = Integer.parseInt(getEpisodeId((currUrl.get("href").getAsString())));
		}
		return episodeArray;

	}

	public int getNumberOfSeasons() {
		TvmEpisode latestEp;
		int number = -1;
		try {
			latestEp = new TvmEpisode(Integer.parseInt(getEpisodeId(getPreEpUrl())));
			number = latestEp.getSeason();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Fel vid läsning i GetShow");
			e.printStackTrace();
		}
		return number;
	}

	// INTERNAL METHODS

	private String getEpisodeId(String epUrl) {
		return epUrl.substring(31);

	}

	private String[] convJsonArray(JsonArray currJArray) {
		String[] currArray = new String[currJArray.size()];
		converter = new Gson();
		currArray = converter.fromJson(currJArray, String[].class);
		return currArray;
	}
}
