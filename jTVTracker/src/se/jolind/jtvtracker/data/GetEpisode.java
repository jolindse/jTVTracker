package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetEpisode {
	

	/*
	 * Episode variables
	 * Declaration of variables retrieved from the Json-response of episode check.
	 */
	
	private int epId, season, epNumber;
	private String epName, airDate, airTime, timeZone, epSummary;

	private JsonObject rootObject;
	private String epBaseUrl = "http://apt.tvmaze.com/episodes/";

	public GetEpisode(int id) throws IOException {
		String sURL = epBaseUrl + Integer.toString(id);

		// Connect to the URL using java's native library
		URL url = new URL(sURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// Convert to a JSON object to print data
		JsonParser jp = new JsonParser(); // from gson
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		rootObject = root.getAsJsonObject(); // May be an array, may be
	}

	

}
