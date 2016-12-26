package com.fri.studentskaprehrana;

import android.text.TextUtils;

import com.fri.studentskaprehrana.utils.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ziga on 4. 12. 2016.
 */

public class Menu implements Serializable {
    protected String menu;

    public Menu(String menu){
        this.menu = menu;
    }

    public Menu(JSONObject json) {
        this.menu = "";
        String[] items = {
                JsonHelper.getStringFromJSON(json, "MainCourse").toLowerCase(),
                JsonHelper.getStringFromJSON(json, "Soup").toLowerCase(),
                JsonHelper.getStringFromJSON(json, "Salad").toLowerCase(),
                JsonHelper.getStringFromJSON(json, "Dessert").toLowerCase()
        };

        ArrayList<String> menuItems = new ArrayList<>();

        for (String item : items) {
            if (item != null && !item.equals("null"))
                menuItems.add(capitalize(item));
        }

        if (menuItems.size() > 0)
            this.menu = "• " + TextUtils.join("\n• ", menuItems);
    }


    private String capitalize(String s) {
        s = s.toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    @Override
    public String toString(){
        return this.menu;
    }

    public static Menu[] getMenuItems(JSONArray json) {
        Menu[] menuItems = new Menu[json.length()];

        for (int i = 0; i < json.length(); i++) {
            try {
                menuItems[i] = new Menu(json.getJSONObject(i));
            } catch (JSONException e) {
            }
        }

        return menuItems;
    }
}