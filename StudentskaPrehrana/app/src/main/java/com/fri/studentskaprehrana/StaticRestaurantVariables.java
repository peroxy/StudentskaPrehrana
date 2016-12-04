package com.fri.studentskaprehrana;

import android.location.Location;

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

    static LinkedList<Restaurant> restaurants = new LinkedList<>();

    static Location mRestaurantSearchLocation;
    static double radius = 10;
}

