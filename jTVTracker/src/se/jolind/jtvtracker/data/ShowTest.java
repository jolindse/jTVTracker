package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.util.Scanner;

import se.jolind.jtvtracker.data.tvmaze.GetShow;


public class ShowTest {
	static GetShow got;
	public ShowTest() throws IOException{
	}

	
	public static void main(String[] args) throws IOException {
		got = new GetShow(82);

		System.out.println(got.getName());
		System.out.println(got.getType());
		System.out.println(got.getStatus());
		System.out.println(got.getRuntime());
		System.out.println(got.getGenres());
		System.out.println(got.getPreEpUrl());
		System.out.println(got.getNextEpUrl());
		System.out.println(got.getSummary());
		System.out.println(got.getLang());
		System.out.println(got.getSchedule());
		System.out.println(got.getNumberOfSeasons());
		
		String[] testeps = got.getAllEpId();
	}
	
	public void testShow(){
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Välj show id:");
		int showId = sc.nextInt();
		try {
			GetShow currShow = new GetShow(showId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}