package se.jolind.jtvtracker.data;

import java.util.HashMap;
import java.util.Map;

/*
 * Skeleton for DAO for show database
 * all to be replaced by calls to SQL-database
 */
public class ShowDB {

	private Map<Integer,Show> showsDB;
	
	public ShowDB(){
		showsDB = new HashMap<>();
	}
	
	public void addShow(int id, Show currShow){
		showsDB.put(id,currShow);
	}
	
	public void removeShow(int id) {
		if (showsDB.containsKey(id)){
			showsDB.remove(id);
		}
	}
	
	public Show getShow(int id) {
		return showsDB.get(id);
	}
}
