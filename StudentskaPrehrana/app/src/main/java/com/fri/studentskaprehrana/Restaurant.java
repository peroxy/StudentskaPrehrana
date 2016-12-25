package com.fri.studentskaprehrana;

import android.location.Location;

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
    }

    public Restaurant(JSONObject json) {

        this.name = JsonHelper.getStringFromJSON(json, "Name");
        this.address = JsonHelper.getStringFromJSON(json, "Address");
        this.phone = JsonHelper.getStringFromJSON(json, "Phone");
        this.price = JsonHelper.getDoubleFromJSON(json, "Price");

        this.xcoord = JsonHelper.getDoubleFromJSON(json, "CoordinateX");
        this.ycoord = JsonHelper.getDoubleFromJSON(json, "CoordinateY");

        this.menu = Menu.getMenuItems(JsonHelper.getJSONArrayFromJSON(json, "Menu"));
        this.servesLunch = JsonHelper.getBooleanFromJSON(json, "ServesLunch");

        this.hasDelivery = JsonHelper.getBooleanFromJSON(json, "HasDelivery");
        this.hasDisabledSupport = JsonHelper.getBooleanFromJSON(json, "HasDisabledSupport");
        this.hasDisabledWcSupport = JsonHelper.getBooleanFromJSON(json, "HasDisabledWcSupport");
        this.hasSaladBar = JsonHelper.getBooleanFromJSON(json, "HasSaladBar");
        this.hasStudentBenefits = JsonHelper.getBooleanFromJSON(json, "HasStudentBenefits");
        this.hasVegetarianSupport = JsonHelper.getBooleanFromJSON(json, "HasVegetarianSupport");

        this.servesPizzas = JsonHelper.getBooleanFromJSON(json, "ServesPizzas");
        this.servesFastFood = JsonHelper.getBooleanFromJSON(json, "ServesFastFood");

        this.openDuringWeekends = JsonHelper.getBooleanFromJSON(json, "OpenDuringWeekends");
        this.openingTime = new OpeningTime(JsonHelper.getJSONObjectFromJSON(json, "OpeningTime"));
    }

    @Override
    public String toString() {
        return String.format("%s, %s", this.name, this.address);
    }

    public float getDistance(LatLng userLocation) {
        Location restaurantLoc = new Location("Restaurant location");
        restaurantLoc.setLatitude(this.xcoord);
        restaurantLoc.setLongitude(this.ycoord);

        Location userLoc = new Location("User location");
        userLoc.setLatitude(userLocation.latitude);
        userLoc.setLongitude(userLocation.longitude);

        return userLoc.distanceTo(restaurantLoc);
    }

    public float getDistanceInKm(LatLng userLocation) {
        return this.getDistance(userLocation)/1000;
    }

}
