package com.example.firebaseexample.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.firebaseexample.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PinMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.txt_pin_name_first)
    TextView txtNameFisrt;
    @BindView(R.id.txt_pin_name_second)
    TextView txtNameSecond;
    @BindView(R.id.btn_pin_accept)
    Button btnAccept;

    private GoogleMap mGoogleMap;
    private Handler mHandler;
    private Runnable requestLocation;
    private Geocoder mGeocoder;
    private List<Address> address;
    private final int REQUEST_CODE_PERMISSION_LOCATION = 10;
    private Location mCurrLocation;
    private String mFirstName;
    private String mSecondName;
    private final String TAG = PinMapActivity.class.getSimpleName();
    private int idTextView;
    private String activity;
    private Location resultLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_map);

        ButterKnife.bind(this);
        idTextView = getIntent().getIntExtra("idTextView", -1);
        activity = getIntent().getStringExtra("fromActivity");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pin_map);


        supportMapFragment.getMapAsync(this);
        if (checkPermissionLocation()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_LOCATION);
        } else {
            mGeocoder = new Geocoder(this, Locale.getDefault());
            setUpListenLocation();
        }
        mHandler = new Handler();
    }

    @OnClick(R.id.ln_exp_nav)
    public void setLnNavOnClick() {
        setResult(Activity.RESULT_CANCELED);
        onBackPressed();
    }

    @OnClick(R.id.img_exp_icon_nav)
    public void setIconNavOnClick() {
        setResult(Activity.RESULT_CANCELED);
        onBackPressed();
    }

    @OnClick(R.id.btn_pin_accept)
    public void setBtnAcceptOnClick() {
        if (mGoogleMap != null) {
            if (resultLocation != null) {
                resultLocation.setLatitude(mGoogleMap.getCameraPosition().target.latitude);
                resultLocation.setLongitude(mGoogleMap.getCameraPosition().target.longitude);
                Intent intent = new Intent();
                intent.putExtra("fromActivity", PinMapActivity.class.getName());
                intent.putExtra("lat", resultLocation.getLatitude());
                intent.putExtra("lng", resultLocation.getLongitude());
                intent.putExtra("namePlace", txtNameFisrt.getText());
                intent.putExtra("idTextViewFrơmSearchPlace", idTextView);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Log.d(TAG, "setBtnAcceptOnClick: Map null");
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                requestLocation = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            address = mGeocoder.getFromLocation(mGoogleMap.getCameraPosition().target.latitude,
                                    mGoogleMap.getCameraPosition().target.longitude, 1);
                            mFirstName = address.get(0).getAddressLine(0);
                            Log.d(TAG, "run: area " + address.get(0).getAdminArea() + "- ,sublocality " + address.get(0).getSubLocality() + " ,featurename "
                                    + address.get(0).getFeatureName()
                                    + " ,locality " + address.get(0).getLocality() + " ,addresline " + address.get(0).getAddressLine(1));
                            txtNameFisrt.setText(mFirstName);
                            btnAccept.setEnabled(true);

                        } catch (IOException e) {
                            txtNameFisrt.setText(getString(R.string.loading));
                            e.printStackTrace();
                        } catch (Exception e) {
                            txtNameFisrt.setText(getString(R.string.loading));
                        }

                    }
                };
                mHandler.postDelayed(requestLocation, 200);

            }
        });
        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                txtNameFisrt.setText(R.string.loading);
                if (requestLocation != null) {
                    mHandler.removeCallbacks(requestLocation);
                    btnAccept.setEnabled(false);
                }
            }
        });
    }

    /**
     * check permision location
     *
     * @return
     */
    private boolean checkPermissionLocation() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        return permission1 != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * set up listen location
     */
    private void setUpListenLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION_LOCATION);
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mCurrLocation = location;
                resultLocation = location;
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mCurrLocation.getLatitude(), mCurrLocation.getLongitude()), 12));
                try {
                    if (mGeocoder != null) {
                        address = mGeocoder.getFromLocation(mCurrLocation.getLatitude(), mCurrLocation.getLongitude(), 1);
                        mFirstName = address.get(0).getAddressLine(0);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: quyền đã đc cho");
                    setUpListenLocation();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
