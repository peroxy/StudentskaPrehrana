package com.fri.studentskaprehrana;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fri.studentskaprehrana.utils.JsonHelper;
import com.fri.studentskaprehrana.utils.RequestHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by domengaber on 10/12/2016.
 */

public class RestaurantsModel {
    private static final String urlService = "http://bonrest.azurewebsites.net/BonRest.svc/";
    private static final String urlInRadius = urlService + "InRadius";

    public static void getRestaurants(final RequestHandler handler, final String tag) {
        double locationLatitude = StaticRestaurantVariables.mRestaurantLatLng.latitude;
        double locationLongitude = StaticRestaurantVariables.mRestaurantLatLng.longitude;
        double radius = StaticRestaurantVariables.radius;

        String requestUrl = String.format(Locale.US, "%s/%f/%f/%f", urlInRadius, locationLatitude, locationLongitude, radius);
        Log.d("Request url:", requestUrl);

        JSONObject cachedData = RestaurantsModel.checkCacheForData(requestUrl);

        if (cachedData == null) {
            final ProgressDialog pDialog = new ProgressDialog((AppCompatActivity) handler);
            pDialog.setMessage("Pridobivanje podatkov o restavracijah.");
            pDialog.show();

            // Making request and setup of callbacks
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    requestUrl, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
//                            Log.d("Response recived ", response.toString());
                            RestaurantsModel.handleResponse(response, handler);
                            pDialog.hide();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(tag, "Error: " + error.getMessage());
                            Log.d("Volley error", error.toString());
                            pDialog.hide();
                        }
                    }
            );

            // Set request retry policy to prevent timeout
            jsonObjReq.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 5000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag);
        } else {
            Log.d("Loading cached: ", "True");
            RestaurantsModel.handleResponse(cachedData, handler);
        }
    }

    private static JSONObject checkCacheForData(String url) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        JSONObject data = null;

        if(entry != null){
            try {
                data = JsonHelper.getJsonObjectFromString(new String(entry.data, "UTF-8"));
                // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static void handleResponse(JSONObject response, RequestHandler handler) {
        JSONArray restaurantsArray;

        try {
            restaurantsArray = response.getJSONArray("Values");
        } catch (Exception e) {
            restaurantsArray = new JSONArray();
        }

        handler.handleResponse(getRestaurantsFromJson(restaurantsArray));
    }

    public static void cancelAllRequest() {
        AppController.getInstance().cancelPendingRequests(AppController.TAG);
        Log.d("Canceling request: ", AppController.TAG);
    }

    private static List<Restaurant> getRestaurantsFromJson(JSONArray json) {
        List<Restaurant> restaurants = new LinkedList<>();
        Restaurant currentRestaurant;

        for (int i = 0; i < json.length(); i++) {
            try {
                currentRestaurant = new Restaurant(json.getJSONObject(i));

                if (correspondsToFilters(currentRestaurant))
                    restaurants.add(currentRestaurant);

            } catch (Exception e) {}
        }

        return restaurants;
    }

    private static boolean correspondsToFilters(Restaurant r) {
        boolean lunch = StaticRestaurantVariables.lunch ? r.servesLunch == StaticRestaurantVariables.lunch : true;
        boolean salad = StaticRestaurantVariables.saladBar ? r.hasSaladBar == StaticRestaurantVariables.saladBar : true;
        boolean veget = StaticRestaurantVariables.vegetarian ? r.hasVegetarianSupport == StaticRestaurantVariables.vegetarian : true;
        boolean disab = StaticRestaurantVariables.disabled ? r.hasDisabledSupport == StaticRestaurantVariables.disabled : true;
        boolean disWS = StaticRestaurantVariables.disabledWC ? r.hasDisabledWcSupport == StaticRestaurantVariables.disabledWC : true;
        boolean pizza = StaticRestaurantVariables.pizzas ? r.servesPizzas == StaticRestaurantVariables.pizzas : true;
        boolean weekend = StaticRestaurantVariables.weekends ? r.openDuringWeekends == StaticRestaurantVariables.weekends : true;
        boolean benefits = StaticRestaurantVariables.studentBenefits ? r.hasStudentBenefits == StaticRestaurantVariables.studentBenefits : true;
        boolean delivery = StaticRestaurantVariables.delivery ? r.hasDelivery == StaticRestaurantVariables.delivery : true;

        return lunch && salad && veget && disab && disWS && pizza && weekend && benefits && delivery;
    }
}
