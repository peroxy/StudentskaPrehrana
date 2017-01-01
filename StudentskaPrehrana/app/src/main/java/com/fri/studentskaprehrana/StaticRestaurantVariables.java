package com.fri.studentskaprehrana;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by ziga on 18. 11. 2016.
 */

class StaticRestaurantVariables {
    // Jih spremeni uporabnik v NastavitveActivity ali pa jih naložimo na začetku iz datoteke, drugače booleani true in radius 10

    static boolean lunch = false;
    static boolean saladBar = false;
    static boolean vegetarian = false;
    static boolean disabled = false;
    static boolean disabledWC = false;
    static boolean pizzas = false;
    static boolean weekends = false;
    static boolean studentBenefits = false;
    static boolean delivery = false;

    static boolean customLocation = false;

    static boolean userLocationJustSelected = false;

    // Ker v onPlaceSelected() ne moremo delati z mapo ob customLocation pribliza nanjo
    static boolean customLocationJustSelected = true;

    static LinkedList<Restaurant> restaurants = new LinkedList<>();

    static Location mRestaurantSearchLocation;
    static Location mRestaurantChangeLocation;
    static LatLng mRestaurantLatLng;
    static String mSelectedPlaceName;
    static double radius = 10;
    static final double minRadius = 1;
    static final double maxRadius = 50;

    static void setLocation(String name, double lat, double lon) {
        mRestaurantSearchLocation = new Location(name);
        mRestaurantSearchLocation.setLatitude(lat);
        mRestaurantSearchLocation.setLongitude(lon);

        mRestaurantLatLng = new LatLng(mRestaurantSearchLocation.getLatitude(),
                mRestaurantSearchLocation.getLongitude());

        mSelectedPlaceName = name;
    }
}

