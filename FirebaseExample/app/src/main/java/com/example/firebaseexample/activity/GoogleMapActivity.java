package com.example.firebaseexample.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseexample.Instance;
import com.example.firebaseexample.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private Marker mMarker;
    private GoogleMap mGoogleMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private float DEFAULT_ZOOM = 17f;
    LocationManager locationManager;
    @BindView(R.id.start_place)
    EditText edtStartPlace;
    @BindView(R.id.end_place)
    EditText edtEndPlace;
    private static final int REQUEST_CODE_RETURN = 1022;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(this);
        checkPermission();
    }

    @OnClick(R.id.linear_end_place)
    public void getEndPlace(){
        Intent intent = new Intent(GoogleMapActivity.this, SearchPlaceActivity.class);
        intent.putExtra("LAT_CURRENT", Instance.lat);
        intent.putExtra("LNG_CURRENT", Instance.lng);
        startActivityForResult(intent, REQUEST_CODE_RETURN);
        Log.d("searchNewPlace", "getEndPlace: " + Instance.lat + Instance.lng);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_RETURN){
            edtEndPlace.setText(data.getStringExtra("PlaceSelected"));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * draw marker
     *
     * @param mMap:     google map
     * @param location: location
     * @param zoom:     zoom map
     */
    public void moveCameraOnMap(GoogleMap mMap, LatLng location, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        Geocoder myLocation = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = myLocation.getFromLocation(location.latitude, location.longitude, 1);
            Address address = addressList.get(0);
            Instance.lat = address.getLatitude();
            Instance.lng = address.getLongitude();
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(address.getAddressLine(0))
            );
            edtStartPlace.setText(address.getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Instance.location = location;
        moveCameraOnMap(mGoogleMap, new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(GoogleMapActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }
}
