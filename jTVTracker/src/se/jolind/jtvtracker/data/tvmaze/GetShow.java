package se.jolind.jtvtracker.data.tvmaze;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/*
 * 
 * Wrapper class for show information from tvmaze.com
 * --------------------------------------------------
 * Parses json results using google gson library. 
 * 
 */


public class GetShow {
	
	private String showBaseUrl = "http://api.tvmaze.com/shows/";
	private JsonObject rootObject;
	private Gson converter;
	

	public GetShow(int id) throws IOException {
		String sURL = showBaseUrl + Integer.toString(id);
		URL url = new URL(sURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); 
		rootObject = root.getAsJsonObject();
	}

	// INFORMATION EXTRACTION METHODS
	
	public int getId(){
		return rootObject.get("id").getAsInt();
	}
	
	public int getRuntime(){
		return rootObject.get("runtime").getAsInt();
	}
	
	public String getName(){
		return rootObject.get("name").getAsString();
	}
	
	public String getType(){
		return rootObject.get("type").getAsString();
	}
	
	public String getLang(){
		return rootObject.get("language").getAsString();
	}

	public String getSummary(){
		return rootObject.get("summary").getAsString();
	}
	
	public String getPreEpUrl(){
		JsonObject links = rootObject.getAsJsonObject("_links");
		JsonObject previous = links.getAsJsonObject("previousepisode");
		return previous.get("href").getAsString();
	}
	
	public String getGenres(){
		String strReturn = "";
		JsonArray jsGenre = rootObject.getAsJsonArray("genres");
		String[] genreList = convJsonArray(jsGenre);
		for (String currGen: genreList){
			strReturn += currGen + ", ";
		}
		return strReturn;
	}
	
	public boolean getStatus(){
		boolean runningShow = false;
		String showString = rootObject.get("status").getAsString();
		if (showString.equalsIgnoreCase("Running")){
			runningShow = true;
		}
		return runningShow;
	}
	
	public String getNextEpUrl(){
		JsonObject links = rootObject.getAsJsonObject("_links");
		String strReturn = "No information";
		try {
			JsonObject previous = links.getAsJsonObject("nextepisode");
			strReturn = previous.get("href").getAsString();
		} catch (NullPointerException e){
		}
		return strReturn;
		}
	
	public String getSchedule(){
		String schedule = "";
		JsonObject jsSchedule = rootObject.getAsJsonObject("schedule");
		JsonArray jsDays = jsSchedule.getAsJsonArray("days");
		String[] days = convJsonArray(jsDays);
		String airTime = jsSchedule.get("time").getAsString();
		schedule += airTime;
		for (String currDay: days){
			schedule += " "+currDay;
		}
		return schedule;
	}
	
	public String[] getImageUrl(){
		String[] imageArray = new String[2];
		JsonObject image = rootObject.getAsJsonObject("image");
		imageArray[0] = image.get("medium").getAsString();
		imageArray[1] = image.get("original").getAsString();
		return imageArray;
	}

	public String[] getAllEpId() throws IOException{
		/*
		 * Method to get all episode IDs for a series and return them as a 
		 * String-array. Gets a new Json from tvmaze wich contains all episode info.
		 */
		
		String currId = Integer.toString(getId());
		String epURL = "http://api.tvmaze.com/shows/"+currId+"/episodes";
		URL epUrl = new URL(epURL);
		HttpURLConnection request = (HttpURLConnection) epUrl.openConnection();
		request.connect();
		JsonParser parser = new JsonParser();
		JsonArray epRoot = parser.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray(); 
		
		String[] episodeArray = new String[epRoot.size()];
		
		for (int i=0; i < epRoot.size(); i++){
			JsonObject currEp = epRoot.get(i).getAsJsonObject();
			JsonObject links = currEp.getAsJsonObject("_links");
			JsonObject currUrl = links.getAsJsonObject("self");
			episodeArray[i] = (currUrl.get("href").getAsString());
		}
		for(String currLid: episodeArray){
			System.out.println(currLid);
		}
		return episodeArray;
	}
	
	public String getNumberOfSeasons() throws NumberFormatException, IOException{
		GetEpisode latestEp = new GetEpisode(Integer.parseInt(getEpisodeId(getPreEpUrl())));
		return Integer.toString(latestEp.getSeason());
	}

	// INTERNAL METHODS
	
	private String getEpisodeId(String epUrl){
		return epUrl.substring(31);
		
	}
	private String[] convJsonArray(JsonArray currJArray){
		String[] currArray = new String[currJArray.size()];
		converter = new Gson();
		currArray = converter.fromJson(currJArray, String[].class);
		return currArray;
	}
}
