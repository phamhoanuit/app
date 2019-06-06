package com.example.firebaseexample.model;

import android.util.Log;

import com.example.firebaseexample.MyNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FMSService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        remoteMessage.getData().keySet().add("Thông báo");
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title,body);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
