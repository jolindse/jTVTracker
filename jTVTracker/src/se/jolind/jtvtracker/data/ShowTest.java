package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.util.Scanner;

import se.jolind.jtvtracker.data.tvmaze.GetShow;


public class ShowTest {
	static GetShow got;
	public ShowTest() throws IOException{
	}

	
	
	public void testShow(){
		boolean keepMenu = true;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("V�lj show id:");
		int showId = sc.nextInt();
		
		Show currShow = new Show(showId);
		System.out.println("Scannar showen!");
		currShow.scanSeasons();
		System.out.println("F�rdig scannat!");
		
		while (keepMenu){
			System.out.println(currShow.getName() + " antal s�songer: "+currShow.getNumberSeasons()+" antal episoder: "+currShow.getLatestEpisode()+"\n\n");
			System.out.println("V�lj:\n"
					+"1. Visa episod\n"
					+"2. Skriv ut alla episoder\n"
					+"3. Avsluta\n");
			String choice = sc.nextLine();
			switch(choice){
			case "1":
				System.out.println("V�lj s�song:");
				int season = sc.nextInt();
				System.out.println("V�lj episod:");
				int episode = sc.nextInt();
				System.out.println(currShow.printEpisode(season, episode));
				break;
			case "2":
				currShow.printShow();
				break;
			case "3":
				keepMenu = false;
				break;
			}
			
					
		}
		
	}
	
	public void testDates(){
		AirTime airtime = new AirTime("21:00", "2015-04-03", "America/New_York");
		System.out.println("Original time: "+airtime.getZonedDateAsString() + " at " + airtime.getZonedTimeAsString());
		System.out.println("Localized time: "+airtime.getLocalDateAsString() + " at " + airtime.getLocalTimeAsString());
	}
	
	
}