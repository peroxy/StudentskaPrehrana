package com.fri.studentskaprehrana.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by domengaber on 10/12/2016.
 */
public class TimeTest {
    @Test
    public void compareTimes() throws Exception {
        Time t1 = new Time(12, 29);
        Time t2 = new Time(12, 30);
        Time t3 = new Time(12, 30);
        Time t4 = new Time(25, 0);
        Time t5 = new Time(-1, 0);

        assertEquals(-1, Time.compareTimes(t1, t2));
        assertEquals(1, Time.compareTimes(t2, t1));
        assertEquals(0, Time.compareTimes(t2, t3));
        assertEquals(-1, Time.compareTimes(t4, t1));
        assertEquals(-1, Time.compareTimes(t5, t1));
        assertEquals(0, Time.compareTimes(t5, t5));
    }

    @Test
    public void convertToMinutes() throws Exception {
        int hour = 12, minutes = 29;
        assertEquals(hour*60 + minutes, Time.convertToMinutes(new Time(hour, minutes)));

        hour = 0;
        minutes = 20;
        assertEquals(20, Time.convertToMinutes(new Time(hour, minutes)));

        hour = -1;
        minutes = 20;
        assertEquals(0, Time.convertToMinutes(new Time(hour, minutes)));

        hour = 1;
        minutes = -20;
        assertEquals(60, Time.convertToMinutes(new Time(hour, minutes)));
    }

    @Test
    public void convertToTime() throws Exception {
        Time t = Time.convertToTime(12 * 60 + 40);
        assertEquals(12, t.getHour());
        assertEquals(40, t.getMinutes());

        t = Time.convertToTime(-80);
        assertEquals(-1, t.getHour());
        assertEquals(0, t.getMinutes());

        t = Time.convertToTime(24 * 60 + 1);
        assertEquals(0, t.getHour());
        assertEquals(1, t.getMinutes());
    }

    @Test
    public void differenceBetweenTimes() throws Exception {
        Time t1 = new Time(12, 30);
        Time t2 = new Time(14, 00);
        Time diff = Time.differenceBetweenTimes(t1, t2);

        assertEquals(1, diff.getHour());
        assertEquals(30, diff.getMinutes());

        diff = Time.differenceBetweenTimes(t2, t1);
        assertEquals(1, diff.getHour());
        assertEquals(30, diff.getMinutes());

        t1 = new Time(2, 30);
        t2 = new Time(-1, 0);
        diff = Time.differenceBetweenTimes(t2, t1);
        assertEquals(-1, diff.getHour());
        assertEquals(0, diff.getMinutes());
    }

    @Test
    public void toStringTest() throws Exception {
        Time t = new Time(10, 30);
        assertEquals("10.30", t.toString());

        t = new Time(1, 5);
        assertEquals("01.05", t.toString());

        t = new Time(-1, 5);
        assertEquals("Ni podatka", t.toString());

        t = new Time(25, 10);
        assertEquals("01.10", t.toString());

        t = new Time(1, 70);
        assertEquals("01.00", t.toString());
    }

}