package com.fri.studentskaprehrana;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by kerry on 30. 11. 2016.
 */

public class Restaurant implements Serializable {
    protected String name;
    protected String address;
    protected String phone;
    protected double price;
    protected double xcoord;
    protected double ycoord;
    protected OpeningTime openingTime;
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
    protected JSONObject json;

    public Restaurant(String name, String address, String phone, double price,
               double xcoord, double ycoord,
               boolean servesLunch, boolean hasSaladBar,
               boolean hasVegetarianSupport, boolean hasDisabledSupport, boolean hasDisabledWcSupport,
               boolean servesPizzas, boolean openDuringWeekends, boolean servesFastFood, boolean hasStudentBenefits, boolean hasDelivery) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.servesLunch = servesLunch;
        this.hasSaladBar = hasSaladBar;
        this.hasVegetarianSupport = hasVegetarianSupport;
        this.hasDisabledSupport = hasDisabledSupport;
        this.hasDisabledWcSupport = hasDisabledWcSupport;
        this.servesPizzas = servesPizzas;
        this.openDuringWeekends = openDuringWeekends;
        this.servesFastFood = servesFastFood;
        this.hasStudentBenefits = hasStudentBenefits;
        this.hasDelivery = hasDelivery;
        this.json = null;
    }

    public Restaurant(JSONObject json) {
        this.json = json;

        this.name = getStringFromJSON("Name");
        this.address = getStringFromJSON("Address");
        this.phone = getStringFromJSON("Phone");
        this.price = getDoubleFromJSON("Price");

        this.xcoord = getDoubleFromJSON("CoordinateX");
        this.ycoord = getDoubleFromJSON("CoordinateY");

        this.menu = new Menu(getJSONObjectFromJSON("Menu"));
        this.servesLunch = getBooleanFromJSON("ServesLunch");

        this.hasDelivery = getBooleanFromJSON("HasDelivery");
        this.hasDisabledSupport = getBooleanFromJSON("HasDisabledSupport");
        this.hasDisabledWcSupport = getBooleanFromJSON("HasDisabledWcSupport");
        this.hasSaladBar = getBooleanFromJSON("HasSaladBar");
        this.hasStudentBenefits = getBooleanFromJSON("HasStudentBenefits");
        this.hasVegetarianSupport = getBooleanFromJSON("HasVegetarianSupport");

        this.servesPizzas = getBooleanFromJSON("ServesPizzas");
        this.servesFastFood = getBooleanFromJSON("ServesFastFood");

        this.openDuringWeekends = getBooleanFromJSON("OpenDuringWeekends");
        this.openingTime = new OpeningTime(getJSONObjectFromJSON("OpeningTime"));
    }

    private String getStringFromJSON(String name) {
        String value = null;

        try {
            value = this.json.getString(name);
        } catch(Exception e) {}

        return value;
    }

    private Double getDoubleFromJSON(String name) {
        Double value = null;

        try {
            value = this.json.getDouble(name);
        } catch(Exception e) {}

        return value;
    }

    private JSONObject getJSONObjectFromJSON(String name) {
        JSONObject value = null;

        try {
            value = this.json.getJSONObject(name);
        } catch(Exception e) {
            value = new JSONObject();
        }

        return value;
    }

    private boolean getBooleanFromJSON(String name) {
        boolean value = false;

        try {
            value = this.json.getBoolean(name);
        } catch(Exception e) {}

        return value;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", this.name, this.address);
    }

    public String getJSONString() {
        return this.json.toString();
    }

    protected void initializeMenu(String des, String mc, String sal, String sou) {
        this.menu = new Menu(des, mc, sal, sou);
    }


}
