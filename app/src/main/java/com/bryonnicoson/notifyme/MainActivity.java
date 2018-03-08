package com.bryonnicoson.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "com.bryonnicoson.notifyme.ANDROID";
    private static final int NOTIFICATION_ID = 0;
    private static final String NOTIFICATION_GUIDE_URL =
            "https://developer.android.com/design/patterns/notifications.html";
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.bryonnicoson.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationManager mNotificationManager;
    private NotificationChannel mNotificationChannel;
    private NotificationReceiver mNotificationReceiver;

    private Intent mNotificationIntent;
    private Intent mLearnMoreIntent;
    private Intent mUpdateIntent;
    private Intent mDeleteIntent;
    private PendingIntent mDeletePendingIntent;
    private PendingIntent mNotificationPendingIntent;
    private PendingIntent mLearnMorePendingIntent;
    private PendingIntent mUpdatePendingIntent;

    private Button mNotifyButton;
    private Button mUpdateButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotifyButton = findViewById(R.id.notify);
        mNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChannel();
                sendNotification();
            }
        });

        mUpdateButton = findViewById(R.id.update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(false);

        // notification intent
        mNotificationIntent = new Intent(this, MainActivity.class);
        mNotificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // learn more implicit intent
        mLearnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_GUIDE_URL));
        mLearnMorePendingIntent = PendingIntent.getActivity(
                this, NOTIFICATION_ID, mLearnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        // update implicit intent
        mUpdateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        mUpdatePendingIntent = PendingIntent.getBroadcast(
                this, NOTIFICATION_ID, mUpdateIntent, PendingIntent.FLAG_ONE_SHOT);

        // delete intent - for when user deletes notification from the notification panel
        // mDeleteIntent = new Intent(Intent.ACTION_DELETE);
        // mDeletePendingIntent = PendingIntent.getActivity()

        // register Broadcast Receiver to receive intent
        registerReceiver(mNotificationReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));
    }

    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create NotificationChannel only on API 26+
            // NotificationChannel class is not in the support library
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            mNotificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationChannel.setDescription(description);

            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }
    }

    public void sendNotification() {

        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(true);
        mCancelButton.setEnabled(true);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("You've been notified!")
                        .setContentText("You should now be aware of this.")
                        .setContentIntent(mNotificationPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_android)
                        .addAction(R.drawable.ic_update, "Update", mUpdatePendingIntent)
                        .addAction(R.drawable.ic_learn_more, "Learn More", mLearnMorePendingIntent);

        Notification myNotification = notificationBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, myNotification);
    }

    private void updateNotification() {
        mNotifyButton.setEnabled(false);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(true);

        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("You've been notified!")
                        .setContentText("This is your notification text.")
                        .setContentIntent(mNotificationPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.ic_android)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(androidImage)
                                .setBigContentTitle("Notification Updated!"))
                        .addAction(R.drawable.ic_learn_more, "Learn More!", mLearnMorePendingIntent);;

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void cancelNotification() {
        mNotifyButton.setEnabled(true);
        mUpdateButton.setEnabled(false);
        mCancelButton.setEnabled(false);

        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNotificationReceiver);
        super.onDestroy();
    }

    public class NotificationReceiver extends BroadcastReceiver {
        public NotificationReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }
}
