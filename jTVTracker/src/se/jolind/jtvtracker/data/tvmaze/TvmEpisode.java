package se.jolind.jtvtracker.data.tvmaze;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/*
 *  Wrapper class for episode info from tvmaze.com
 *  ----------------------------------------------
 *  Parses json response from tvmaze.
 */

public class TvmEpisode {

	private String sURL;
	private JsonObject rootObject;
	private String epBaseUrl = "http://api.tvmaze.com/episodes/";

	public TvmEpisode(int id) {
		sURL = epBaseUrl + Integer.toString(id);
		URL url;
		JsonElement root = null;
		try {
			url = new URL(sURL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser(); 
			root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rootObject = root.getAsJsonObject(); 
	}

	public String getName() {
		/*
		 * Returns episode name
		 */
		return rootObject.get("name").getAsString();
	}

	public int getSeason() {
		/*
		 * Returns episode season
		 */
		return rootObject.get("season").getAsInt();
	}

	public int getNumber() {
		/*
		 * Returns episode number
		 */
		return rootObject.get("number").getAsInt();
	}

	public String getAirDate() {
		/*
		 * Returns the airdate field as string
		 */
		return rootObject.get("airdate").getAsString();
	}

	public String getAirTime() {
		/*
		 * Returns the airtime field as string
		 */
		return rootObject.get("airtime").getAsString();
	}

	public String getAirStamp() {
		/*
		 * Returns the airstams field as string
		 */
		return rootObject.get("airstamp").getAsString();
	}

	public String getSummary() {
		/*
		 * Returns the episode summary
		 */
		return rootObject.get("summary").getAsString();
	}

	public String getsURL() {
		/*
		 * Returns the url for the episode
		 */
		return sURL;
	}

	public String[] getImgUrl() {
		/*
		 * Returns an array with urls to episode images if present in json,
		 * if not returns reference to default picture to be used as placeholder.
		 */
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

}
