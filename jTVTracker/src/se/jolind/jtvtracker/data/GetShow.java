package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;

import org.omg.CORBA.Request;

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
		genre = rootObject.getAsJsonArray("genres");
	}
}
