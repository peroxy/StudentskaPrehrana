package com.fri.studentskaprehrana;

import java.io.Serializable;

/**
 * Created by kerry on 30. 11. 2016.
 */
/* TO DO:
Create classes for Menu and both times
 */
class Menu implements Serializable{
    public String Dessert;
    public String MainCourse;
    public String Salad;
    public String Soup;
    Menu(String des, String mc, String sal, String sou){
        this.Dessert=des;
        this.MainCourse=mc;
        this.Salad=sal;
        this.Soup=sou;
    }
    @Override
    public String toString(){
        return String.format("Main course: %s\nDessert: %s\nSalad: %s\nSoup: %s",this.MainCourse,this.Dessert,this.Salad,this.Soup);
    }
}
public class Restaurant implements Serializable
{
    public String Name;
    public String Address;
    public String Phone;
    public float Price;
    public float CoordinateX;
    public float CoordinateY;
    /*public OpeningTime OpeningTime;
    public DateTime UpdatedOn;*/
    public Menu Menu;
    public boolean ServesLunch;
    public boolean HasSaladBar;
    public boolean HasVegetarianSupport;
    public boolean HasDisabledSupport;
    public boolean HasDisabledWcSupport;
    public boolean ServesPizzas;
    public boolean OpenDuringWeekends;
    public boolean ServesFastFood;
    public boolean HasStudentBenefits;
    public boolean HasDelivery;
    Restaurant(String n, String a, String ph, float pr, float cx, float cy, boolean sl, boolean hsb, boolean hvs, boolean hds, boolean hdwc, boolean sp, boolean ow,
               boolean sff, boolean hstb, boolean hd){ //vse je po vrsti za čim manj confusiona
        this.Name=n;
        this.Address=a;
        this.Phone=ph;
        this.Price=pr;
        this.CoordinateX=cx;
        this.CoordinateY=cy;
        this.ServesLunch=sl;
        this.HasSaladBar=hsb;
        this.HasVegetarianSupport=hvs;
        this.HasDisabledSupport=hds;
        this.HasDisabledWcSupport=hdwc;
        this.ServesPizzas=sp;
        this.OpenDuringWeekends=ow;
        this.ServesFastFood=sff;
        this.HasStudentBenefits=hstb;
        this.HasDelivery=hd;
    }
    @Override
    public String toString(){
        return String.format("%s, %s",this.Name,this.Address);
    }
    public void initializeMenu(String des, String mc, String sal, String sou){
        this.Menu=new Menu(des,mc,sal,sou);
    }
}
