package com.fri.studentskaprehrana;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by domengaber on 10/12/2016.
 */
public class OpeningTimeTest {
    JSONObject json;
    OpeningTime oTime;
    String jsonString;

    @Before
    public void setUp() throws Exception {
        jsonString = "{\n" +
                "        \"Saturday\":{\n" +
                "            \"From\":\"PT11H30M\",\n" +
                "            \"To\":\"PT20H\"\n" +
                "        },\n" +
                "        \"Sunday\":{\n" +
                "            \"From\":\"PT11H30M\",\n" +
                "            \"To\":\"PT18H\"\n" +
                "        },\n" +
                "        \"Week\":{\n" +
                "            \"From\":\"PT11H\",\n" +
                "            \"To\":\"PT20H\"\n" +
                "        }\n" +
                "    }";
        json = new JSONObject(jsonString);
        oTime = new OpeningTime(json);
    }

    @Test
    public void getTodayOpeningTime() throws Exception {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if (day == 1)
            assertEquals("Od 11.30 do 18.00", oTime.getTodayOpeningTime().toString());
        else if (day == 7)
            assertEquals("Od 11.30 do 20.00", oTime.getTodayOpeningTime().toString());
        else
            assertEquals("Od 11.00 do 20.00", oTime.getTodayOpeningTime().toString());
    }

    @Test
    public void getOpenedTimeLeft() throws Exception {

    }

}