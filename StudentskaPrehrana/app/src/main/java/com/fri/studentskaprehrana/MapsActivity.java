package com.fri.studentskaprehrana;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final LatLng LOWER_WEST_BOUND = new LatLng(45.427882, 13.347119);
    private final LatLng UPPER_EAST_BOUND = new LatLng(46.904552, 16.664808);
    private final LatLng LJUBLJANA = new LatLng(46.052771, 14.503602);
    private final LatLngBounds SLOVENIJA_OUTER_BOUNDS = new LatLngBounds(LOWER_WEST_BOUND, UPPER_EAST_BOUND);

    private boolean lunch;
    private boolean saladBar;
    private boolean vegetarian;
    private boolean disabled;
    private boolean disabledWC;
    private boolean pizzas;
    private boolean weekends;
    private boolean studentBenefits;
    private boolean delivery;

    private double radius;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initVariables();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setLatLngBoundsForCameraTarget(SLOVENIJA_OUTER_BOUNDS);
        mMap.addMarker(new MarkerOptions().position(LJUBLJANA).title("Marker in Ljubljana"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LJUBLJANA, 15));
    }

    private void initVariables() {
        lunch = false;
        saladBar = false;
        vegetarian = false;
        disabled = false;
        disabledWC = false;
        pizzas = false;
        weekends = false;
        studentBenefits = false;
        delivery = false;

        radius = 10;
    }
}
