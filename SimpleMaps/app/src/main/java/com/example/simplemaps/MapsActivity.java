package com.example.simplemaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000; // The desired interval for location updates.
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2; // The fastest rate for active location updates.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 21; // Code used in requesting runtime permissions.
    private GoogleMap mMap;

    // UI Widgets
    private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
    private TextView mLastUpdateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    // Labels
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;

    private Boolean mRequestingLocationUpdates; // Tracks the status of the location updates request.
    private LocationCallback mLocationCallback; // Callback for Location events.
    private LocationRequest mLocationRequest; // Stores parameters for requests to the FusedLocationProviderApi.
    private FusedLocationProviderClient mFusedLocationClient; // Provides access to the Fused Location Provider API.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Locate the UI widgets
        mStartUpdatesButton = findViewById(R.id.start_updates_button);
        mStopUpdatesButton = findViewById(R.id.stop_updates_button);
        mLatitudeTextView = findViewById(R.id.latitude_text);
        mLongitudeTextView = findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = findViewById(R.id.last_update_time_text);

        // Set labels
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationRequest();
        createLocationCallback();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    // Sets up the location request.
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS).setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Creates a callback for receiving location events.
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            updateUI(locationResult.getLastLocation());
            }
        };
    }

    // Requests location updates from the FusedLocationApi.
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            // Error still occurred even after adding ACCESS_FINE_LOCATION permissions in AndroidManifest.xml
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    updateUI(location);
                }
            });
        }

        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    // Removes location updates from the FusedLocationApi.
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // Handles the Start Updates button and requests start of location updates.
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    // Handles the Stop Updates button, and requests removal of location updates.
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ITS = new LatLng(-7.2819705,112.795323);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 15));
    }

    // Sets the value of the UI fields for the location latitude, longitude and last update time.
    private void updateLocationUI(Location location) {
        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel, location.getLatitude()));
        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel, location.getLongitude()));
        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel, DateFormat.getTimeInstance().format(new Date())));
    }

    // Disables both buttons when functionality is disabled due to insufficient location settings.
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        }

        else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    // Updates all UI fields.
    private void updateUI(@NonNull Location location) {
        if (location == null) {
            Toast.makeText(this, "Cannot get location at this moment", Toast.LENGTH_SHORT).show();
        }

        else {
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(myLoc).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
            setButtonsEnabledState();
            updateLocationUI(location);
        }
    }
}