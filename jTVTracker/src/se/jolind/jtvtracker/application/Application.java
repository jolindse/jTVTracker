package se.jolind.jtvtracker.application;

import javax.swing.SwingUtilities;

import se.jolind.jtvtracker.gui.MainFrame;

/*
 *  Class that starts the application gui and controller
 */

public class Application {
	
	private static Controller controller;
	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame view = new MainFrame();
				controller = new Controller(view);
				controller.createListnerHandler(controller);
			}
		});
	}
			
}
