package se.jolind.jtvtracker.data.tvmaze;

import javax.swing.ImageIcon;

/*
 * Class to store basic information on a show based for search result
 * display. Only used by the search part of the program.
 */

public class TvmShortShow {

	private int id;
	private String name, summary;
	private ImageIcon icon;

	public TvmShortShow(int id, String name, String summary, ImageIcon icon) {
		this.id = id;
		this.name = name;
		this.summary = summary;
		this.icon = icon;
	}

	public int getId() {
		/*
		 * Returns show id
		 */
		return id;
	}

	public String getInfo() {
		/*
		 * Returns HTML formatted information about instance for display in search results gui
		 */
		String NEWLINE = "<BR>";
		String BOLD = "<B>";
		String ENDBOLD = "</B>";
		String info = "<HTML>"
				+ BOLD + getName() + ENDBOLD + " (id: " + getId() + ")" + NEWLINE + NEWLINE
				+ getSummary() + "</HTML>";
		return info;
	}
	
	private String getName() {
		/*
		 * Returns the name value
		 */
		return name;
	}
	
	public ImageIcon getIcon() {
		/*
		 * Returns the ImageIcon
		 */
		return icon;
	}

	private String getSummary() {
		/*
		 * Returns a summary for display with a max size of 80 characters.
		 */
		String returnSummary = "";
		if (summary.length() > 80) {
			returnSummary = summary.substring(0, 80) + "...";
		} else if (summary.length() == 0) {
			returnSummary = "No summary.";
		} else {
			returnSummary = summary;
		}
		return returnSummary;
	}

	@Override
	public String toString() {
		return "Show: " + getName() + " with id: " + getId() + " summary: " + getSummary();
	}
}
