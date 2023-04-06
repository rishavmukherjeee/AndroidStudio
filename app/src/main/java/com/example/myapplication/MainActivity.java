package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> createNotification());

        IntentFilter filter = new IntentFilter(NotificationButtonReceiver.ACTION_INCREMENT_COUNT);
        registerReceiver(new NotificationButtonReceiver(), filter);
    }

    private void createNotification() {
        Intent intent = new Intent(this, NotificationButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My Notification")
                .setContentText("This is my sticky notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .addAction(R.drawable.ic_launcher_foreground, "Click Me (" + count + ")", pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification";
            String description = "This is my sticky notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public class NotificationButtonReceiver extends BroadcastReceiver {
        public static final String ACTION_INCREMENT_COUNT = "com.example.myapplication.INCREMENT_COUNT";

        @Override
        public void onReceive(Context context, Intent intent) {
            count++; // increment the count here
            createNotification();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1);
        }
    }
}
