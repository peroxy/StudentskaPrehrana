package com.fri.studentskaprehrana;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fri.studentskaprehrana.utils.RequestHandler;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListViewRestaurants extends AppCompatActivity  implements RequestHandler {
    ArrayList<Restaurant> listItems;
    ArrayAdapter<Restaurant> adapter;
    public Restaurant randomRestaurantWithName(String n, String a, String ph){ //za lepšo preglednost lahko naključno nastavimo vse razen imena, naslova in telefonske
        //function that generates a random restaurant, hard-coding is boring
        Random r = new Random();
        //double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        float pr=6 * r.nextFloat();
        double x = 13.5 + (16.5 - 13.5) * r.nextDouble();
        double y = 45.5 + (47.5 - 45.5) * r.nextDouble();
        boolean sl=Math.random()<0.5;
        boolean hsb=Math.random()<0.5;
        boolean hvs=Math.random()<0.5;
        boolean hds=Math.random()<0.5;
        boolean hdwc=Math.random()<0.5;
        boolean sp=Math.random()<0.5;
        boolean ow=Math.random()<0.5;
        boolean sff=Math.random()<0.5;
        boolean htsb=Math.random()<0.5;
        boolean hd=Math.random()<0.5;
        return new Restaurant(n,a,ph,pr,x,y,sl,hsb,hvs,hds,hdwc,sp,ow,sff,htsb,hd);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_restaurants);

        listItems=new ArrayList<Restaurant>();

        // Test
        StaticRestaurantVariables.mRestaurantLatLng = new LatLng(46.1422922, 14.4062103);
        RestaurantsModel.getRestaurants(this, "getRestaurants");
    }

    @Override
    public void handleResponse(final List<Restaurant> restaurants) {
//        listItems = new ArrayList<>(restaurants);
//        ListView lv = (ListView)findViewById(R.id.lv);
//        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, listItems);
//
//        for (int i = 0; i < restaurants.size(); i++) {
//            Log.d("Distance: ", String.format("%.1f", restaurants.get(i).getDistanceInKm(StaticRestaurantVariables.mRestaurantLatLng)));
//        }
//
//        lv.setAdapter(adapter);
//        lv.setTextFilterEnabled(true);
//        adapter.notifyDataSetChanged();
//
//        lv.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Restaurant send = (Restaurant)adapter.getItem(position);
//                Intent intent = new Intent(ListViewRestaurants.this, DetailedViewRestaurant.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("value", send);
//                Log.e("Res",send.toString());
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.activity_list_view_restaurants,
                null, false);
        LinearLayout list = ((LinearLayout) parent.findViewById(R.id.restaurantsList));

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
