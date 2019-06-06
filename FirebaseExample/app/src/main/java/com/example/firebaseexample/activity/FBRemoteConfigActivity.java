package com.example.firebaseexample.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.firebaseexample.BuildConfig;
import com.example.firebaseexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FBRemoteConfigActivity extends AppCompatActivity {

    double result_1 = 0;
    long versionCode = 0;
    private FirebaseRemoteConfig mRemoteConfig;
    public static long CONFIG_EXPIRE_SECOND = 12 * 3600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbremote_config);

        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        mRemoteConfig.setDefaults(R.xml.default_remote_config);
        mRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(3600)
                .setFetchTimeoutInSeconds(CONFIG_EXPIRE_SECOND)
                .build());
        versionCode = getVersionCode();
        Log.d("VVV", "onCreate: " + versionCode);
        mRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
//                            Toast.makeText(PlaceActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    mRemoteConfig.activate();
                    result_1 = displayWelcomeMessage();
                    Log.d("VVV ", "onComplete: " + result_1);
                    Toast.makeText(FBRemoteConfigActivity.this, (int) result_1 + "" + "version code" + versionCode, Toast.LENGTH_SHORT).show();
                    if (versionCode < (int) result_1) {
                        showDialog_1();
                    }
                } else {
                    Toast.makeText(FBRemoteConfigActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getVersionCode() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }

    private double displayWelcomeMessage() {
        return mRemoteConfig.getDouble("VERSION_CODE");
    }

    private void showDialog_1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notification");
        builder.setMessage("Bạn hãy cập nhật phiên bản mới nhất");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setVersionCode();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setVersionCode() {
        Toast.makeText(this, "Bạn đã cập nhật version mới", Toast.LENGTH_SHORT).show();
    }
}
