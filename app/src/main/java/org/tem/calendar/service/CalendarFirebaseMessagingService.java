package org.tem.calendar.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.tem.calendar.R;
import org.tem.calendar.activities.DailyActivity;

import java.util.Objects;

public class CalendarFirebaseMessagingService extends FirebaseMessagingService {

    private static final int NOTIFICATION_ID = 12034;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
    }

    private void sendNotification(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, DailyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.daily_calendar)
                        .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        NotificationChannel channel = new NotificationChannel(channelId,
                channelId,
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        System.out.println("TOKEN+++++++++++++++=" + token);
        super.onNewToken(token);
    }
}
