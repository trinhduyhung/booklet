package com.javaspace.booklet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackIdleTimeWorker extends Worker {

    public TrackIdleTimeWorker(@NonNull Context context,
                               @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    String CHANNEL_ID = "booklet";

    private static final String TAG = "TrackIdleTimeWorker";
    public static final CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    public static String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: I'm working");

        List<Book> books = InMemoryBookStore.getInstance().getAddedBooks();

        StringBuilder booksToRead = new StringBuilder();

        for (Book book : books) {
            Date pausedTime = book.getPausedTime();
            Date now = new Date();
            long differenceInMillis = now.getTime() - pausedTime.getTime();

            // TODO read time interval from SharedPreference later

            long differenceInMinutes = TimeUnit.MINUTES.convert(differenceInMillis, TimeUnit.MILLISECONDS);

            // if we pause reading for 1 minute, we will be notified
            if (differenceInMinutes <= 10) {
                booksToRead.append(book.getTitle());
                booksToRead.append("\n");
            }
        }

        if (!booksToRead.toString().isEmpty()) {
            showNotification(booksToRead.toString());
        }

        return Result.success();
    }

    private void showNotification(String booksToRead) {
        Context context = getApplicationContext();

        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

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
}
