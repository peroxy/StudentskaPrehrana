package com.fri.studentskaprehrana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fri.studentskaprehrana.utils.RequestHandler;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class NastavitveActivity extends AppCompatActivity implements RequestHandler {
    public ToggleButton tbLunch;
    public ToggleButton tbSaladBar;
    public ToggleButton tbVegetarian;
    public ToggleButton tbDisabled;
    public ToggleButton tbDisabledWC;
    public ToggleButton tbPizzas;
    public ToggleButton tbWeekends;
    public ToggleButton tbStudentBenefits;
    public ToggleButton tbDelivery;

    public Button btSeznam;

    public SeekBar sbRadius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastavitve);

        this.tbLunch = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_lunch);
        this.tbSaladBar = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_saladBar);
        this.tbVegetarian = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_vegetarian);
        this.tbDisabled = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_disabled);
        this.tbDisabledWC = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_disabledWC);
        this.tbPizzas = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_pizzas);
        this.tbWeekends = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_weekends);
        this.tbStudentBenefits = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_studentBenefits);
        this.tbDelivery = (ToggleButton) findViewById(R.id.activity_nastavitve_tb_delivery);

        this.sbRadius = (SeekBar) findViewById(R.id.activity_nastavitve_sb_radius);

        this.btSeznam = (Button) findViewById(R.id.btSeznam);

        this.tbLunch.setChecked(StaticRestaurantVariables.lunch);
        this.tbSaladBar.setChecked(StaticRestaurantVariables.saladBar);
        this.tbVegetarian.setChecked(StaticRestaurantVariables.vegetarian);
        this.tbDisabled.setChecked(StaticRestaurantVariables.disabled);
        this.tbDisabledWC.setChecked(StaticRestaurantVariables.disabledWC);
        this.tbPizzas.setChecked(StaticRestaurantVariables.pizzas);
        this.tbWeekends.setChecked(StaticRestaurantVariables.weekends);
        this.tbStudentBenefits.setChecked(StaticRestaurantVariables.studentBenefits);
        this.tbDelivery.setChecked(StaticRestaurantVariables.delivery);

        tbLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.lunch = isChecked;
            }
        });

        tbSaladBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.saladBar = isChecked;
            }
        });

        tbVegetarian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.vegetarian = isChecked;
            }
        });

        tbDisabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.disabled = isChecked;
            }
        });

        tbDisabledWC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.disabledWC = isChecked;
            }
        });

        tbPizzas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.pizzas = isChecked;
            }
        });

        tbWeekends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.weekends = isChecked;
            }
        });

        tbStudentBenefits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.studentBenefits = isChecked;
            }
        });

        tbDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StaticRestaurantVariables.delivery = isChecked;

            }
        });
        btSeznam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NastavitveActivity.this, ListViewRestaurants.class);
                startActivity(intent);
            }
        });

        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = (TextView) findViewById(R.id.textView);

                tv.setText(Integer.toString(progress + (int) StaticRestaurantVariables.minRadius));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Test
        StaticRestaurantVariables.mRestaurantLatLng = new LatLng(46.052771, 14.503602);
        RestaurantsModel.getRestaurants(this, "getRestaurants");
    }

    @Override
    public void handleResponse(List<Restaurant> restaurants) {
        Log.d("Response: ", restaurants.toString());
    }
}
