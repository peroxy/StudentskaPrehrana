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
        TextView tvName = (TextView)findViewById(R.id.tvName);
        TextView tvAddress = (TextView)findViewById(R.id.tvAddress);
        TextView tvPhoneNumber = (TextView)findViewById(R.id.tvPhone);
        TextView tvMenu = (TextView)findViewById(R.id.tvMenu);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        //if(bundle!=null) {
        Restaurant received = (Restaurant) bundle.getSerializable("value");
        Log.e("Received:", received.toString());
        tvName.setText(received.Name);
        tvAddress.setText(received.Address);
        tvPhoneNumber.setText(received.Phone);
        tvMenu.setText(received.Menu.toString());
        //}
    }
}
