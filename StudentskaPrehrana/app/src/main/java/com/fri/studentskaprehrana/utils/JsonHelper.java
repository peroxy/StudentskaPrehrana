package com.fri.studentskaprehrana.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by domengaber on 18/12/2016.
 */

public class JsonHelper {

    public static String getStringFromJSON(JSONObject json, String name) {
        String value = null;

        try {
            value = json.getString(name);
        } catch(Exception e) {}

        return value;
    }

    public static Double getDoubleFromJSON(JSONObject json, String name) {
        Double value = null;

        try {
            value = json.getDouble(name);
        } catch(Exception e) {}

        return value;
    }

    public static JSONObject getJSONObjectFromJSON(JSONObject json, String name) {
        JSONObject value = null;

        try {
            value = json.getJSONObject(name);
        } catch(Exception e) {
            value = new JSONObject();
        }

        return value;
    }

    public static JSONArray getJSONArrayFromJSON(JSONObject json, String name) {
        JSONArray value = null;

        try {
            value = json.getJSONArray(name);
        } catch(Exception e) {
            value = new JSONArray();
        }

        return value;
    }

    public static boolean getBooleanFromJSON(JSONObject json, String name) {
        boolean value = false;

        try {
            value = json.getBoolean(name);
        } catch(Exception e) {}

        return value;
    }
}
