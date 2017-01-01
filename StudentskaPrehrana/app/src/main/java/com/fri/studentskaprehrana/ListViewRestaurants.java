package com.fri.studentskaprehrana;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fri.studentskaprehrana.utils.RequestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewRestaurants extends AppCompatActivity  implements RequestHandler {
    ArrayList<Restaurant> listItems;
    ArrayAdapter<Restaurant> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_restaurants);

        listItems=new ArrayList<Restaurant>();

        if (StaticRestaurantVariables.mRestaurantLatLng != null) {
            RestaurantsModel.getRestaurants(this, "getRestaurants");
        } else {
            Toast.makeText(this, "Lokacija ni izbrana", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void handleResponse(final List<Restaurant> restaurants) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.activity_list_view_restaurants,
                null, false);
        LinearLayout list = ((LinearLayout) parent.findViewById(R.id.restaurantsList));

        Collections.sort(restaurants);
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant currentRes = restaurants.get(i);
            View custom = inflater.inflate(R.layout.restaurantlistitem, null, false);
            LinearLayout restaurantItem = (LinearLayout) custom.findViewById(R.id.restaurantItem);

            if(restaurantItem.getParent()!=null)
                ((ViewGroup)restaurantItem.getParent()).removeView(restaurantItem);


            ((TextView)restaurantItem.findViewById(R.id.tv_restaurantName)).setText(currentRes.name);
            ((TextView)restaurantItem.findViewById(R.id.tv_restaurantLocation)).setText("Naslov: " + currentRes.address);
            ((TextView)restaurantItem.findViewById(R.id.tv_restaurantDistance)).setText(String.format("Oddaljenost: %.1fkm", currentRes.getDistanceInKm(StaticRestaurantVariables.mRestaurantLatLng)));
            ((TextView)restaurantItem.findViewById(R.id.restaurant_number)).setText(Integer.toString(i));
            ((TextView)restaurantItem.findViewById(R.id.tv_openedTimeLeft)).setText(currentRes.openingTime.getOpenedTimeLeft());

            restaurantItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resNumber = Integer.parseInt((String) ((TextView)v.findViewById(R.id.restaurant_number)).getText());
                    Restaurant toSend = (Restaurant)restaurants.get(resNumber);
                    Intent intent = new Intent(ListViewRestaurants.this, DetailedViewRestaurant.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("value", toSend);
                    Log.e("Res",toSend.toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            list.addView(restaurantItem);
        }

        setContentView(parent);
    }
}
