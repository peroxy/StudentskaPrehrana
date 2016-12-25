package com.fri.studentskaprehrana;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailedViewRestaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            Restaurant restaurant = (Restaurant) bundle.getSerializable("value");
            Menu menu = restaurant.menu[0];
            Menu[] menus = restaurant.menu;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.activity_detailed_view_restaurant,
                    null, false);

            TextView tvName = (TextView) parent.findViewById(R.id.tv_name);
            TextView tvAddress = (TextView) parent.findViewById(R.id.tv_address);
            TextView tvPhoneNumber = (TextView) parent.findViewById(R.id.tv_phone);

            tvName.setText(restaurant.name != null && !restaurant.name.equals("null") ? restaurant.name : "Ni podatka");
            tvAddress.setText(restaurant.address != null && !restaurant.address.equals("null") ? restaurant.address : "Ni podatka");
            tvPhoneNumber.setText(restaurant.phone != null && !restaurant.phone.equals("null") ? restaurant.phone : "Ni podatka");

            for (int i = 0; i < menus.length; i++) {
                Menu currentItem = menus[i];
                View custom = inflater.inflate(R.layout.menuitem, null, false);
                LinearLayout menuItem = (LinearLayout) custom.findViewById(R.id.menuItem);

                if(menuItem.getParent()!=null)
                    ((ViewGroup)menuItem.getParent()).removeView(menuItem);

                ((TextView)menuItem.findViewById(R.id.tv_menuNumber)).setText(String.format("Meni %d", i+1));
                ((TextView)menuItem.findViewById(R.id.tv_soup)).setText(currentItem.soup);
                ((TextView)menuItem.findViewById(R.id.tv_salad)).setText(currentItem.salad);
                ((TextView)menuItem.findViewById(R.id.tv_mainCourse)).setText(currentItem.mainCourse);
                ((TextView)menuItem.findViewById(R.id.tv_dessert)).setText(currentItem.dessert);

                ((LinearLayout) parent.findViewById(R.id.listMenuItems)).addView(menuItem);
            }

            setContentView(parent);
        }
    }
}
