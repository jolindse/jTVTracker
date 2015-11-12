package se.jolind.jtvtracker.data.tvmaze;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import se.jolind.jtvtracker.application.Controller;
import se.jolind.jtvtracker.gui.interfaces.IProgress;

/*
 * The search class wich parses search result from TvMaze.com
 */

public class TvmSearch {

	private String searchBaseUrl = "http://api.tvmaze.com/search/shows?q=";
	private IProgress progressListener;
	private JsonArray searchArray;
	private List<TvmShortShow> resultList;
	private Gson converter;

	public TvmSearch(String searchString) {
		/*
		 * Initiates search and gets json response
		 */
		searchString = searchString.replace(" ", "+");
		
		progressListener = Controller.getListener();

		String sURL = searchBaseUrl + searchString;

		HttpURLConnection request;
		try {
			URL url = new URL(sURL);
			request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			searchArray = root.getAsJsonArray();
		} catch (IOException e) {
			System.out.println("Problem getting network respons.");
			e.printStackTrace();
		}
		buildList();
	}

	private void buildList() {
		/*
		 * Builds a ArrayList with show information from search results
		 */
		resultList = new ArrayList<>();
		
		progressListener.initProgressBar(0, searchArray.size());
		
		for (int i = 0; i < searchArray.size(); i++) {
			JsonObject currRoot = searchArray.get(i).getAsJsonObject();
			JsonObject currShow = currRoot.get("show").getAsJsonObject();
			int currId = currShow.get("id").getAsInt();
			String currName = currShow.get("name").getAsString();
			String currSummary = currShow.get("summary").getAsString();
			ImageIcon currIcon = makeImgIcon(getImageUrl(currShow));
			TvmShortShow currShortShow = new TvmShortShow(currId, currName, currSummary, currIcon);
			resultList.add(currShortShow);
			progressListener.increaseProgressBar();
		}
	}

	private ImageIcon makeImgIcon(String imgUrlString) {
		/*
		 * Makes an ImageIcon from an Image-url and resizes
		 * if for display in search results
		 */
		
		// Get image original size
		boolean imgFromUrl = false;
		URL imgUrl;
		Image mediumImg = null;
		try {
				if (!imgUrlString.equals("resources/noImage.png")){
				imgUrl = new URL(imgUrlString);
				mediumImg = ImageIO.read(imgUrl);
				imgFromUrl = true;
				}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Do nothing
		}
		if (imgFromUrl){
		// Resize and create ImageIcon
		Image scaledImg = mediumImg.getScaledInstance(60, 75, Image.SCALE_SMOOTH);
		ImageIcon returnIcon = new ImageIcon(scaledImg);
		return returnIcon;
		}
		return getDefaultIcon();
	}
	
	private ImageIcon getDefaultIcon(){
		/*
		 * Returns the default placeholder image as icon resized for display
		 * in search results
		 */
		Image mediumImg = null;
		try {
			mediumImg = ImageIO.read(new File("resources/noImage.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image scaledImg = mediumImg.getScaledInstance(60, 75, Image.SCALE_SMOOTH);
		ImageIcon returnIcon = new ImageIcon(scaledImg);
		return returnIcon;
	}

	private String getImageUrl(JsonObject rootObject) {
		/*
		 * Gets the image URL
		 */
		String imgUrl = "";
		if (rootObject.get("image").isJsonNull()) {
			imgUrl = "resources/noImage.png";

		} else {
			JsonObject image = rootObject.getAsJsonObject("image");
			imgUrl = image.get("medium").getAsString();

		}
		return imgUrl;
	}

	public List<TvmShortShow> getList() {
		/*
		 * Returns the list to display in the gui. Only top six
		 * matches for the search term goes is being sent.
		 */
		List<TvmShortShow> listToFeed = new ArrayList<>();

		if (resultList.size() > 6) {
			for (int i = 0; i < 6; i++) {
				listToFeed.add(resultList.get(i));
			}
		} else {
			for (int i = 0; i < resultList.size(); i++) {
				listToFeed.add(resultList.get(i));
			}
		}

		return listToFeed;
	}

}
