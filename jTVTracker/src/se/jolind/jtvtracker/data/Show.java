package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import se.jolind.jtvtracker.data.tvmaze.GetEpisode;
import se.jolind.jtvtracker.data.tvmaze.GetShow;

/*
 *  Show class. 
 *  ------------------------
 *  Saves show data and has references to the season class associated 
 *  with the current show.
 *  
 */
public class Show {

	private GetShow currShow;
	private String name, lang, url, genres;
	private String[] imgArray;
	private int id, runtime, numberSeasons, latestEpisode, nextEpisode;
	private Map<Integer, Season> seasons;
	private boolean activeShow;

	/*
	public Show(int id) {
		this.id = id;
	}
	*/
	
	public Show(int id) throws NumberFormatException, IOException {
		GetShow currShow = new GetShow(id);

		name = currShow.getName();
		lang = currShow.getLang();
		genres = currShow.getGenres();
		imgArray = currShow.getImageUrl();
		id = currShow.getId();
		runtime = currShow.getRuntime();
		numberSeasons = Integer.parseInt(currShow.getNumberOfSeasons());
		activeShow = currShow.getStatus();
		seasons = makeSeasons(currShow.getAllEpId());
	}
	
	public void printShow(){
		System.out.println(seasons);
		for (int i=1; i<numberSeasons;i++){
			Season currSeason = seasons.get(i);
			currSeason.printSeasonEps();
		}
	}

	private Map<Integer, Season> makeSeasons(int[] allEps) throws IOException {
		// Init
		Map<Integer, Season> seasonMap = new HashMap<>();
		Season currSeason = new Season();
		int currSeasonNumber = 1;

		for (int currId : allEps) {
			GetEpisode currEp = new GetEpisode(currId);
			int episodeSeason = currEp.getSeason();
			if (episodeSeason == currSeasonNumber) {
				Episode ep = new Episode(currEp.getNumber(), currEp.getSummary(), currEp.getsURL(),
						currEp.getAirDate());
				System.out.println("Addar episode "+ep.getNumber()+" från säsong "+currSeasonNumber+" till mapen."); // Testar
				currSeason.addEpisode(ep);
				currSeason.setSeasonNumber(currSeasonNumber);
			} else {
				currSeason.setNumberOfEpisodes();
				seasonMap.put(currSeasonNumber, currSeason);
				currSeasonNumber = currEp.getSeason();
				System.out.println("Skapar ny säsong!");
				currSeason = new Season();
				Episode ep = new Episode(currEp.getNumber(), currEp.getSummary(), currEp.getsURL(),
						currEp.getAirDate());
				System.out.println("Addar episode "+ep.getNumber()+" från säsong "+currSeasonNumber+" till mapen."); // Testar
				currSeason.addEpisode(ep);
				currSeason.setSeasonNumber(currSeasonNumber);
			}
		}
		System.out.println(seasonMap);//Testar
		return seasonMap;
	}
}
