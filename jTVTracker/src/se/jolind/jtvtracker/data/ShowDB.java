package se.jolind.jtvtracker.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.imageio.ImageIO;

/*
 * DAO class
 * Handles all SQL-queries to database and connection.
 */
public class ShowDB {

	private int showId;

	// private Map<Integer,Show> showsDB;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/tvtracker";
	private static final String USER = "root";
	private static final String PASS = "pectus";

	private Connection conn = null;
	private Statement stmt = null;

	public ShowDB() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

	}

	public void addShow(Show currShow) throws SQLException, IOException {
		/*
		 * Adds a show to database
		 */

		showId = currShow.getId();
		String sqlString = "";

		// Insert show in database
		stmt = conn.createStatement();
		String showSql;
		showSql = "INSERT INTO shows (showTvMazeId, showName, showSummary, showLanguage, showNetwork, showRuntime, showStatus, showUpdated, showTimeZone, showPreUrl, showNextUrl, showScheduleTime) VALUES "
				+ currShow.getSqlShow() + ";";
		stmt.executeUpdate(showSql);

		ResultSet sqlResult = stmt.executeQuery("SELECT showId FROM shows WHERE showTvMazeId = " + showId + ";");
		int showSqlId = 0;

		while (sqlResult.next()) {
			showSqlId = sqlResult.getInt("showId");
		}
		
		// Save and insert image
		String showNameCleaned = currShow.getName().replaceAll("'", "");
		String fileName = currShow.getId() + showNameCleaned.replaceAll(" ", "_");
		String filePath = "images/"+fileName;
		BufferedImage image = currShow.getImageForSave();
		ImageIO.write(image,"jpg", new File(filePath));
						
		sqlString = "UPDATE shows SET shows.showImage = '"+filePath+"' WHERE shows.showId = "+showSqlId+";";
		stmt.executeUpdate(sqlString);
		
		// Insert days

		String[] daysArray = currShow.getDays();
		for (String currDay : daysArray) {
			ResultSet daysResult = stmt.executeQuery("SELECT daysId FROM days WHERE daysName = '" + currDay + "';");
			if (daysResult.next()) {
				int dayId = daysResult.getInt("daysId");
				String daysSqlString = "INSERT INTO showdays (showdaysDaysId,showdaysShowId) VALUES (" + dayId + ","
						+ showSqlId + ")" + ";";
				stmt.executeUpdate(daysSqlString);
			}
		}
		
		// Insert genres

		String[] genresArray = currShow.getGenresArray();
		for (String currGenre : genresArray)

		{
			ResultSet genresSqlId = stmt
					.executeQuery("SELECT genresId FROM genres WHERE genresName = '" + currGenre + "';");
			Integer genreId;
			if (genresSqlId.next()) {
				genreId = genresSqlId.getInt("genresId");
				String genreSqlString = "INSERT INTO showgenres (showgenresShowId, showgenresGenreId) VALUES ("
						+ showSqlId + "," + genreId + ");";
				stmt.executeUpdate(genreSqlString);
			} else {
				genreId = 1;
				String addGenreSql = "INSERT INTO genres (genresName) VALUES ('" + currGenre + "');";
				stmt.executeUpdate(addGenreSql);
				genresSqlId = stmt.executeQuery("SELECT genresId FROM genres WHERE genresName = '" + currGenre + "';");
				if (genresSqlId.next()) {
					genreId = genresSqlId.getInt("genresId");
				}
				String genreSqlString = "INSERT INTO showgenres (showgenresShowId, showgenresGenreId) VALUES ("
						+ showSqlId + "," + genreId + ");";
				stmt.executeUpdate(genreSqlString);
			}
		}
		
		// Insert episodes

		String epSql;
		String seasonSql;
		String addEpisodesString = "";
		String addSeasonString = "";

		int numberSeasons = currShow.getNumberSeasons();
		for (int i = 1; i <= numberSeasons; i++)

		{
			int numberEps = currShow.getNumberOfEps(i);
			for (int j = 1; j <= numberEps; j++) {
				String currEpString = currShow.getSqlEpisode(i, j);
				epSql = "INSERT INTO episodes (episodeTvMazeId, episodeName, episodeNr, episodeSummary, episodeInstant) VALUES "
						+ currEpString + ";";
				stmt.executeUpdate(epSql + ";");
				
				int currEpId = currShow.getEpisode(i, j).getId();
				int currEpSqlId = 0;
				ResultSet episodeSql = stmt
						.executeQuery("SELECT episodeId FROM episodes WHERE episodeTvMazeId = " + currEpId + ";");
				while (episodeSql.next()) {
					currEpSqlId = episodeSql.getInt("episodeId");
				}
				
				// Make images and add path
				String epNameCleaned = currShow.getName().replaceAll("'", "");
				String epFileName = currShow.getEpisode(i, j).getId()+"S"+i+"E"+j+epNameCleaned.replaceAll(" ", "_");
				String epFilePath = "images/"+epFileName;
				BufferedImage epImage = currShow.getEpisode(i, j).getBufferedImage();
				ImageIO.write(epImage,"jpg", new File(epFilePath));
				
				sqlString = "UPDATE episodes SET episodes.episodeImage = '"+epFilePath+"' WHERE episodes.episodeId = "+currEpSqlId+";";
				stmt.executeUpdate(sqlString);
				
				// Make seasons and add episodes
				
				String currSeasonString = "(" + showSqlId + "," + currEpSqlId + "," + i + ")";
				seasonSql = "INSERT INTO seasons (seasonsShowId, seasonsEpisodeId, seasonsNumber) VALUES "
						+ currSeasonString;
				stmt.executeUpdate(seasonSql);
			}
		}

	}

	public void removeShow(int id) throws SQLException {
		/*
		 * Removes a show from database
		 */
		
		int sqlShowId = 0;
		int numEps = 0;
		int[] sqlEpisodeId;
		String showImgPath = "";
		
		String sqlString = "";
		stmt = conn.createStatement();
		
		sqlString = "SELECT shows.showId, shows.showImage FROM shows WHERE shows.showTvMazeId = "+id+";";
		ResultSet sqlShow = stmt.executeQuery(sqlString);
		while(sqlShow.next()){
			sqlShowId = sqlShow.getInt("showId");
			showImgPath = sqlShow.getString("showImage");
		}
		
		sqlString = "SELECT COUNT(seasons.seasonsEpisodeId) AS numberOfEps FROM seasons WHERE seasons.seasonsShowId = "+sqlShowId+";";
		ResultSet sqlNumEp = stmt.executeQuery(sqlString);
		while(sqlNumEp.next()){
			numEps = sqlNumEp.getInt("numberOfEps");
		}
		
		sqlString = "SELECT seasons.seasonsEpisodeId FROM seasons WHERE seasons.seasonsShowId = "+sqlShowId+";";
		ResultSet sqlEpIds = stmt.executeQuery(sqlString);
		
		sqlEpisodeId = new int[numEps];
		int epCounter = 0;
		while (sqlEpIds.next()){
			sqlEpisodeId[epCounter] = sqlEpIds.getInt("seasonsEpisodeId");
			epCounter++;
		}
		
		
		for (int currEp: sqlEpisodeId){
			sqlString = "SELECT episodes.episodeImage FROM episodes WHERE episodes.episodeId = "+currEp+";";
			ResultSet currImgPath = stmt.executeQuery(sqlString);
			while (currImgPath.next()){
				Path currPath = Paths.get(currImgPath.getString("episodeImage"));
				try {
					Files.deleteIfExists(currPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		sqlString = "DELETE FROM seasons WHERE seasons.seasonsShowId = "+sqlShowId+";";
		stmt.executeUpdate(sqlString);
		
		for (int currId: sqlEpisodeId){
			sqlString = "DELETE FROM episodes WHERE episodes.episodeId = "+currId+";";
			stmt.executeUpdate(sqlString);
		}
		
		sqlString = "DELETE FROM showDays WHERE showdaysShowId = "+sqlShowId+";";
		stmt.executeUpdate(sqlString);
		sqlString = "DELETE FROM showgenres WHERE showgenresShowId = "+sqlShowId+";";
		stmt.executeUpdate(sqlString);
		sqlString = "DELETE FROM shows WHERE shows.showId = "+sqlShowId+";";
		stmt.executeUpdate(sqlString);
		
		Path currPath = Paths.get(showImgPath);
		try {
			Files.deleteIfExists(currPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Show getShow(int id) throws SQLException {
		/*
		 * Makes a complete show object from database values and returns it.
		 * 
		 */
		
		// Init variables
		int sqlShowId = 0;
		int numSeasons = 0;
		int numEpisodes = 0;
		String showName = "";
		String showSummary = "";
		String showLang = "";
		String showNetwork = "";
		String showRuntime = "";
		String showStatus = "";
		String showTimeZone = "";
		String showNextEpUrl = "";
		String showPreEpUrl = "";
		String showScheduleTime = "";
		int showUpdated = 0;
		int episodeTvMazeId = 0;
		String episodeName = "";
		String episodeSummary = "";
		long episodeInstant = 0;
		int episodeNumber = 0;

		stmt = conn.createStatement();
		// Get sql show Id and other info
		String sqlString = "SELECT * FROM shows WHERE shows.showTvMazeId=" + id + ";";
		ResultSet sqlShow = stmt.executeQuery(sqlString);
		while (sqlShow.next()) {
			showName = sqlShow.getString("showName");
			sqlShowId = sqlShow.getInt("showId");
			showSummary = sqlShow.getString("showSummary");
			showLang = sqlShow.getString("showLanguage");
			showNetwork = sqlShow.getString("showNetwork");
			showRuntime = sqlShow.getString("showRuntime");
			showStatus = sqlShow.getString("showStatus");
			showTimeZone = sqlShow.getString("showTimeZone");
			showUpdated = sqlShow.getInt("showUpdated");
			showNextEpUrl = sqlShow.getString("showNextUrl");
			showPreEpUrl = sqlShow.getString("showPreUrl");
			showScheduleTime = sqlShow.getString("showScheduleTime");
		}
		sqlString = "SELECT MAX(seasons.seasonsNumber) AS numberOfSeasons FROM seasons WHERE seasons.seasonsShowId="
				+ sqlShowId + ";";
		ResultSet sqlNumSeasons = stmt.executeQuery(sqlString);
		while (sqlNumSeasons.next()) {
			numSeasons = sqlNumSeasons.getInt("numberOfSeasons");
		}

		// Make show
		Show currShow = new Show(id, showName, showSummary, showLang, showNetwork, showRuntime, showStatus,
				showUpdated, showScheduleTime);
		currShow.setPreEpUrl(showPreEpUrl);
		currShow.setNextEpUrl(showNextEpUrl);

		// Get genres
		int numberOfGenres = 0;
		sqlString = "SELECT COUNT(genres.genresName) AS numberOfGenres FROM genres INNER JOIN showgenres ON genresId = showgenres.showgenresGenreId INNER JOIN shows ON showgenres.showgenresShowId = shows.showId WHERE shows.showId ="
				+ sqlShowId + ";";
		ResultSet sqlNumGenres = stmt.executeQuery(sqlString);
		while (sqlNumGenres.next()) {
			numberOfGenres = sqlNumGenres.getInt("numberOfGenres");
		}

		String[] showGenres = new String[numberOfGenres];
		sqlString = "SELECT genres.genresName FROM genres INNER JOIN showgenres ON genresId = showgenres.showgenresGenreId INNER JOIN shows ON showgenres.showgenresShowId = shows.showId WHERE shows.showId = "
				+ sqlShowId + ";";
		ResultSet sqlGenres = stmt.executeQuery(sqlString);
		int genreCounter = 0;
		while(sqlGenres.next()){
			showGenres[genreCounter] = sqlGenres.getString("genresName");
			genreCounter++;
		}

		currShow.setGenresArray(showGenres);

		// Get days
		int numberOfDays = 0;
		sqlString = "SELECT COUNT(days.daysName) AS numberOfDays FROM days INNER JOIN showdays ON days.daysId = showdays.showdaysDaysId INNER JOIN shows ON showdays.showdaysShowId = shows.showId WHERE shows.showId = "
				+ sqlShowId + ";";
		ResultSet sqlNumDays = stmt.executeQuery(sqlString);
		while (sqlNumDays.next()) {
			numberOfDays = sqlNumDays.getInt("numberOfDays");
		}

		String[] showDays = new String[numberOfDays];
		sqlString = "SELECT days.daysName FROM days INNER JOIN showdays ON days.daysId = showdays.showdaysDaysId INNER JOIN shows ON showdays.showdaysShowId = shows.showId WHERE shows.showId = "
				+ sqlShowId + ";";
		ResultSet sqlDays = stmt.executeQuery(sqlString);
		int daysCounter = 0;
		while(sqlDays.next()){
			showDays[daysCounter] = sqlDays.getString("daysName");
			daysCounter++;
		}
		currShow.setDays(showDays);

		// ADD SHOW IMAGE
		
		sqlString = "SELECT shows.showImage FROM shows WHERE shows.showId = "+sqlShowId+";";
		ResultSet sqlImage = stmt.executeQuery(sqlString);
		String imgPath = "";
		while(sqlImage.next()){
			imgPath = sqlImage.getString("showImage");
		}
		currShow.setImage(imgPath);
		
		

		// Make seasons and populate with episodes

		for (int i = 1; i <= numSeasons; i++) {
			// Make a season object
			Season currSeason = new Season();
			currSeason.setSeasonNumber(i);
			// Get number of episodes in season
			sqlString = "SELECT COUNT(seasons.seasonsEpisodeId) AS numberOfEpisodes FROM seasons WHERE seasons.seasonsShowId ="
					+ sqlShowId + " AND seasons.seasonsNumber = " + i + ";";
			ResultSet sqlNumEpisodes = stmt.executeQuery(sqlString);
			while (sqlNumEpisodes.next()) {
				numEpisodes = sqlNumEpisodes.getInt("numberOfEpisodes");
			}
			for (int j = 1; j <= numEpisodes; j++) {
				// Create and add episode
				sqlString = "SELECT episodes.* FROM episodes INNER JOIN seasons ON seasons.seasonsEpisodeId = episodes.episodeId INNER JOIN shows ON shows.showId = seasons.seasonsShowId WHERE shows.showId = "
						+ sqlShowId + " AND seasons.seasonsNumber = " + i + " AND episodes.episodeNr = " + j + ";";
				ResultSet sqlEpisode = stmt.executeQuery(sqlString);
				while (sqlEpisode.next()) {
					episodeTvMazeId = sqlEpisode.getInt("episodeTvMazeId");
					episodeName = sqlEpisode.getString("episodeName");
					episodeNumber = sqlEpisode.getInt("episodeNr");
					episodeSummary = sqlEpisode.getString("episodeSummary");
					episodeInstant = sqlEpisode.getLong("episodeInstant");
				}
				Episode currEpisode = new Episode(episodeTvMazeId, episodeName, episodeNumber, episodeSummary);
				
				// Get image and add to episode object
				
				sqlString = "SELECT episodes.episodeImage FROM episodes WHERE episodes.episodeTvMazeId = "+episodeTvMazeId+";";
				ResultSet sqlEpImage = stmt.executeQuery(sqlString);
				String epImgPath = "";
				while(sqlEpImage.next()){
					epImgPath = sqlEpImage.getString("episodeImage");
				}
				currEpisode.setImage(epImgPath);
				
				// Create and add AirTime to episode
				
				Long epInstant = (long) 0;
				sqlString = "SELECT episodes.episodeInstant FROM episodes WHERE episodes.episodeTvMazeId = "+episodeTvMazeId+";";
				ResultSet sqlInstant = stmt.executeQuery(sqlString);
				while(sqlInstant.next()){
					epInstant = sqlInstant.getLong("episodeInstant");
				}
				AirTime epAirTime = new AirTime(epInstant, showTimeZone);
				currEpisode.setAirTime(epAirTime);
				
				// Add episode to seasons
				
				currSeason.addEpisode(currEpisode);
			}
			currShow.addSeason(currSeason, i);
		}
		
		// Set premiere time to show by first season first episode
		Long premInstant = (long) 0;
		
		sqlString = "SELECT episodes.* FROM episodes INNER JOIN seasons ON seasons.seasonsEpisodeId = episodes.episodeId INNER JOIN shows ON shows.showId = seasons.seasonsShowId WHERE shows.showId = "+ sqlShowId + " AND seasons.seasonsNumber = 1 AND episodes.episodeNr = 1;";
		ResultSet sqlPremiere = stmt.executeQuery(sqlString);
		while (sqlPremiere.next()){
			premInstant = sqlPremiere.getLong("episodeInstant");
		}

		AirTime premTime = new AirTime(premInstant, showTimeZone);
		currShow.setAirTime(premTime);
		currShow.secondInit();
		
		return currShow;
	}
	
	public int[] getShowIds() throws SQLException {
		/*
		 * Returns all TvMaze ids in int-array.
		 */
		stmt = conn.createStatement();
		String sqlString = "SELECT COUNT(showId) AS numberOfShows FROM shows;";
		ResultSet sqlFavShows = stmt.executeQuery(sqlString);
		int numberOfShows = 0;
		while(sqlFavShows.next()){
			numberOfShows = sqlFavShows.getInt("numberOfShows");
		}
		
		
		sqlString = "SELECT showTvMazeId FROM shows;";
		ResultSet sqlShowIds = stmt.executeQuery(sqlString);
		int[] showArray = new int[numberOfShows];
		int counter = 0;
		
		while(sqlShowIds.next()){
			int currId = sqlShowIds.getInt("showTvMazeId");
			showArray[counter] = currId;
			counter++;
		}
		return showArray;
	}
}
