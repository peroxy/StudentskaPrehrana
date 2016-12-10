package com.fri.studentskaprehrana;

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
        this.dessert = des;
        this.mainCourse = mc;
        this.salad = sal;
        this.soup = sou;
    }

    public Menu(JSONObject json) {
        try {
            this.dessert = json.getString("Dessert");
            this.mainCourse = json.getString("MainCourse");
            this.salad = json.getString("Salad");
            this.soup = json.getString("Soup");
        } catch (Exception e) {
            this.dessert = null;
            this.mainCourse = null;
            this.salad = null;
            this.soup = null;
        }
    }

    @Override
    public String toString(){
        return String.format("Main course: %s\nDessert: %s\nSalad: %s\nSoup: %s",this.mainCourse,this.dessert,this.salad,this.soup);
    }
}