package com.fri.studentskaprehrana.utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by domengaber on 10/12/2016.
 */

public class TimePeriod implements Serializable {
    private Time openingTime;
    private Time closingTime;
    public final String error;

    public TimePeriod() {
        this.openingTime = new Time(-1, 0);
        this.closingTime = new Time(-1, 0);
        this.error = "Ni podatka o obratovalnem času";
    }

    public TimePeriod(JSONObject openingTimes) {
        this();
        String openingTime = null, closingTime = null;

        // Read opening and closing time from JSON object
        try {
            openingTime = openingTimes.getString("From");
            closingTime = openingTimes.getString("To");
        } catch (Exception e) {}

        // If opening or close time is null use default values
        if (openingTime == null || closingTime == null)
            return;

        // Get array with int values for hour and minute
        int[] opening = getHourAndMinutesFromStupidFormat(openingTime);
        int[] closing = getHourAndMinutesFromStupidFormat(closingTime);

        this.openingTime = new Time(opening[0], opening[1]);
        this.closingTime = new Time(closing[0], closing[1]);
    }

    /**
     * Reparse stupid time format string to int values
     * @param s String in stupid format (ex. PT11H30M)
     * @return int[] Array of length 2 where index 0 of array represents hour, index 1 represents minute
     */
    private int[] getHourAndMinutesFromStupidFormat(String s) {
        int[] timePeriodInt = new int[2];
        timePeriodInt[0] = -1;
        timePeriodInt[1] = 0;

        if (s.equals("null"))
            return timePeriodInt;

        String[] timeString = s.replace("PT", "").replace("M", "").split("H");

        switch (timeString.length) {
            case 2:
                timePeriodInt[1] = Integer.parseInt(timeString[1]);
            case 1:
                timePeriodInt[0] = Integer.parseInt(timeString[0]);
                break;
        }

        return timePeriodInt;
    }

    /**
     * Time period to string. If one side of period is unknown returns "Ni podatka"
     * @return String Time period. Ex. "Od HH:MM do HH:MM"
     */
    @Override
    public String toString() {
        if (this.openingTime.getHour() < 0 || this.closingTime.getHour() < 0)
            return this.error;
        return String.format("Od %s do %s", this.openingTime.toString(), this.closingTime.toString());
    }

    /**
     * Returns time as String if it's still open, else returns "Zaprto"
     * @return
     */
    public String openTimeLeft() {
        // If time is not known
        if (!this.isDefined())
            return this.error;

        Calendar calendar = Calendar.getInstance();
        Time now = new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        String result;

        // Check if current time is between opening and closing time
        if (Time.compareTimes(now, openingTime) > -1 && Time.compareTimes(now, closingTime) < 1)
            return String.format("Odprto še: %s", Time.differenceBetweenTimes(now, closingTime).toString());
        else
            return "Zaprto";
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public boolean isDefined() {
        return this.openingTime.getHour() >= 0 && this.closingTime.getHour() >= 0;
    }
}
