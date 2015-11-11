package se.jolind.jtvtracker.gui.interfaces;

public interface IProgress {
	
	/*
	 * Interface for progressbar handling
	 */

	public void initProgressBar(int startValue, int endValue);
	public void increaseProgressBar();
}
