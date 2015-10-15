package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetShow {
	
	private String showBaseUrl = "http://api.tvmaze.com/shows/";
	private JsonObject rootObject;
				
	/*
	 *  Show variables.
	 *  Declarations of variables retrieved from the Json response of show.
	 */
			
	private String showName, type, lang, status, premDate, showSummary, prevEpUrl; 
	private String[] genre, schedule, network;
	private int runtime, updateTime;
	private Gson converter;
	

	public GetShow(int id) throws IOException {
		String sURL = showBaseUrl + Integer.toString(id);
		
		URL url = new URL(sURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		JsonParser jp = new JsonParser(); // from gson
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); 
		rootObject = root.getAsJsonObject(); // May be an array, may be
														// an object.
	
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
	
	public String getStatus(){
		return rootObject.get("status").getAsString();
	}
	
	public String getSummary(){
		return rootObject.get("summary").getAsString();
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
	
	private String[] convJsonArray(JsonArray currJArray){
		String[] currArray = new String[currJArray.size()];
		return currArray;
	}
}
