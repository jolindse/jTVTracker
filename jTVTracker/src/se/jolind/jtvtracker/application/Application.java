package se.jolind.jtvtracker.application;

import java.io.IOException;

import se.jolind.jtvtracker.data.ShowTest;

public class Application {

	public static void main(String[] args) {
		try {
			ShowTest tester = new ShowTest();
			tester.testDates();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
