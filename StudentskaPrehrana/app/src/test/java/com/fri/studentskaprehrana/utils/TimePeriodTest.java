package com.fri.studentskaprehrana.utils;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by domengaber on 10/12/2016.
 */
public class TimePeriodTest {
    TimePeriod time;
    String jsonString;
    Time opening, closing;
    JSONObject json;

    @Before
    public void setUp() throws Exception {
        time = new TimePeriod();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void initWithJSONTestOnlyHours() throws Exception {
        jsonString = "{\"From\":\"PT10H\",\"To\":\"PT20H\"}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        opening = time.getOpeningTime();
        closing = time.getClosingTime();
        assertEquals(opening.getHour(), 10);
        assertEquals(opening.getMinutes(), 0);
        assertEquals(closing.getHour(), 20);
        assertEquals(closing.getMinutes(), 0);
        assertEquals(time.toString(), "Od 10.00 do 20.00");
    }

    @Test
    public void initWithJSONTestWithMinutes() throws Exception {
        jsonString = "{\"From\":\"PT10H30M\",\"To\":\"PT20H40M\"}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        opening = time.getOpeningTime();
        closing = time.getClosingTime();
        assertEquals(opening.getHour(), 10);
        assertEquals(opening.getMinutes(), 30);
        assertEquals(closing.getHour(), 20);
        assertEquals(closing.getMinutes(), 40);
        assertEquals(time.toString(), "Od 10.30 do 20.40");
    }

    @Test
    public void initWithJSONTestWithOneNull() throws Exception {
        jsonString = "{\"From\":\"PT10H\",\"To\":\"null\"}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        opening = time.getOpeningTime();
        closing = time.getClosingTime();
        assertEquals(opening.getHour(), 10);
        assertEquals(opening.getMinutes(), 0);
        assertEquals(closing.getHour(), -1);
        assertEquals(closing.getMinutes(), 0);
        assertEquals(time.toString(), time.error);
    }

    @Test
    public void initWithJSONTestWithInvalidValue() throws Exception {
        jsonString = "{\"From\":\"PT25H\",\"To\":\"20H\"}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        opening = time.getOpeningTime();
        closing = time.getClosingTime();
        assertEquals(opening.getHour(), 1);
        assertEquals(opening.getMinutes(), 0);
        assertEquals(closing.getHour(), 20);
        assertEquals(closing.getMinutes(), 0);
        assertEquals(time.toString(), "Od 01.00 do 20.00");
    }

    @Test
    public void initWithJSONTestWithEmptyJSON() throws Exception {
        jsonString = "{}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        opening = time.getOpeningTime();
        closing = time.getClosingTime();
        assertEquals(opening.getHour(), -1);
        assertEquals(opening.getMinutes(), 0);
        assertEquals(closing.getHour(), -1);
        assertEquals(closing.getMinutes(), 0);
        assertEquals(time.toString(), time.error);
    }

    @Test
    public void openTimeLeft() throws Exception {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        jsonString = String.format("{\"From\":\"PT%dH\",\"To\":\"PT%dH%dM\"}", hour, hour, minute + 1);
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        String openLeft = time.openTimeLeft();
        assertTrue(openLeft.matches("Odprto še: (.*)"));

        jsonString = String.format("{\"From\":\"PT%dH%dM\",\"To\":\"PT%dH%dM\"}", hour, minute - 2, hour, minute -1);
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        openLeft = time.openTimeLeft();
        assertEquals("Zaprto", openLeft);

        jsonString = "{\"From\":\"null\",\"To\":\"20H\"}";
        json = new JSONObject(jsonString);
        time = new TimePeriod(json);
        openLeft = time.openTimeLeft();
        assertEquals(time.error, openLeft);
    }

}