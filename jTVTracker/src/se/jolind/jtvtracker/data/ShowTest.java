package se.jolind.jtvtracker.data;

import java.io.IOException;
import java.util.Scanner;

import se.jolind.jtvtracker.data.tvmaze.GetShow;


public class ShowTest {
	static GetShow got;
	public ShowTest() throws IOException{
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
	
	public void doTest(int id){
		Show currShow;
		try {
			currShow = new Show(id);
			currShow.printShow();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}