package com.example.firebaseexample.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.firebaseexample.Config;
import com.example.firebaseexample.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseCloudMessageActivity extends AppCompatActivity {

    Spinner spinnerTopic;
    Button btnChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_cloud_message);

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(Config.CHANNEL_ID, Config.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//        NotificationChannel mChannel = new NotificationChannel()
            channel.setDescription(Config.CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            mNotification.createNotificationChannel(channel);

            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String topic = spinnerTopic.getSelectedItem().toString();
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                }
            });
        }
    }

    private void init(){
        spinnerTopic = (Spinner)findViewById(R.id.spinnerTopic);
        btnChoose = (Button)findViewById(R.id.buttonChoose);
    }
}
