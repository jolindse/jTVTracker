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

public class TvmSearch {

	private String searchBaseUrl = "http://api.tvmaze.com/search/shows?q=";
	private JsonArray searchArray;
	private List<TvmShortShow> resultList;
	// private JsonObject rootObject;
	private Gson converter;

	public TvmSearch(String searchString) {
		searchString = searchString.replace(" ", "+");

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

	public void buildList() {
		resultList = new ArrayList<>();
		for (int i = 0; i < searchArray.size(); i++) {
			JsonObject currRoot = searchArray.get(i).getAsJsonObject();
			JsonObject currShow = currRoot.get("show").getAsJsonObject();
			int currId = currShow.get("id").getAsInt();
			String currName = currShow.get("name").getAsString();
			String currSummary = currShow.get("summary").getAsString();
			ImageIcon currIcon = makeImgIcon(getImageUrl(currShow));
			TvmShortShow currShortShow = new TvmShortShow(currId, currName, currSummary, currIcon);
			resultList.add(currShortShow);
		}
	}

	private ImageIcon makeImgIcon(String imgUrlString) {
		// Get image original size
		URL imgUrl;
		Image mediumImg = null;
		try {
			if (imgUrlString.equals("resources/noImage.png")) {

				mediumImg = ImageIO.read(new File(imgUrlString));
			} else {
				imgUrl = new URL(imgUrlString);
				mediumImg = ImageIO.read(imgUrl);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Resize and create ImageIcon
		Image scaledImg = mediumImg.getScaledInstance(60, 75, Image.SCALE_SMOOTH);
		ImageIcon returnIcon = new ImageIcon(scaledImg);
		return returnIcon;
	}

	private String getImageUrl(JsonObject rootObject) {
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

	public void printResults() {
		for (TvmShortShow currShow : resultList) {
			System.out.println(currShow);
		}
	}

}
