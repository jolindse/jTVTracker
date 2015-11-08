package se.jolind.jtvtracker.application;

import java.io.IOException;

import javax.swing.SwingUtilities;

import se.jolind.jtvtracker.data.ShowTest;
import se.jolind.jtvtracker.gui.MainFrame;

public class Application {
	
	private static Controller controller;
	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainFrame view = new MainFrame();
				controller = new Controller(view);		
			}
		});
	}
		
	public static Controller getListener(){
		return controller;
	}
}
