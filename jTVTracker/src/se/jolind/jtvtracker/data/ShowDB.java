package se.jolind.jtvtracker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Skeleton for DAO for show database
 * all to be replaced by calls to SQL-database
 */
public class ShowDB {

	private int showId;

	// private Map<Integer,Show> showsDB;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/tvtracker";
	private static final String USER = "dbuser";
	private static final String PASS = "password";

	private Connection conn = null;
	private Statement stmt = null;

	public ShowDB() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

	}

	public void addShow(Show currShow) throws SQLException {

		showId = currShow.getId();

		// Insert show in database
		stmt = conn.createStatement();
		String showSql;
		showSql = "INSERT INTO shows (showTvMazeId, showName, showSummary, showLanguage, showNetwork, showRuntime, showStatus, showUpdated) VALUES "
				+ currShow.getSqlShow() + ";";
		stmt.executeUpdate(showSql);

		System.out.println("Add show:\n\n");
		System.out.println(currShow.getSqlShow()+",");

		ResultSet sqlResult = stmt.executeQuery("SELECT showId FROM shows WHERE showTvMazeId = " + showId + ";");
		int showSqlId = 0;

		while (sqlResult.next()) {
			showSqlId = sqlResult.getInt("showId");
		}

		// Insert days

		String[] daysArray = currShow.getDays();
		System.out.println(daysArray); // TEST
		System.out.println("\nAdd days:\n\n"); // TEST
		for (String currDay : daysArray) {
			System.out.println(currDay); // TEST
			ResultSet daysResult = stmt.executeQuery("SELECT daysId FROM days WHERE daysName = '" + currDay + "';");
			if (daysResult.next()) {
				int dayId = daysResult.getInt("daysId");
				String daysSqlString = "INSERT INTO showdays (showdaysDaysId,showdaysShowId) VALUES (" + dayId + ","
						+ showSqlId + ")" + ";";
				stmt.executeUpdate(daysSqlString);
				System.out.println("(" + dayId + ","
						+ showSqlId + ")" + ","); // TEST
			}
		}

		// Insert genres

		String[] genresArray = currShow.getGenresArray();
		System.out.println("\nAdd genres:\n\n"); // TEST
		for (String currGenre : genresArray)

		{
			ResultSet genresSqlId = stmt
					.executeQuery("SELECT genresId FROM genres WHERE genresName = '" + currGenre + "';");
			Integer genreId;
			if (genresSqlId.next()) {
				genreId = genresSqlId.getInt("genresId");
				String genreSqlString = "INSERT INTO showgenres (showgenresShowId, showgenresGenreId) VALUES ("
						+ showSqlId + "," + genreId + ");";
				System.out.println("\nAdd show to genre list:\n\n"); // TEST
				stmt.executeUpdate(genreSqlString);
				System.out.println(genreSqlString); // TEST
			} else {
				genreId = 1;
				String addGenreSql = "INSERT INTO genres (genresName) VALUES ('" + currGenre + "');";
				System.out.println("\nAdding genre and show\n\n"); // TEST
				stmt.executeUpdate(addGenreSql);
				System.out.println(addGenreSql); // TEST
				genresSqlId = stmt.executeQuery("SELECT genresId FROM genres WHERE genresName = '" + currGenre + "';");
				if (genresSqlId.next()) {
					genreId = genresSqlId.getInt("genresId");
				}
				String genreSqlString = "INSERT INTO showgenres (showgenresShowId, showgenresGenreId) VALUES ("
						+ showSqlId + "," + genreId + ");";
				stmt.executeUpdate(genreSqlString);
				System.out.println(genreSqlString); // TEST
			}
		}

		// Insert episodes

		System.out.println("\nInserting episodes:\n"); // TEST

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
				addEpisodesString += currEpString + ",\n";

				int currEpId = currShow.getEpisode(i, j).getId();
				int currEpSqlId = 0;
				ResultSet episodeSql = stmt
						.executeQuery("SELECT episodeId FROM episodes WHERE episodeTvMazeId = " + currEpId + ";");
				while (episodeSql.next()) {
					currEpSqlId = episodeSql.getInt("episodeId");
				}
				String currSeasonString = "(" + showSqlId + "," + currEpSqlId + "," + i + ")";
				seasonSql = "INSERT INTO seasons (seasonsShowId, seasonsEpisodeId, seasonsNumber) VALUES "
						+ currSeasonString;
				stmt.executeUpdate(seasonSql);
				addSeasonString += currSeasonString + ",\n";
			}
		}
		System.out.println(addEpisodesString + "\n");
		System.out.println("Seasons add statements:\n");
		System.out.println(addSeasonString);

	}

	public void removeShow(int id) {
		/*
		 * if (showsDB.containsKey(id)){ showsDB.remove(id); }
		 */
	}

	public void getShow(int id) {
		// return showsDB.get(id);
	}
}
