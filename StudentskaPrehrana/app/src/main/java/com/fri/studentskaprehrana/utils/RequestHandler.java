package com.fri.studentskaprehrana.utils;

import com.fri.studentskaprehrana.Restaurant;

import java.util.List;

/**
 * Created by domengaber on 17/12/2016.
 */

public interface RequestHandler {
    void handleResponse(List<Restaurant> restaurants);
}
