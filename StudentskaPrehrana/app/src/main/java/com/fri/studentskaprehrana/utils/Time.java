package com.fri.studentskaprehrana.utils;

import java.io.Serializable;

/**
 * Created by domengaber on 10/12/2016.
 */

public class Time implements Serializable {
    private int hour, minutes;

    public Time(int hour, int minutes) {
        this.setHour(hour);
        this.setMinutes(minutes);
    }

    private void setHour(int hour) {
        if (hour < -1)
            hour = -1;
        this.hour = hour % 24;
    }

    private void setMinutes(int minutes) {
        if (minutes > 59 || minutes < 0)
            minutes = 0;
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        if (this.hour < 0)
            return "Ni podatka";
        return String.format("%02d.%02d", this.hour, this.minutes);
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    /**
     * Compare two times
     * @param t1
     * @param t2
     * @return int | value 1 if t1 is later than t2 | value -1 if t2 is bigger than t2 | 0 if t1 and t2 are same
     */
    public static int compareTimes(Time t1, Time t2) {
        if (t1.hour == t2.hour)
            if (t1.minutes > t2.minutes)
                return 1;
            else if (t1.minutes < t2.minutes)
                return -1;
            else
                return 0;
        else if (t1.hour > t2.hour)
            return 1;
        else
            return -1;
    }

    /**
     * Returns time in minutes
     * @param t
     * @return
     */
    public static int convertToMinutes(Time t) {
        if (t.getHour() < 0)
            return 0;
        return t.getHour() * 60 + t.getMinutes();
    }

    /**
     * Transforms minutes and creates Time object
     * @param minutes
     * @return
     */
    public static Time convertToTime(int minutes) {
        if (minutes < 0)
            return new Time(-1, 0);
        return new Time(minutes / 60, minutes % 60);
    }

    /**
     * Calculates time difference in between given times
     * @param t1 Time from
     * @param t2 Time to
     * @return Time Calculated difference in hours and minutes
     */
    public static Time differenceBetweenTimes(Time t1, Time t2) {
        if (t1.getHour() < 0 || t2.getHour() < 0)
            return new Time(-1, 0);
        return convertToTime(Math.abs(Time.convertToMinutes(t1) - Time.convertToMinutes(t2)));
    }
}
