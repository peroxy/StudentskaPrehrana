package com.fri.studentskaprehrana;

import com.fri.studentskaprehrana.utils.JsonHelper;
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
    protected Menu[] menu;
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

        this.name = JsonHelper.getStringFromJSON(this.json, "Name");
        this.address = JsonHelper.getStringFromJSON(this.json, "Address");
        this.phone = JsonHelper.getStringFromJSON(this.json, "Phone");
        this.price = JsonHelper.getDoubleFromJSON(this.json, "Price");

        this.xcoord = JsonHelper.getDoubleFromJSON(this.json, "CoordinateX");
        this.ycoord = JsonHelper.getDoubleFromJSON(this.json, "CoordinateY");

        this.menu = Menu.getMenuItems(JsonHelper.getJSONArrayFromJSON(this.json, "Menu"));
        this.servesLunch = JsonHelper.getBooleanFromJSON(this.json, "ServesLunch");

        this.hasDelivery = JsonHelper.getBooleanFromJSON(this.json, "HasDelivery");
        this.hasDisabledSupport = JsonHelper.getBooleanFromJSON(this.json, "HasDisabledSupport");
        this.hasDisabledWcSupport = JsonHelper.getBooleanFromJSON(this.json, "HasDisabledWcSupport");
        this.hasSaladBar = JsonHelper.getBooleanFromJSON(this.json, "HasSaladBar");
        this.hasStudentBenefits = JsonHelper.getBooleanFromJSON(this.json, "HasStudentBenefits");
        this.hasVegetarianSupport = JsonHelper.getBooleanFromJSON(this.json, "HasVegetarianSupport");

        this.servesPizzas = JsonHelper.getBooleanFromJSON(this.json, "ServesPizzas");
        this.servesFastFood = JsonHelper.getBooleanFromJSON(this.json, "ServesFastFood");

        this.openDuringWeekends = JsonHelper.getBooleanFromJSON(this.json, "OpenDuringWeekends");
        this.openingTime = new OpeningTime(JsonHelper.getJSONObjectFromJSON(this.json, "OpeningTime"));
    }

    @Override
    public String toString() {
        return String.format("%s, %s", this.name, this.address);
    }

    public String getJSONString() {
        return this.json.toString();
    }

}
