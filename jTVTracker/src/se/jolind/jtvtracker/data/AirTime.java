package se.jolind.jtvtracker.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AirTime {

	private ZonedDateTime origDateTime;
	private LocalDateTime localDateTime;

	public AirTime() {

	}

	public AirTime(String origTime, String origDate, String timeZone) {
		// Parse input strings.
		String[] dateArrayString = origDate.split("-");
		String[] timeArrayString = origTime.split(":");
		int[] dateArray = new int[3];
		int[] timeArray = new int[2];
		for (int i = 0; i < 3; i++) {
			dateArray[i] = Integer.parseInt(dateArrayString[i]);
		}
		for (int i = 0; i < 2; i++) {
			timeArray[i] = Integer.parseInt(timeArrayString[i]);
		}
		// Create instant from the date/time with timezone
		ZoneId origZone = ZoneId.of(timeZone);
		origDateTime = ZonedDateTime.of(dateArray[0], dateArray[1], dateArray[2], timeArray[0], timeArray[1], 0, 0,
				origZone);
		Instant absoluteTime = origDateTime.toInstant();
	}

	public void getLocalTime() {

	}

	public void getLocalDate() {
		// Convert zoned date to local date and return
	}

	public void getZonedTime() {
		// Get zoned time in proper object
	}

	public void getZonedDate() {
		// Get zoned date in proper object
	}

	public String getLocalTimeAsString() {
		// Convert zoned time to localtime and return as string
		return "";
	}

	public String getLocalDateAsString() {
		// Convert zoned date to local date and return as string
		return "";
	}

	public String getZonedTimeAsString() {
		// Get zoned time as string
		return "";
	}

	public String getZonedDateAsString() {
		// Get zoned date as string
		return "";
	}

	private void convertIndata() {
		// Convert indata to proper format for operations
	}
}
