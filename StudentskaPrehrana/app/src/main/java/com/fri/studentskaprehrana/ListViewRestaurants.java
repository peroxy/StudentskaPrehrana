package com.fri.studentskaprehrana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

public class ListViewRestaurants extends AppCompatActivity {
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
        ListView lv = (ListView)findViewById(R.id.lv);
        adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        String[] imena={"Marjetica", "Indeks", "McDonalds", "Pub Legende", "Tinetov Hram Zadovoljstva"};
        String[] ulica={"Tobačna 2", "Kersnikova 19", "Kolodvorska 22", "Tržaška 17", "Trubarjeva 67"};
        for(int i=0;i<5;i++){
            Restaurant temp = randomRestaurantWithName(imena[i],ulica[i],"01 675 667 7");
            temp.initializeMenu("Puding", "Dunajski in pomfri", "zelena solata s prelivom", "Goveja");
            listItems.add(temp);
        }
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Restaurant send = (Restaurant)adapter.getItem(position);
                //Log.e("TAG",send.phone);
                Intent intent = new Intent(ListViewRestaurants.this, DetailedViewRestaurant.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", send);
                Log.e("Res",send.toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
