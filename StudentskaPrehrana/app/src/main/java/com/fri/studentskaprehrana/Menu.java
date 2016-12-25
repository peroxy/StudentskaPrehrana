package com.fri.studentskaprehrana;

import com.fri.studentskaprehrana.utils.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ziga on 4. 12. 2016.
 */

/* TO DO:
Create classes for Menu and both times
 */

public class Menu implements Serializable {
    protected String dessert;
    protected String mainCourse;
    protected String salad;
    protected String soup;

    public Menu(String des, String mc, String sal, String sou){
        this.dessert = des == null || des == "null" ? "Ni podatka" : capitalize(des);
        this.mainCourse = mc == null || mc == "null" ? "Ni podatka" : capitalize(mc);
        this.salad = sal == null || sal == "null" ? "Ni podatka" : capitalize(sal);
        this.soup = sou == null || sou == "null" ? "Ni podatka" : capitalize(sou);
    }

    public Menu(JSONObject json) {
        this(JsonHelper.getStringFromJSON(json, "Dessert"),
             JsonHelper.getStringFromJSON(json, "MainCourse"),
             JsonHelper.getStringFromJSON(json, "Salad"),
             JsonHelper.getStringFromJSON(json, "Soup"));
    }


    private String capitalize(String s) {
        s = s.toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    @Override
    public String toString(){
        return String.format("Main course: %s\nDessert: %s\nSalad: %s\nSoup: %s",this.mainCourse,this.dessert,this.salad,this.soup);
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