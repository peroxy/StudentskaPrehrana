package com.fri.studentskaprehrana;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class NastavitveActivity extends AppCompatActivity {
    public ToggleButton tbLunch;
    public ToggleButton tbSaladBar;
    public ToggleButton tbVegetarian;
    public ToggleButton tbDisabled;
    public ToggleButton tbDisabledWC;
    public ToggleButton tbPizzas;
    public ToggleButton tbWeekends;
    public ToggleButton tbStudentBenefits;
    public ToggleButton tbDelivery;

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

        this.tbLunch.setChecked(true);
        this.tbSaladBar.setChecked(true);
        this.tbVegetarian.setChecked(true);
        this.tbDisabled.setChecked(true);
        this.tbDisabledWC.setChecked(true);
        this.tbPizzas.setChecked(true);
        this.tbWeekends.setChecked(true);
        this.tbStudentBenefits.setChecked(true);
        this.tbDelivery.setChecked(true);

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

    }

}
