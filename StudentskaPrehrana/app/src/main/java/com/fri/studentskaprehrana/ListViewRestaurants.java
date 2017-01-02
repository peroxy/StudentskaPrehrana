package com.fri.studentskaprehrana;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void nastavitveClick(MenuItem item) {
        Intent intent = new Intent(this, NastavitveActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_restaurants);
        setTitle("Seznam restavracij");

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
            ((ImageView) restaurantItem.findViewById(R.id.iv_status)).setImageResource(!currentRes.openingTime.getOpenedTimeLeft().equals("Zaprto") ? R.drawable.ic_open : R.drawable.ic_closed);

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
