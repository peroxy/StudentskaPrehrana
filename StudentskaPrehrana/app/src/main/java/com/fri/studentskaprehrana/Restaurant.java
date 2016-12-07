package com.fri.studentskaprehrana;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by kerry on 30. 11. 2016.
 */

public class Restaurant implements Serializable
{
    protected String name;
    protected String address;
    protected String phone;
    protected double price;
    protected double xcoord;
    protected double ycoord;
    /*protected OpeningTime OpeningTime;
    protected DateTime UpdatedOn;*/
    protected Menu menu;
    protected boolean servesLunch;
    protected boolean hasSaladBar;
    protected boolean hasVegetarianSupport;
    protected boolean hasDisabledSupport;
    protected boolean hasDisabledWcSupport;
    protected boolean servesPizzas;
    protected boolean openDuringWeekends;
    protected boolean servesFastFood;
    protected boolean hasStudentBenefits;
    protected boolean hasDelivery;
    Restaurant(String n, String a, String ph, double pr, double x, double y, boolean sl, boolean hsb, boolean hvs, boolean hds, boolean hdwc, boolean sp, boolean ow,
               boolean sff, boolean hstb, boolean hd){ //vse je po vrsti za čim manj confusiona
        this.name=n;
        this.address=a;
        this.phone=ph;
        this.price=pr;
        this.xcoord = x;
        this.ycoord = y;
        this.servesLunch=sl;
        this.hasSaladBar=hsb;
        this.hasVegetarianSupport=hvs;
        this.hasDisabledSupport=hds;
        this.hasDisabledWcSupport=hdwc;
        this.servesPizzas=sp;
        this.openDuringWeekends=ow;
        this.servesFastFood=sff;
        this.hasStudentBenefits=hstb;
        this.hasDelivery=hd;
    }
    @Override
    public String toString(){
        return String.format("%s, %s",this.name,this.address);
    }
    protected void initializeMenu(String des, String mc, String sal, String sou){
        this.menu=new Menu(des,mc,sal,sou);
    }
}
