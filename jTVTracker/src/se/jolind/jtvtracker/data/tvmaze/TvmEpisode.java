package se.jolind.jtvtracker.data.tvmaze;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 *  Wrapper class for episode info from tvmaze.com
 *  ----------------------------------------------
 *  Parses json response from tvmaze.
 */

public class TvmEpisode {

	private String sURL;
	private JsonObject rootObject;
	private String epBaseUrl = "http://api.tvmaze.com/episodes/";

	public TvmEpisode(int id) throws IOException {
		sURL = epBaseUrl + Integer.toString(id);

		// Connect to the URL using java's native library
		URL url = new URL(sURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// Convert to a JSON object to print data
		JsonParser jp = new JsonParser(); // from gson
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		rootObject = root.getAsJsonObject(); // May be an array, may be
	}

	public String getName() {
		return rootObject.get("name").getAsString();
	}

	public int getSeason() {
		return rootObject.get("season").getAsInt();
	}

	public int getNumber() {
		return rootObject.get("number").getAsInt();
	}

	public String getAirDate() {
		return rootObject.get("airdate").getAsString();
	}

	public String getAirTime() {
		return rootObject.get("airtime").getAsString();
	}

	public String getAirStamp() {
		return rootObject.get("airstamp").getAsString();
	}

	public String getSummary() {
		return rootObject.get("summary").getAsString();
	}

	public String getsURL() {
		return sURL;
	}

	public String[] getImgUrl() {
		String[] imageArray = new String[2];
		if (rootObject.get("image").isJsonNull()){
			imageArray[0] = "resources/noImage.png";
			imageArray[1] = "resources/noImage.png";
		}else{
		JsonObject image = rootObject.getAsJsonObject("image");
			imageArray[0] = image.get("medium").getAsString();
			imageArray[1] = image.get("original").getAsString();
		}
		return imageArray;
	}

}
