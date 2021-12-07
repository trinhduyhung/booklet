package com.javaspace.booklet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.squareup.picasso.Picasso;

public class NotificationUtil {

    public static void showWarningNotification(Context context, String booksToRead) {

        String CHANNEL_ID = "booklet";

        final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
                "Verbose WorkManager Notifications";

         String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
                "Shows notifications whenever work starts";

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, VERBOSE_NOTIFICATION_CHANNEL_NAME, importance);
            channel.setDescription(VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(context, BooksActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.coffee_maker_primary_24dp)
                .setContentTitle(context.getString(R.string.start_reading))
                .setContentText(booksToRead)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);

        NotificationManagerCompat.from(context).notify(100, builder.build());
    }

    public static void showReadingNotification(Context context, int bookId, String title,
                                               String timerProgress, String coverImagePath) {

        final String CHANNEL_ID = "booklet_reading";
        final CharSequence CHANNEL_NAME = "Reading Books Notifications";
        final String CHANNEL_DESCRIPTION = "Shows notifications whenever user starts reading a book";
        final int NOTIFICATION_ID = 101;

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = BookDetailActivity.newIntent(context, bookId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_reading);
        remoteView.setTextViewText(R.id.txt_title, title);
        remoteView.setTextViewText(R.id.txt_timer_progress, timerProgress);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.coffee_maker_primary_24dp)
                .setCustomContentView(remoteView)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[0]);

        Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);

        Picasso.get().load(new MediaSaver(context).getFile(coverImagePath)).into(remoteView,
                R.id.img_cover, NOTIFICATION_ID, notification);
    }

}
