package se.jolind.jtvtracker.application;

import java.io.IOException;

import javax.swing.SwingUtilities;

import se.jolind.jtvtracker.data.ShowTest;
import se.jolind.jtvtracker.gui.MainFrame;

public class Application {

	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame view = new MainFrame();
				Controller controller = new Controller(view);
				
			}
		});
		
		
		/*
		 *	Test console
		 * 
		
		try {
			ShowTest tester = new ShowTest();
			tester.testShow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
