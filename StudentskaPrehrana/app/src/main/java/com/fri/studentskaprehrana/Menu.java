package com.fri.studentskaprehrana;

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
    Menu(String des, String mc, String sal, String sou){
        this.dessert=des;
        this.mainCourse=mc;
        this.salad=sal;
        this.soup=sou;
    }
    @Override
    public String toString(){
        return String.format("Main course: %s\nDessert: %s\nSalad: %s\nSoup: %s",this.mainCourse,this.dessert,this.salad,this.soup);
    }
}