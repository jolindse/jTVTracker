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

	//private Map<Integer,Show> showsDB;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/tvtracker";
	private static final String USER = "root";
	private static final String PASS = "pectus";
	
	private Connection conn = null;
	private Statement stmt = null;
	
	
	public ShowDB() throws ClassNotFoundException, SQLException{
		
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		
	}
	
	public void addShow(Show currShow) throws SQLException{
	
		showId = currShow.getId();
		
		// Insert show in database
		System.out.println("Inserting show:\n\n"); // TEST
		stmt = conn.createStatement();
		String showSql;
		System.out.println(currShow.getSqlShow()); // TEST
		showSql = "INSERT INTO shows (showTvMazeId, showName, showSummary, showLanguage, showNetwork, showRuntime, showStatus, showUpdated) VALUES " + currShow.getSqlShow()+";";
		System.out.println(showSql); // TEST
		stmt.executeUpdate(showSql);
		
		ResultSet sqlResult = stmt.executeQuery("SELECT showId FROM shows WHERE showTvMazeId = " + showId+";");
		int showSqlId = 0;
		
		while(sqlResult.next()){
			showSqlId = sqlResult.getInt("showId");
		}
		
		System.out.println("ShowId: " + showId + " SQLId: "+showSqlId); // TEST
		
		// Insert episodes
		
		System.out.println("\n\nInserting episodes:\n");
		String epSql;
		String seasonSql;
		String addEpisodesString = "";
		String addSeasonString = "";
		
		int numberSeasons = currShow.getNumberSeasons();
		for (int i = 1; i <= numberSeasons; i++){
			System.out.println("Season "+i+"\n\n");
			int numberEps = currShow.getNumberOfEps(i);
			for (int j = 1; j <= numberEps; j++){
				String currEpString = currShow.getSqlEpisode(i, j);
				System.out.println(currEpString); // TEST
				epSql = "INSERT INTO episodes (episodeTvMazeId, episodeName, episodeNr, episodeSummary, episodeInstant) VALUES " + currEpString+";";
				System.out.println(epSql); // TEST
				stmt.executeUpdate(epSql+";");
				addEpisodesString += currEpString + "\n";
				
				int currEpId = currShow.getEpisode(i, j).getId();
				int currEpSqlId = 0;
				ResultSet episodeSql = stmt.executeQuery("SELECT episodeId FROM episodes WHERE episodeTvMazeId = "+currEpId+";");
				while(episodeSql.next()){
					currEpSqlId = episodeSql.getInt("episodeId");
				}
				System.out.println("CurrEpId: " + currEpId + " SQLId: " + currEpSqlId); // TEST
				String currSeasonString = "("+showSqlId+","+currEpSqlId+","+i+")";
				System.out.println(currSeasonString); // TEST
				seasonSql = "INSERT INTO seasons (seasonsShowId, seasonsEpisodeId, seasonsNumber) VALUES " + currSeasonString;
				stmt.executeUpdate(seasonSql);
				addSeasonString += currSeasonString + "\n";
			}
		}
		System.out.println(addEpisodesString+"\n");
		System.out.println("Seasons add statements:\n");
		System.out.println(addSeasonString);
	}
	
	public void removeShow(int id) {
		/*
		if (showsDB.containsKey(id)){
			showsDB.remove(id);
		}
		*/
	}
	
	public void getShow(int id) {
		// return showsDB.get(id);
	}
}
