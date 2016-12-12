package com.fri.studentskaprehrana;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by ziga on 18. 11. 2016.
 */

class StaticRestaurantVariables {
    // Jih spremeni uporabnik v NastavitveActivity ali pa jih naložimo na začetku iz datoteke, drugače booleani true in radius 10

    static boolean lunch = true;
    static boolean saladBar = true;
    static boolean vegetarian = true;
    static boolean disabled = true;
    static boolean disabledWC = true;
    static boolean pizzas = true;
    static boolean weekends = true;
    static boolean studentBenefits = true;
    static boolean delivery = true;

    static boolean customLocation = false;

    static boolean userLocationJustSelected = false;

    // Ker v onPlaceSelected() ne moremo delati z mapo ob customLocation pribliza nanjo
    static boolean customLocationJustSelected = false;

    static LinkedList<Restaurant> restaurants = new LinkedList<>();

    static Location mRestaurantSearchLocation;
    static LatLng mRestaurantLatLng;
    static String mSelectedPlaceName;
    static double radius = 10;
    static final double minRadius = 5;

    static void setLocaation(String name, double lat, double lon) {
        mRestaurantSearchLocation = new Location(name);
        mRestaurantSearchLocation.setLatitude(lat);
        mRestaurantSearchLocation.setLongitude(lon);

        mRestaurantLatLng = new LatLng(mRestaurantSearchLocation.getLatitude(),
                mRestaurantSearchLocation.getLongitude());

        mSelectedPlaceName = name;
    }
}

