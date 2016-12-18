package com.fri.studentskaprehrana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailedViewRestaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view_restaurant);
        TextView tvName = (TextView)findViewById(R.id.tv_name);
        TextView tvAddress = (TextView)findViewById(R.id.tv_address);
        TextView tvPhoneNumber = (TextView)findViewById(R.id.tv_phone);
        TextView tvSoup = (TextView)findViewById(R.id.tv_soup);
        TextView tvSalad = (TextView)findViewById(R.id.tv_salad);
        TextView tvMainCourse = (TextView)findViewById(R.id.tv_mainCourse);
        TextView tvDessert = (TextView)findViewById(R.id.tv_dessert);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        //if(bundle!=null) {
        Restaurant received = (Restaurant) bundle.getSerializable("value");
        Log.e("Received:", received.toString());
        tvName.setText(received.name);
        tvAddress.setText(received.address);
        tvPhoneNumber.setText(received.phone);
        Menu menu = received.menu;
        tvSoup.setText(menu.soup);
        tvSalad.setText(menu.salad);
        tvMainCourse.setText(menu.mainCourse);
        tvDessert.setText(menu.dessert);
        //}
    }
}
