package com.fri.studentskaprehrana;

import com.fri.studentskaprehrana.utils.Time;
import com.fri.studentskaprehrana.utils.TimePeriod;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by domengaber on 10/12/2016.
 */

public class OpeningTime {
    private TimePeriod weekDay, saturday, sunday;

    public OpeningTime() {
        this.weekDay = new TimePeriod();
        this.saturday = new TimePeriod();
        this.sunday = new TimePeriod();
    }

    public OpeningTime(JSONObject times) {
        this();

        try {
            this.weekDay = new TimePeriod(times.getJSONObject("Week"));
        } catch (Exception e) {}

        try {
            this.saturday = new TimePeriod(times.getJSONObject("Saturday"));
        } catch (Exception e) {}

        try {
            this.sunday = new TimePeriod(times.getJSONObject("Sunday"));
        } catch (Exception e) {}
    }

    public TimePeriod getTodayOpeningTime() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if (day > 1 && day < 7)
            return this.weekDay;
        else if (day == 1)
            return sunday;
        else
            return saturday;
    }

    public String getOpenedTimeLeft() {
        return this.getTodayOpeningTime().openTimeLeft();
    }

    public TimePeriod getWeekDay() {
        return weekDay;
    }

    public TimePeriod getSaturday() {
        return saturday;
    }

    public TimePeriod getSunday() {
        return sunday;
    }
}
