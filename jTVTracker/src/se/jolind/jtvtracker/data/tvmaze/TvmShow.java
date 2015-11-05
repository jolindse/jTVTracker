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
	private boolean timeInfoOk;
	private boolean isWebSeries = false;

	public TvmShow(int id) {
		String sURL = showBaseUrl + Integer.toString(id);
		URL url;
		HttpURLConnection request;
		JsonElement root;
		try {
			url = new URL(sURL);
			request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			rootObject = root.getAsJsonObject();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Check if its a web series
		if (rootObject.get("network").isJsonNull()) {
			isWebSeries = true;
		}
		// Check time info
		timeInfoOk = checkTimeInfo();
	}

	// INFORMATION EXTRACTION METHODS

	public int getId() {
		return rootObject.get("id").getAsInt();
	}

	public String getRuntime() {
		// return rootObject.get("runtime").getAsString();
		//return checkNull(rootObject, "runtime");
		return rootObject.get("runtime").isJsonNull() ? "No information" : rootObject.get("runtime").getAsString();
	}

	public String getName() {
		//return checkNull(rootObject, "name");
		// return rootObject.get("name").getAsString();
		return rootObject.get("name").isJsonNull() ? "No information" : rootObject.get("name").getAsString();
	}

	public String getType() {
		//return checkNull(rootObject, "type");
		// return rootObject.get("type").getAsString();
		return rootObject.get("type").isJsonNull() ? "No information" : rootObject.get("type").getAsString();
	}

	public String getLang() {
		//return checkNull(rootObject, "language");
		// return rootObject.get("language").getAsString();
		return rootObject.get("language").isJsonNull() ? "No information" : rootObject.get("language").getAsString();
	}

	public String getSummary() {
		//return checkNull(rootObject, "summary");
		// return rootObject.get("summary").getAsString();
		return rootObject.get("summary").isJsonNull() ? "No information" : rootObject.get("summary").getAsString();
	}

	public String getStatusString() {
		//return checkNull(rootObject, "status");
		return rootObject.get("status").isJsonNull() ? "No information" : rootObject.get("status").getAsString();
	}
	
	public boolean getTimeInfo() {
		return timeInfoOk;
	}
	
	public String getPreEpUrl() {
		String strReturn = "No information";
		JsonObject links = rootObject.getAsJsonObject("_links");
		try {
			JsonObject previous = links.getAsJsonObject("previousepisode");
			strReturn = previous.get("href").getAsString();
		} catch (NullPointerException e) {
		}
		return strReturn;
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

	public int getUpdated() {
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

		if (!epRoot.isJsonNull()) {

			int[] episodeArray = new int[epRoot.size()];

			for (int i = 0; i < epRoot.size(); i++) {
				JsonObject currEp = epRoot.get(i).getAsJsonObject();
				JsonObject links = currEp.getAsJsonObject("_links");
				JsonObject currUrl = links.getAsJsonObject("self");
				episodeArray[i] = Integer.parseInt(getEpisodeId((currUrl.get("href").getAsString())));
			}
			return episodeArray;
		}
		int[] emptyArray = { -1 };
		return emptyArray;
	}

	public int getNumberOfSeasons() {
		TvmEpisode latestEp;
		int number = 0;
		try {
			String epUrl = getPreEpUrl();
			System.out.println("Previous ep url: "+epUrl); // TEST
			if (!epUrl.equals("No information")){
			latestEp = new TvmEpisode(Integer.parseInt(getEpisodeId(epUrl)));
			number = latestEp.getSeason();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return number;
	}

	// TIME VALUES
	
	private boolean checkTimeInfo(){
		String noInfo = "No information";
		if (getPremiereDate().equals(noInfo) || getScheduleTime().equals(noInfo)) {
			return false;
		}
		return true;
	}
	
	public String getPremiereDate() {
		return rootObject.get("premiered").isJsonNull() ? "No information" : rootObject.get("premiered").getAsString();
		//return checkNull(rootObject, "premiered");
		// return rootObject.get("premiered").getAsString();
	}
	
	public String[] getScheduleDays() {
		JsonObject jsSchedule = rootObject.getAsJsonObject("schedule");
		JsonArray jsDays = jsSchedule.getAsJsonArray("days");
		if (jsDays.size() > 0) {
			String[] days = convJsonArray(jsDays);
			return days;
		}
		String[] emptyArray = { "No information" };
		return emptyArray;
	}

	public String getScheduleTime() {
		
		JsonObject jsSchedule = rootObject.getAsJsonObject("schedule");
		String hour = jsSchedule.get("time").getAsString();
		if (hour.equals(":") || hour.equals("")){
			return "No information";
		}
		return hour;
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
	
	
	// INTERNAL METHODS

	private String checkNull(JsonObject currentObject, String key) {

		if (currentObject.isJsonNull()){
			return "No information";
		}
		return currentObject.get(key).toString();
		//return currentObject.get(key).getAsString();
		/*
		String result = currentObject.get(key).getAsString();
		System.out.println("Key: "+key+" Result: "+result); // TEST
		if (result.equals("") || result.equals("null")) {
			result = "No information";
		}
		return result;
		
		return currentObject.isJsonNull() ? "No information" : currentObject.getAsString();
		 */
	}

	private String getEpisodeId(String epUrl) {
		if (epUrl.length() > 30){
		return epUrl.substring(31);
		}
		return "0";
	}

	private String[] convJsonArray(JsonArray currJArray) {
		String[] currArray = new String[currJArray.size()];
		converter = new Gson();
		currArray = converter.fromJson(currJArray, String[].class);
		return currArray;
	}
}
