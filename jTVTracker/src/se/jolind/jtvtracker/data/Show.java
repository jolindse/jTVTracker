package se.jolind.jtvtracker.data;

import java.io.IOException;

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
	private Season[] seasons;
	private boolean activeShow;
	
	public Show(int id) {
		this.id = id;
	}

	public Show(GetShow currShow) throws NumberFormatException, IOException{
		this.currShow = currShow;
		
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
		
	private Season[] makeSeasons(int[] allEps) throws IOException{
		Season[] seasonArray = new Season[numberSeasons];
		int currSeasonNumber = 1;
		Season currSeason = new Season();
		for (int currId: allEps){
			GetEpisode currEp = new GetEpisode(currId);
			if (currEp.getSeason() == currSeasonNumber){
				
			}
			
			
		}
	}
}
