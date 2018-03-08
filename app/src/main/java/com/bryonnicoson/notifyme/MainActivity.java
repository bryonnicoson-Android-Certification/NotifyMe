package com.bryonnicoson.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.bryonnicoson.notifyme.ANDROID";
    //private static final String CHANNEL_NAME = "ANDROID CHANNEL";
    private static final int NOTIFICATION_ID = 0;

    private Button mNotifyButton;
    private NotificationManager mNotificationManager;
    //private NotificationChannel mNotificationChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create NotificationChannel only on API 26+
            // NotificationChannel class is not in the support library
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            try {
                mNotificationManager.createNotificationChannel(channel);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        //mNotificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        mNotifyButton = findViewById(R.id.notify);
        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    public void sendNotification() {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("You've been notified!")
                        .setContentText("This is your notification text.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.ic_android);

        Notification myNotification = notificationBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, myNotification);
    }
}
