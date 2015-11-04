package se.jolind.jtvtracker.data.tvmaze;

import javax.swing.ImageIcon;

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
		return id;
	}

	public String getInfo() {
		String NEWLINE = "<BR>";
		String BOLD = "<B>";
		String ENDBOLD = "</B>";
		String info = "<HTML>"
				+ BOLD + getName() + ENDBOLD + " (id: " + getId() + ")" + NEWLINE + NEWLINE
				+ getSummary() + "</HTML>";
		return info;
	}
	
	private String getName() {
		return name;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}

	private String getSummary() {
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
