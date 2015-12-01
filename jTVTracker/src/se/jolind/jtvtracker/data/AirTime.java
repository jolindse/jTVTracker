package se.jolind.jtvtracker.data;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/*
 * Class to handle date and time values stored in show and episode
 */
public class AirTime {


	private ZonedDateTime origDateTime, localDateTime;
	private Instant absoluteTime;

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
		TimeZone localTimeZone = Calendar.getInstance().getTimeZone(); 
		ZoneId origZone = ZoneId.of(timeZone);
		origDateTime = ZonedDateTime.of(dateArray[0], dateArray[1], dateArray[2], timeArray[0], timeArray[1], 0, 0,
				origZone);
		// Create instant for use with system timezone
		absoluteTime = origDateTime.toInstant();
		// Make local time
		ZoneId localZone = ZoneId.of(localTimeZone.getDisplayName(false, TimeZone.SHORT));
		localDateTime = ZonedDateTime.ofInstant(absoluteTime, localZone);
		
	}

	public ZonedDateTime getLocalTime() {
		/*
		 * Returns the time value as local time.
		 */
		return localDateTime;
	}
	
	public ZonedDateTime getZonedTime() {
		/*
		 * Returns the time value from original timezone.
		 */
		return origDateTime;
		
	}
	
	public String getYear(){
		/*
		 * Returns the year
		 */
		return Integer.toString(origDateTime.getYear());
	}
	
	public String getLocalDay(){
		/*
		 * Returns day as string
		 */
		String dayString = "";
		DayOfWeek day = localDateTime.getDayOfWeek();
		switch (day){
		 	case MONDAY:
		 		dayString = "Monday";
		 		break;
		 	case TUESDAY:
		 		dayString = "Tuesday";
		 		break;
		 	case WEDNESDAY:
		 		dayString = "Wednesday";
		 		break;
		 	case THURSDAY:
		 		dayString = "Thursday";
		 		break;
		 	case FRIDAY:
		 		dayString = "Friday";
		 		break;
		 	case SATURDAY:
		 		dayString = "Saturday";
		 		break;
		 	case SUNDAY:
		 		dayString = "Sunday";
		 		break;
		 }
		return dayString;
	}
	
	public String getLocalTimeAsString() {
		/*
		 * Returns a formatted string representation of the local time
		 */
		int hour = localDateTime.getHour();
		int minute = localDateTime.getMinute();
		return String.format("%02d:%02d",hour,minute);
	}

	public String getLocalDateAsString() {
		/*
		 * Returns a formatted string representation of the local date
		 */
		int year = localDateTime.getYear();
		int month = localDateTime.getMonthValue();
		int day = localDateTime.getDayOfMonth();
		return String.format("%04d-%02d-%02d",year,month,day);
	}
	
	public String getZonedTimeAsString() {
		/*
		 * Returns the original time formatted as a string
		 */
		int zHour = origDateTime.getHour();
		int zMinute = origDateTime.getMinute();
		return String.format("%02d:%02d",zHour,zMinute);
	}

	public String getZonedDateAsString() {
		/*
		 * Returns the original date formatted as a string
		 */		
		int zYear = origDateTime.getYear();
		int zMonth = origDateTime.getMonthValue();
		int zDay = origDateTime.getDayOfMonth();
		return String.format("%04d-%02d-%02d", zYear, zMonth, zDay);
	}
	
	public Instant getInstant(){
		/*
		 * Returns the original instant
		 */
		return absoluteTime;
	}
	
	public long getLongInstant(){
		return absoluteTime.toEpochMilli();
	}
	
}
