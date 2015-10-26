package se.jolind.jtvtracker.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class AirTime {

	private ZonedDateTime origDateTime, localDateTime;
	
	//private Instant absoluteTime;

	public AirTime() {

	}

	public AirTime(String origTime, String origDate, String timeZone) {
		// Parse input strings.
		System.out.println(origTime + " " + origDate + " " + timeZone); // TESTA
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
		TimeZone localTimeZone = Calendar.getInstance().getTimeZone(); 
		ZoneId origZone = ZoneId.of(timeZone);
		origDateTime = ZonedDateTime.of(dateArray[0], dateArray[1], dateArray[2], timeArray[0], timeArray[1], 0, 0,
				origZone);
		// Create instant for use with system timezone
		Instant absoluteTime = origDateTime.toInstant();
		// Make local time
		ZoneId localZone = ZoneId.of(localTimeZone.getDisplayName(false, TimeZone.SHORT));
		localDateTime = ZonedDateTime.ofInstant(absoluteTime, localZone);
		
	}

	public ZonedDateTime getLocalTime() {
		return localDateTime;
	}
	
	public ZonedDateTime getZonedTime() {
		return origDateTime;
		
	}
	
	public String getLocalTimeAsString() {
		int hour = localDateTime.getHour();
		int minute = localDateTime.getMinute();
		return String.format("%02d:%02d",hour,minute);
	}

	public String getLocalDateAsString() {
		// Convert zoned date to local date and return as string
		int year = localDateTime.getYear();
		int month = localDateTime.getMonthValue();
		int day = localDateTime.getDayOfMonth();
		return String.format("%04d-%02d-%02d",year,month,day);
	}

	public String getZonedTimeAsString() {
		// Return the time zone time as string
		int zHour = origDateTime.getHour();
		int zMinute = origDateTime.getMinute();
		return String.format("%02d:%02d",zHour,zMinute);
	}

	public String getZonedDateAsString() {
		// Get zoned date as string
		
		int zYear = origDateTime.getYear();
		int zMonth = origDateTime.getMonthValue();
		int zDay = origDateTime.getDayOfMonth();
		return String.format("%04d-%02d-%02d", zYear, zMonth, zDay);
	}

	private void convertIndata() {
		// Convert indata to proper format for operations
	}
}
