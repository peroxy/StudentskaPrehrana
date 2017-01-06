package com.fri.studentskaprehrana;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fri.studentskaprehrana.utils.RequestHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.fri.studentskaprehrana.R.id.map;
import static com.fri.studentskaprehrana.StaticRestaurantVariables.customLocationJustSelected;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        PlaceSelectionListener, RequestHandler,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.InfoWindowAdapter {
    private final LatLng LOWER_WEST_BOUND = new LatLng(45.427882, 13.347119);
    private final LatLng UPPER_EAST_BOUND = new LatLng(46.904552, 16.664808);
    private final LatLng LJUBLJANA = new LatLng(46.052771, 14.503602);
    private final LatLng GEOSS = new LatLng(46.120319, 14.815608);
    private final LatLngBounds SLOVENIJA_OUTER_BOUNDS = new LatLngBounds(LOWER_WEST_BOUND, UPPER_EAST_BOUND);

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;

    private final int circleColor = Color.rgb(66, 133, 244);

    private final int MY_PERMISSIONS_ALL_GRANTED = 7;

    private boolean mShowPermissionDeniedDialog;
    private boolean noGPSMode;

    private String[] permissions;

    private PlaceAutocompleteFragment autocompleteFragment;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private java.util.Date mLastLocationUpdate;

    private UiSettings mUiSettings;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private View popup = null;

    private Marker currentMarker;
    private Circle circle;
    private LinkedList<Marker> markers;
    private LinkedList<Restaurant> restaurants;

    // Dodaj menu gumb katerega opis se nahaja v res/menu/more_tab_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initVariables();
        //Log.d("onCreate: ", "MapsActivity");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        Location x = new Location("daffd");
        x.setLatitude(LOWER_WEST_BOUND.longitude);
        x.setLongitude(LOWER_WEST_BOUND.longitude);

        Location y = new Location("daffd");
        y.setLatitude(UPPER_EAST_BOUND.latitude);
        y.setLongitude(UPPER_EAST_BOUND.longitude);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, ListViewRestaurants.class);
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();

//        to enable gps enable message on start uncmment this
//
//        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
//
//        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
//            buildAlertMessageNoGps();
//        }
    }

    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Za delovanje aplikacije je potrebna povezava s spletom")
                .setCancelable(false)
                .setTitle("Povezava onemogočena")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        noGPSMode = true;
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Toast.makeText(MapsActivity.this, "Brez povezave aplikacija ne deluje", Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Za boljše delovanje aplikacije so potrebne lokacijske storitve")
                .setCancelable(false)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        noGPSMode = false;
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        noGPSMode = true;
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (mMap != null) {
            mMap.clear();
        }

        clearMarkers();
//
//        mMap.clear();
//
//        try {
//            // Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
//            FileOutputStream output = openFileOutput("latlngpoints.txt",
//                    Context.MODE_PRIVATE);
//            DataOutputStream dout = new DataOutputStream(output);
//            dout.writeInt(listOfPoints.size()); // Save line count
//            for (LatLng point : listOfPoints) {
//                dout.writeUTF(point.latitude + "," + point.longitude);
//                Log.v("write", point.latitude + "," + point.longitude);
//            }
//            dout.flush(); // Flush stream ...
//            dout.close(); // ... and close.
//        } catch (IOException exc) {
//            exc.printStackTrace();
//        }
    }

//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }

        StaticRestaurantVariables.customLocationJustSelected = true;

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        if (StaticRestaurantVariables.mRestaurantSearchLocation != null) {
            RestaurantsModel.getRestaurants(this, "getRestaurants");
        }

//
//        try {
//            FileInputStream input = openFileInput("latlngpoints.txt");
//            DataInputStream din = new DataInputStream(input);
//            int sz = din.readInt(); // Read line count
//            for (int i = 0; i < sz; i++) {
//                String str = din.readUTF();
//                Log.v("read", str);
//                String[] stringArray = str.split(",");
//                double latitude = Double.parseDouble(stringArray[0]);
//                double longitude = Double.parseDouble(stringArray[1]);
//                listOfPoints.add(new LatLng(latitude, longitude));
//            }
//            din.close();
//            loadMarkers(listOfPoints);
//        } catch (IOException exc) {
//            exc.printStackTrace();
//        }
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GEOSS, 7.5f));
        mMap.setMapType(MAP_TYPE_NORMAL);
        // omeji kamero na slovenijo
        mMap.setLatLngBoundsForCameraTarget(SLOVENIJA_OUTER_BOUNDS);
        mUiSettings = mMap.getUiSettings();

        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            buildGoogleApiClient();
            enableLocation();

        } else {
            //Request Location Permission
            checkLocationPermission();
        }



//        updateMyLocation();

//        updateUI();
    }


//    private void updateMyLocation() {
//        if (!checkReady()) {
//            return;
//        }
//
//        // Enable the location layer. Request the location permission if needed.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mGoogleApiClient == null) {
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//                        .addApi(LocationServices.API)
//                        .build();
//            }
//            mMap.setMyLocationEnabled(true);
//        } else {
//            checkLocationPermission();
//        }
//    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("Za boljšo izkušnjo aplikacija potrebuje dostop do lokacije naprave")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this, permissions,
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "Map not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != MY_PERMISSIONS_REQUEST_LOCATION) {
            Toast.makeText(this, "Wrong permission request code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED && mMap != null) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                enableLocation();
            }
            enableLocation();
        } else {
            mShowPermissionDeniedDialog = true;
        }
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    // pojdi v nastavitve ob kliku na gumb
    public void nastavitveClick(MenuItem item) {
        Intent intent = new Intent(this, NastavitveActivity.class);
        startActivity(intent);
    }

    // Nastavi default vrednosti
    private void initVariables() {
        markers = new LinkedList<>();
        restaurants = new LinkedList<>();

        permissions = new String[1];
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(SLOVENIJA_OUTER_BOUNDS);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        mLocationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        if (!isNetworkAvailable()) {
            buildAlertMessageNoInternet();
        }
        if (noGPSMode) {
            Toast.makeText(this, "Izberite lokacijo v iskalniku", Toast.LENGTH_SHORT).show();
        }

        mShowPermissionDeniedDialog = false;
        //customLocation = false;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            StaticRestaurantVariables.mRestaurantSearchLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
//
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
//                        builder.build());
//
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
//            return;
//        }
//
//        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//
//        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastLocationUpdate = new Date();

//        for(int i=0;i<restavracije.size();i++){
//            Restaurant tmp = restavracije.get(i);
//            LatLng tmpLL = new LatLng(tmp.xcoord, tmp.ycoord);
//            Marker tempMarker = mMap.addMarker(new MarkerOptions()
//                    .position(tmpLL)
//                    .title(tmp.name));
//            tempMarker.setTag(0);
//        }

        if (StaticRestaurantVariables.mRestaurantSearchLocation == null) {
            StaticRestaurantVariables.mRestaurantChangeLocation = location;
        }


        if (!StaticRestaurantVariables.customLocation && mMap != null) {
            StaticRestaurantVariables.setLocation(location.toString(), location.getLatitude(),
                    location.getLongitude());

            if (currentMarker != null ) {
                currentMarker.remove();
            }
            if (circle != null) {
                circle.remove();
            }

            circle = mMap.addCircle(new CircleOptions()
                    .center(StaticRestaurantVariables.mRestaurantLatLng)
                    .radius(StaticRestaurantVariables.radius * 1000)
                    .strokeColor(circleColor)
                    .strokeWidth(4f));

            // Request new restaurants on update or when location change is 400m or more
            if (customLocationJustSelected ||
                    StaticRestaurantVariables.mRestaurantChangeLocation
                            .distanceTo(StaticRestaurantVariables.mRestaurantSearchLocation) > 400) {
                clearMarkers();
                RestaurantsModel.getRestaurants(this, "getRestaurants");

                StaticRestaurantVariables.mRestaurantChangeLocation = location;
                customLocationJustSelected = false;
            }
        }
        else if (StaticRestaurantVariables.customLocation && mMap != null) {
            if (circle != null) {
                circle.remove();
            }
            if (currentMarker != null) {
                currentMarker.remove();
            }

            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(StaticRestaurantVariables.mRestaurantLatLng)
                    .title(StaticRestaurantVariables.mSelectedPlaceName));
            currentMarker.setTag(0);

            circle = mMap.addCircle(new CircleOptions()
                    .center(StaticRestaurantVariables.mRestaurantLatLng)
                    .radius(StaticRestaurantVariables.radius * 1000)
                    .strokeColor(circleColor)
                    .strokeWidth(4f));

            if (customLocationJustSelected) {
                zoomToLocation(StaticRestaurantVariables.mRestaurantLatLng);
                customLocationJustSelected = false;
            }
        }

//        else if (customLocation && currentMarker != null && mMap != null &&
//                !currentMarker.getPosition().equals(new LatLng(StaticRestaurantVariables.mRestaurantSearchLocation.getLatitude(),
//                        StaticRestaurantVariables.mRestaurantSearchLocation.getLongitude()))) {
//            currentMarker.remove();
//            currentMarker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(StaticRestaurantVariables.mRestaurantSearchLocation.getLatitude(),
//                            StaticRestaurantVariables.mRestaurantSearchLocation.getLongitude()))
//                    .title(StaticRestaurantVariables.mRestaurantSearchLocation.toString()));
//        }

//        updateMyLocation();

//        updateUI();
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
            mUiSettings.setRotateGesturesEnabled(true);
            mUiSettings.setZoomControlsEnabled(true);
            mMap.setMinZoomPreference(7.5f);

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                @Override
                public boolean onMyLocationButtonClick()
                {
                    StaticRestaurantVariables.customLocation = false;
                    customLocationJustSelected = true;
                    onLocationChanged(StaticRestaurantVariables.mRestaurantSearchLocation);

                    return false;
                }
            });
        }
    }

    private void zoomToLocation(LatLng loc) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        StaticRestaurantVariables.setLocation(place.getName().toString(), place.getLatLng().latitude,
                place.getLatLng().longitude);

        StaticRestaurantVariables.customLocation = true;
        customLocationJustSelected = true;

        onLocationChanged(StaticRestaurantVariables.mRestaurantSearchLocation);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void clearMarkers() {
        for (Marker m : markers) {
            m.remove();
        }

        markers = new LinkedList<>();
        restaurants = new LinkedList<>();
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(MapsActivity.this, "Napaka iskanja", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleResponse(List<Restaurant> restaurants) {
        Log.d("handleResponse", "happened");

        for(Iterator<Restaurant> r = restaurants.iterator(); mMap != null && r.hasNext(); ) {
            Restaurant rest = r.next();
            Marker tmpMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(rest.xcoord, rest.ycoord))
                    .title(rest.name)
                    .snippet(String.format("Odprto še:%s\nCena: %.2f",
                            rest.openingTime.getOpenedTimeLeft(),
                            rest.price)));
            tmpMarker.setTag(0);

            markers.add(tmpMarker);
        }

        this.restaurants = (LinkedList<Restaurant>) restaurants;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d("onInfoWindowClick", "happened");

        for (int i = 0; i < markers.size(); i++) {
            if (marker.equals(markers.get(i))) {
                Intent intent = new Intent(MapsActivity.this, DetailedViewRestaurant.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", (Restaurant) restaurants.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = getLayoutInflater().inflate(R.layout.popup, null);
        }

        TextView tv = (TextView)popup.findViewById(R.id.title);

        tv.setText(marker.getTitle());
        tv = (TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());

        return popup;
    }

//    private void updateUI() {
//        TextView tv = (TextView) findViewById(R.id.activity_maps_tv_test);
//
//        if (mCurrentLocation != null && tmpMarker != null) {
//            tv.setText(String.format("Lat: %f\nLng:%f", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
//
//            tmpMarker.remove();
//
//            tmpMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).title("Marker in Ljubljana"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15));
//        }
//    }
}