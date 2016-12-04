package com.fri.studentskaprehrana;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.TreeSet;

import static com.fri.studentskaprehrana.R.id.map;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        PlaceSelectionListener{
    private final LatLng LOWER_WEST_BOUND = new LatLng(45.427882, 13.347119);
    private final LatLng UPPER_EAST_BOUND = new LatLng(46.904552, 16.664808);
    private final LatLng LJUBLJANA = new LatLng(46.052771, 14.503602);
    private final LatLng GEOSS = new LatLng(46.120319, 14.815608);
    private final LatLngBounds SLOVENIJA_OUTER_BOUNDS = new LatLngBounds(LOWER_WEST_BOUND, UPPER_EAST_BOUND);

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;

    private final int MY_PERMISSIONS_ALL_GRANTED = 7;

    private boolean mShowPermissionDeniedDialog;
    private boolean customLocation;
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

    private Marker currentMarker;
    private TreeSet<LatLng> listOfPoints;

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

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
        mMap.clear();
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
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
        permissions = new String[1];
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        mLocationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        if (noGPSMode) {
            Toast.makeText(this, "Izberite lokacijo v iskalniku", Toast.LENGTH_SHORT).show();
        }

        mShowPermissionDeniedDialog = false;
        customLocation = false;
        listOfPoints = new TreeSet<>();
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
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastLocationUpdate = new Date();


        if (StaticRestaurantVariables.mRestaurantSearchLocation == null) {
            StaticRestaurantVariables.mRestaurantSearchLocation = mCurrentLocation;
        }
        else if (!customLocation && StaticRestaurantVariables.mRestaurantSearchLocation.distanceTo(mCurrentLocation) > 10) {
            StaticRestaurantVariables.mRestaurantSearchLocation = mCurrentLocation;
            zoomToLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
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
                    //TODO: Any custom actions
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
        StaticRestaurantVariables.mRestaurantSearchLocation = new Location(place.getAddress().toString());
        StaticRestaurantVariables.mRestaurantSearchLocation.setLatitude(place.getLatLng().latitude);
        StaticRestaurantVariables.mRestaurantSearchLocation.setLongitude(place.getLatLng().longitude);
        customLocation = true;
        if (mMap != null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
        }
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(MapsActivity.this, "Napaka iskanja", Toast.LENGTH_SHORT).show();
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
