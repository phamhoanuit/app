package com.example.firebaseexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.example.firebaseexample.activity.FirebaseCloudMessageActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager {

    private Context context;
    private static MyNotificationManager mNotificationManager;


    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context){
        if(mNotificationManager == null){
            mNotificationManager = new MyNotificationManager(context);
        }
        return mNotificationManager;
    }

    public void displayNotification(String titleMessage, String messageBody){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, Config.CHANNEL_ID)
                .setContentTitle(titleMessage)
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//                .setBadgeIconType(R.drawable.test)
//                .setLargeIcon(getBitmapfromUrl("https://picture.dzogame.vn/Img/19e900067fe727452a52_pp_285.jpg"));
//                .setLargeIcon(getBitmapfromUrl("https://scontent.fsgn2-2.fna.fbcdn.net/v/t1.0-9/35886256_1817548334966944_8810748043327438848_n.jpg?_nc_cat=103&oh=99e9593685276fe8c1691a1ccef65ec4&oe=5C541E23"));

        Intent intent = new Intent(context, FirebaseCloudMessageActivity.class);

        PendingIntent pendingIntent =  PendingIntent.getActivity(context,0, intent , PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(0,mBuilder.build());
        }

    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
